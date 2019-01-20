package com.androidcat.jly.cordova.manager;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.webkit.WebView;

import com.androidcat.acnet.manager.BaseManager;
import com.androidcat.acnet.okhttp.callback.RawResponseHandler;
import com.androidcat.jly.cordova.plugin.qrcode.QrCodeHelper;
import com.androidcat.jly.utils.DeviceUuidFactory;
import com.androidcat.utilities.LogUtil;
import com.androidcat.utilities.Utils;
import com.androidcat.jly.consts.GConfig;
import com.androidcat.jly.consts.LocationInfo;
import com.androidcat.jly.persistense.JepayDatabase;
import com.androidcat.jly.utils.AppUtils;
import com.androidcat.jly.utils.JlEncodingUtil;
import com.androidcat.jly.utils.ResourceUtil;
import com.androidcat.jly.utils.log.LogU;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by androidcat on 2018/3/28.
 */

public class TyPluginCoreWorker {

  /**
   * 用于处理二维码生成或识别的接口.
   *
   * @param plugin          连接此接口的插件
   * @param callbackContext 回调函数句柄；向js返回数据靠此
   */
  public static void qrcode(CordovaPlugin plugin, String title, final CallbackContext callbackContext) {
    QrCodeHelper qrCodeHelper = QrCodeHelper.getQrCodeHelper(plugin, callbackContext);
    qrCodeHelper.gotoQrCapture(title);
  }

  public static void clearJnbLocalStorage(final Activity activity){
    activity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        final WebView webView = new WebView(activity);
        //webView.getSettings().setJavaScriptEnabled(true);
        //webView.getSettings().setDomStorageEnabled(true);// 打开本地缓存提供JS调用,至关重要
        //String lastUrl = SharedPreferencesUtil.getString(activity, SPConsts.LAST_WXWEBVIEW_URL,DEFAULT_WXWEBVIEW_URL);
        //webView.loadUrl(DEFAULT_WXWEBVIEW_URL);
        //webView.loadUrl("file:///android_asset/clearCache.html");
        /*webView.setWebViewClient(new WebViewClient() {
          @Override
          public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl("javascript:window.localStorage.setItem('code','');");
            webView.destroy();
          }
        });*/
      }
    });
  }

  public static void trace(Context context,final String url, final String src,final String des){
    try {
      JepayDatabase database = JepayDatabase.getInstance(context);
      String userName = database.getCacheData("username");

      final JSONObject root = new JSONObject();
      root.put("code", (Utils.isNull(src)?"null":src) + (Utils.isNull(des)?"null":des));
      root.put("date", DateFormat.format("yyyy-MM-dd HH:mm:ss",new Date()));
      root.put("appid", context.getPackageName());
      root.put("os", "android");
      root.put("deviceid", android.os.Build.SERIAL);
      root.put("msgid", random());
      root.put("phonevender", Build.MANUFACTURER);
      root.put("sysversion", android.os.Build.VERSION.RELEASE);
      root.put("phonemodel", android.os.Build.MODEL);
      root.put("lng", LocationInfo.longitude==""?"null":LocationInfo.longitude);
      root.put("lat", LocationInfo.latitude==""?"null":LocationInfo.latitude);
      root.put("citycode", LocationInfo.cityCode==""?"null":LocationInfo.cityCode);
      root.put("dstviewcode", Utils.isNull(des)?"null":des);
      //root.put("elementcode","");
      root.put("srcviewcode", Utils.isNull(src)?"null":src);
      root.put("username", Utils.isNull(userName)?"null":userName);
      root.put("du", android.os.Build.SERIAL+"+"+userName);
      root.put("fbl", getFBL(context));
      root.put("nettype", Utils.getNetTypeStr(Utils.getAPNType(context)));
      root.put("yys",AppUtils.getYYS(context));
      //root.put("outversion", "");
      root.put("innerversion", Utils.getVersionName(context));
      root.put("projectname", ResourceUtil.getStringById(context, "R.string.app_name"));

      String params = root.toString();

      //记录日志到本地服务
      BaseManager baseManager = new BaseManager(context);
      baseManager.post(url, params, new RawResponseHandler() {
        @Override
        public void onSuccess(int statusCode, String response) {
          //LogUtil.d("trace","----trace resp:"+response);
        }

        @Override
        public void onStart(int code) {
          //do nothing...
        }

        @Override
        public void onFailure(int statusCode, String errorMsg) {
        }
      });
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public static void trace(CordovaPlugin plugin,final String url, final String src,final String des, final CallbackContext callbackContext){
    trace(plugin.cordova.getActivity(),url,src,des);
  }

  /**
   * 获取随机数
   *
   * @return 返回四位随机数
   */
  public static String random() {
    Date date = new Date();
    SimpleDateFormat dfFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    String sDate = dfFormat.format(date);
    return sDate + String.valueOf((int) (Math.random() * 9000 + 1000));
  }

  public static String getFBL(Context context){
    DisplayMetrics dm = context.getResources().getDisplayMetrics();
    int screenWidth = dm.widthPixels;
    int screenHeight = dm.heightPixels;
    return screenHeight + "*" + screenWidth;
  }

  public static JSONObject getCommInfo(Context context) throws JSONException {
    JSONObject msg = new JSONObject();
    msg.put("deviceId", DeviceUuidFactory.getDeviceUuid().toString());
    msg.put("appVersion", com.androidcat.utilities.Utils.getVersionName(context));
    msg.put("model", Build.MODEL);
    msg.put("manufacturer", Build.BRAND);
    msg.put("brand", Build.MANUFACTURER);
    msg.put("osVersion", Build.VERSION.RELEASE);
    msg.put("ipAddress", com.androidcat.utilities.Utils.getIPAddress(context));
    msg.put("networkType", com.androidcat.utilities.Utils.getNetType(com.androidcat.utilities.Utils.getAPNType(context)));
    return msg;
  }

  public static void post(final String userName,Context context,final String url,JSONObject postData,RawResponseHandler responseHandler){
    try {
      //设置http请求header等参数，并开始请求服务端
      Map<String, String> headers = new HashMap<String, String>();
      headers.put("Content-Type", "application/json");
      headers.put("Connection", "close");
      headers.put("APPID",GConfig.getConfig().comm.appId);
      LogU.e("APPID","APP_ID:"+GConfig.getConfig().comm.appId);

      if(!url.endsWith("file/upload/string")){
        headers.put("ENCODEMETHOD","base64");
      }

      if(!url.endsWith("file/upload/string")){
        headers.put("SIGNMETHOD","ecb");
      }
      String token = TyPluginCoreWorker.getToken(url,userName,context);
      if (!com.androidcat.jly.utils.Utils.isNull(token)){
        headers.put("Authorization",token);
      }

      //JSONObject paramJson = new JSONObject(postData);
      LogU.e("appKey","appKey:"+GConfig.getConfig().comm.appKey);
      String sign = JlEncodingUtil.getSort(JlEncodingUtil.sort(postData),GConfig.getConfig().comm.appKey);
      LogU.e("sign","sign:"+sign);
      postData.put("sign",sign);

      String finalParams = postData.toString();
      LogU.e("params","params:"+finalParams);
      if(!url.endsWith("file/upload/string")){
        finalParams = JlEncodingUtil.encodeRequest(finalParams);
      }
      BaseManager baseManager = new BaseManager(context);
      baseManager.post(url, finalParams,headers, responseHandler);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public static String getToken(String url,String userName,Context context){
    String token = "";
    if (GConfig.getConfig().comm.mode == 0 &&  url.contains("/card/app/m2goAppActive")){
      token = JepayDatabase.getInstance(context).getCacheData(userName + "_prod_token");
    }else {
      token = JepayDatabase.getInstance(context).getCacheData(userName + "_" + GConfig.getConfig().comm.env + "_token");
    }
    return token;
  }
}
