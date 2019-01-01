package com.androidcat.jly.manager;

import android.content.Context;

import com.androidcat.acnet.entity.response.BaseResponse;
import com.androidcat.acnet.manager.BaseManager;
import com.androidcat.acnet.okhttp.callback.RawResponseHandler;
import com.androidcat.jly.utils.DeviceUuidFactory;
import com.androidcat.utilities.LogUtil;
import com.androidcat.utilities.permission.AndPermission;
import com.androidcat.utilities.permission.Permission;
import com.androidcat.jly.consts.GConfig;
import com.androidcat.jly.persistense.JepayDatabase;
import com.androidcat.jly.utils.JlEncodingUtil;
import com.androidcat.jly.utils.StringUtils;
import com.androidcat.jly.utils.Utils;
import com.androidcat.jly.utils.VersionUpdateUtil;
import com.androidcat.jly.utils.log.LogU;

import org.apache.cordova.CallbackContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by androidcat on 2018/9/18.
 */

public class CheckUpdateManager {
  private static final String TAG = "CheckUpdateManager";
  private Context context;
  private CallbackContext callbackContext;

  public CheckUpdateManager(Context context, CallbackContext callbackContext){
    this.context = context;
    this.callbackContext = callbackContext;
  }

  public void checkUpdate(final boolean forceShowUpdate) {
    String userName = JepayDatabase.getInstance(context).getCacheData("username");
    LogU.e("APPID","APP_ID:"+GConfig.getConfig().comm.appId);
    Map<String, String> headers = new HashMap<String, String>();
    headers.put("Content-Type", "application/json");
    headers.put("Connection", "close");
    headers.put("APPID",GConfig.getConfig().comm.appId);
    String token = JepayDatabase.getInstance(context).getCacheData(userName+"_token");
    if (!Utils.isNull(token)){
      headers.put("Authorization",token);
    }
    headers.put("ENCODEMETHOD","base64");
    headers.put("SIGNMETHOD","ecb");
    String finalParams = "";
    try {
      final JSONObject actionInfoObj = new JSONObject();
      actionInfoObj.put("deviceId", DeviceUuidFactory.getDeviceUuid().toString());
      actionInfoObj.put("msgId", System.currentTimeMillis()+"");
      actionInfoObj.put("systemCode", "12345678");
      actionInfoObj.put("versionCode", com.androidcat.utilities.Utils.getVersion(context));

      LogU.e("appKey","appKey:"+GConfig.getConfig().comm.appKey);
      String sign = JlEncodingUtil.getSort(JlEncodingUtil.sort(actionInfoObj),GConfig.getConfig().comm.appKey);
      LogU.e("sign","sign:"+sign);
      actionInfoObj.put("sign",sign);

      finalParams = actionInfoObj.toString();
      LogU.e("params","params:"+finalParams);
      finalParams = JlEncodingUtil.encodeRequest(finalParams);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    final String url = GConfig.getDomian() + "/hce-interface/api/appManage/queryVersion";
    BaseManager manager = new BaseManager(context);
    manager.post(url, finalParams, headers, new RawResponseHandler() {
      @Override
      public void onSuccess(int statusCode, String response) {
        LogUtil.d(TAG, "onSuccess response = " + response + " url = " + url);
        if (Utils.isNull(response)) {
          //解析信息出错
          callbackContext.error("解析版本信息出错");
          return;
        }
        if (response.contains("\"")){
          LogU.e("post","response contains \"-->"+response);
          response = response.replaceAll("\"","");
        }
        response = JlEncodingUtil.decodeResponse(response);
        JSONObject result = StringUtils.stringToJSONObject(response);
        if (result == null || !BaseResponse.SUCCESS.equals(result.optString("returnCode"))){
          callbackContext.error("服务端返回信息有误");
          return;
        }

        JSONObject obj = result.optJSONObject("data");
        String version = obj.optString("appVersionName");  //versionName
        String needUpgraded = obj.optString("isForce");
        String downloadUrl = obj.optString("setupUrl");
        String apkUrl = obj.optString("versionPath");
        String verDescribe = obj.optString("versionLog");
        String versionCode = obj.optString("appVersionCode");
        //versionCode = "2";
        try{
          int apkVersionCode = com.androidcat.utilities.Utils.getVersion(context);
          if (!com.androidcat.utilities.Utils.isNull(versionCode)) {
            if (apkVersionCode < Integer.parseInt(versionCode)) {
              if (!com.androidcat.utilities.Utils.isNull(apkUrl)) {
                VersionUpdateUtil versionUpdateUtil = new VersionUpdateUtil();
                versionUpdateUtil.isForce = needUpgraded;
                versionUpdateUtil.versionCode = versionCode;
                versionUpdateUtil.versionLog = verDescribe;
                versionUpdateUtil.versionName = version;
                versionUpdateUtil.versionPath = apkUrl;
                UpdateManager mUpdateManager = new UpdateManager(context, versionUpdateUtil);
                if (!AndPermission.hasPermission(context, Permission.STORAGE)) {
                  callbackContext.error("请开启应用存储权限!");
                  return;
                } else {
                  if (forceShowUpdate){
                    mUpdateManager.showNoticeDialog("1");
                  }else {
                    if (checkIfShowUpdate(version)){
                      mUpdateManager.showNoticeDialog("1");
                    }
                  }
                  callbackContext.success("");
                }
                return;
              } else {
                callbackContext.error("新版下载地址有误!");
                return;
              }
            } else if (apkVersionCode >= Integer.parseInt(versionCode)) {
              callbackContext.success("当前已是最新版本!");
              return;
            }
          }
          callbackContext.success("当前已是最新版本!");
          android.util.Log.d(TAG, "version = " + version + " needUpgraded = " + needUpgraded + " downloadUrl = " + downloadUrl);
        }catch (Exception e){
          e.printStackTrace();
          callbackContext.error("");
        }

      }

      @Override
      public void onStart(int code) {

      }

      @Override
      public void onFailure(int statusCode, String errorMsg) {
        callbackContext.error("请求失败，请检查网络是否畅通");
        android.util.Log.d(TAG, "onError response = " + errorMsg + " url = " + url);
      }
    });
  }


  private boolean checkIfShowUpdate(String versionName){
    //定义是否提示更新的策略
    JepayDatabase database = JepayDatabase.getInstance(context);
    String value = database.getCacheData("skipUpdate_"+versionName);
    if ("true".equals(value)){
      return false;
    }
    return true;
  }
}
