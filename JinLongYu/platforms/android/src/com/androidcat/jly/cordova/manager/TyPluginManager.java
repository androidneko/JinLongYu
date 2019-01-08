package com.androidcat.jly.cordova.manager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Base64;

import com.androidcat.acnet.manager.BaseManager;
import com.androidcat.acnet.okhttp.callback.RawResponseHandler;
import com.androidcat.acnet.okhttp.util.GsonUtil;
import com.androidcat.jly.consts.GConfig;
import com.androidcat.jly.consts.LocationInfo;
import com.androidcat.jly.cordova.plugin.CordovaConfig;
import com.androidcat.jly.manager.CheckUpdateManager;
import com.androidcat.jly.persistense.JepayDatabase;
import com.androidcat.jly.persistense.bean.KeyValue;
import com.androidcat.jly.utils.JlEncodingUtil;
import com.androidcat.jly.utils.Utils;
import com.androidcat.jly.utils.log.LogU;
import com.androidcat.utilities.persistence.SPConsts;
import com.androidcat.utilities.persistence.SharePreferencesUtil;
import com.google.gson.Gson;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by androidcat on 2017/11/20.
 */

public class TyPluginManager {
  //通用post
  public static void post(final Context context, final String message, final CallbackContext callbackContext) {
    if (!Utils.isNetworkAvailable(context)){
      callbackContext.error("网络异常，请检查网络设置");
    }
    try {
      JSONObject jsonObject = new JSONObject(message);
      String params = jsonObject.optString("postData");
      final String url = jsonObject.optString("url");

      //设置http请求header等参数，并开始请求服务端
      Map<String, String> headers = new HashMap<String, String>();
      headers.put("Content-Type", "application/json");
      headers.put("Connection", "close");

      JSONObject paramJson = new JSONObject(params);
      String finalParams = paramJson.toString();
      LogU.e("params","params:"+finalParams);
      BaseManager baseManager = new BaseManager(context);
      baseManager.post(url, finalParams,headers, new RawResponseHandler() {
        @Override
        public void onSuccess(int statusCode, String response) {
          if (com.androidcat.utilities.Utils.isNull(response)){
            callbackContext.error("服务端返回信息为空");
            return;
          }
          String finalResponse = response;
          if (com.androidcat.utilities.Utils.isNull(finalResponse) || !GsonUtil.isJson(finalResponse)){
            callbackContext.error("解析信息出错");
            return;
          }
          //业务交给上层处理更加灵活
          callbackContext.success(finalResponse);
        }

        @Override
        public void onStart(int code) {
          //do nothing...
        }

        @Override
        public void onFailure(int statusCode, String errorMsg) {
          callbackContext.error(errorMsg);
        }
      });

    } catch (JSONException e) {
      e.printStackTrace();
      callbackContext.error("解析信息出错");
    }
  }

  //通用get
  public static void get(final Context context, final String message, final CallbackContext callbackContext) {

  }

  //通用缓存存储
  public static void saveString(final Context context, final String message, final CallbackContext callbackContext) {
    try {
      JSONObject jsonObject = new JSONObject(message);
      String key = jsonObject.optString("key");
      String value = jsonObject.optString("data");
      KeyValue keyValue = new KeyValue();
      keyValue.key = key;
      keyValue.value = value;
      keyValue.time = System.currentTimeMillis();
      JepayDatabase database = JepayDatabase.getInstance(context);
      if (database.saveCacheData(keyValue)) {
        callbackContext.success();
      } else {
        callbackContext.error("数据保存失败");
      }
    } catch (JSONException e) {
      e.printStackTrace();
      callbackContext.error("解析缓存信息出错");
    }
  }

  //通用缓存读取
  public static void getString(final Context context, final String message, final CallbackContext callbackContext) {
    JepayDatabase database = JepayDatabase.getInstance(context);
    String value = database.getCacheData(message);
    if (value == null) {
      callbackContext.success("");
    } else {
      callbackContext.success(value);
    }
  }

  public static void uploadFileWithBase64String(Activity activity, final String message, final CallbackContext callbackContext) {
    try {
      JSONObject jsonObject = new JSONObject(message);
      String url = jsonObject.optString("url");
      String base64String = jsonObject.optString("base64String");
      String fileType = jsonObject.optString("fileType");
      String fileName = "file_"+System.currentTimeMillis()+fileType;

      byte[] bytes = Base64.decode(base64String, Base64.DEFAULT);// 将字符串转换为byte数组

      RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"),bytes);
      MultipartBody mBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("file",fileName,fileBody)
        .build();

        /* 下边的就和post一样了 */
      OkHttpClient client = new OkHttpClient().newBuilder()
        .connectTimeout(25, TimeUnit.SECONDS)
        .writeTimeout(25, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .build();
      Request request = new Request.Builder()
        .url(url)
        .post(mBody)
        .build();
      LogU.e("onStart", "Start UpLoad");
      client.newCall(request).enqueue(new Callback() {
        public void onResponse(Call call, Response response) throws IOException {
          if (response.isSuccessful()) {
            final String bodyStr = response.body().string();
            LogU.e(bodyStr);
            callbackContext.success(bodyStr);
          }else {
            LogU.e(response.message());
            callbackContext.error("网络异常，请检查网络设置");
          }

        }

        public void onFailure(Call call, final IOException e) {
          LogU.e(e.getMessage());
          callbackContext.error("网络异常，请检查网络设置");
        }
      });
    } catch (JSONException e) {
      e.printStackTrace();
      LogU.e(e.getMessage());
      callbackContext.error(e.getMessage());
    }
  }

  @SuppressLint("MissingPermission")
  @TargetApi(Build.VERSION_CODES.O)
  public static void push(CordovaPlugin plugin, final String message, final CallbackContext callbackContext){
    try {
      JSONObject jsonObject = new JSONObject(message);
      String code = jsonObject.optString("command");

//      if ("webView".equals(code)){
//        String url = jsonObject.optString("commandData");
//        Intent intent = new Intent(plugin.cordova.getActivity(), WebBrowserActivity.class);
//        intent.putExtra("url",url);
//        plugin.cordova.getActivity().startActivity(intent);
//        callbackContext.success();
//      }
      if ("telephone".equals(code)){
        String number = jsonObject.optString("commandData");
        Utils.call(plugin.cordova.getActivity(),number);
        callbackContext.success();
      }
      else if ("share".equals(code)){
        /*Intent intent = new Intent(plugin.cordova.getActivity(), ShareActivity.class);
        plugin.cordova.getActivity().startActivity(intent);
        callbackContext.success();*/
      }
      else if ("searchCode".equals(code)){
//        QrCodeHelperForCordova qrCodeHelper = QrCodeHelperForCordova.getQrCodeHelper(plugin, callbackContext);
//        qrCodeHelper.gotoQrCapture();
      }
      else if ("contacts".equals(code)){
        TyPluginCoreWorker.getContacts(plugin.cordova.getActivity(),callbackContext);
      } else if ("webCacheClear".equals(code)){
        TyPluginCoreWorker.clearJnbLocalStorage(plugin.cordova.getActivity());
      } else if("getVersion".equals(code)){
        callbackContext.success(getVersionName(plugin.cordova.getActivity()));
      } else if ("appCatche".equals(code)){
        String command = jsonObject.optString("commandData");
        if (command.equals("clear")){
          android.util.Log.d("cw_test", "clear cache");
          plugin.cordova.getActivity().deleteDatabase("webview.db");
          plugin.cordova.getActivity().deleteDatabase("webviewCache.db");
          File webViewCacheDir = new File(plugin.cordova.getActivity().getCacheDir().getAbsolutePath());
          if (webViewCacheDir.exists()){
            deleteFile(webViewCacheDir);
          }
        }
        callbackContext.success();
      } else if ("getLocation".equals(code)){
        JSONObject ret = new JSONObject();
        JSONObject dataInfo = new JSONObject();
        dataInfo.put("altitude", LocationInfo.altitude);
        dataInfo.put("cityName", LocationInfo.cityName);
        dataInfo.put("horizontalAccuracy", LocationInfo.accuracy);
        dataInfo.put("latitude", LocationInfo.latitude);
        dataInfo.put("longitude", LocationInfo.longitude);
        dataInfo.put("cityCode", LocationInfo.cityCode);
        dataInfo.put("adCode", LocationInfo.adCode);
        dataInfo.put("district", LocationInfo.district);
        ret.put("data",dataInfo);
        if (LocationInfo.cityName.length() > 0){
          ret.put("returnCode","SUCCESS");
          ret.put("returnDes","获取位置成功");
          callbackContext.success(ret.toString());
        } else {
          callbackContext.error("获取位置失败");
        }
      }
      else if ("prodQrCode".equals(code)){
        /*String qrCodeData = jsonObject.optString("commandData");
        JSONObject qrCodeJosn = new JSONObject(qrCodeData);
        String qrString = qrCodeJosn.optString("qrString");
        if (qrString != null) {
          PluginResult pluginResult = null;
          try {
            String qrBase64 = QrCodeHelper.generateBase64QrCode(qrString);
            pluginResult = new PluginResult(PluginResult.Status.OK, qrBase64);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
          } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error("生产二维码异常");
          }
        } else {
          callbackContext.error("待转码数据为空");
        }*/
      }
      else if ("trace".equals(code)){
        if (plugin.preferences.getBoolean("trace",false)){
          JSONObject pages = jsonObject.optJSONObject("commandData");
          String url = pages.optString("url");
          if (!Utils.isNull(url)){
            CordovaConfig.TRACE_URL = url;
            String src = pages.optString("src");
            String des = pages.optString("des");
            TyPluginCoreWorker.trace(plugin,url,src,des,callbackContext);
          }
        }
      }
      else if ("config".equals(code)){
        /*ConfigManager configManager = ConfigManager.getInstance(plugin.cordova.getActivity());
        ConfigManager.IReadConfigListener listener = new ConfigManager.IReadConfigListener() {
          @Override
          public void onReadConfigStart() {}

          @Override
          public void onReadConfigSuccess(String config) {
            callbackContext.success(config);
          }

          @Override
          public void onReadConfigFail(String err) {
            callbackContext.success(new Gson().toJson(GConfig.getConfig()));
            //callbackContext.error(err);
          }
        };
        configManager.setListener(listener);
        configManager.initConfig();*/
        callbackContext.success(new Gson().toJson(GConfig.getConfig()));
      } else if("keep".equals(code)){
        JSONObject kv = jsonObject.optJSONObject("commandData");
        String key = kv.optString("key");
        String value = kv.optString("value");
        SharePreferencesUtil.setValue(key,value);
        callbackContext.success();
      } else if("fetch".equals(code)){
        String key = jsonObject.optString("commandData");
        SharePreferencesUtil.getValue(key);
        callbackContext.success();
      } else if("commInfo".equals(code)){
        JSONObject msg = TyPluginCoreWorker.getCommInfo(plugin.cordova.getActivity());
        callbackContext.success(msg);
      } else if("jpushId".equals(code)){
        String jpushId = SharePreferencesUtil.getValue(SPConsts.JPUSH_ID);
        callbackContext.success(jpushId);
      } else if ("checkUpdate".equals(code)){
        String force = jsonObject.optString("commandData");
        CheckUpdateManager checkUpdateManager = new CheckUpdateManager(plugin.cordova.getActivity(),callbackContext);
        checkUpdateManager.checkUpdate("true".equals(force)?true:false);
      } else if ("exit".equals(code)){
        plugin.cordova.getActivity().finish();
        android.os.Process.killProcess(android.os.Process.myPid());    //获取PID
        System.exit(0);
      }
    } catch (JSONException e) {
      e.printStackTrace();
      callbackContext.error("解析缓存信息出错");
    }
  }

  private static String getVersionName(Context context){
    String versionName = SharePreferencesUtil.getValue(SPConsts.VERSION_NAME, "0");
    if (versionName.equals("0")){
      try{
        versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
      } catch (PackageManager.NameNotFoundException e){
        e.printStackTrace();
      }
    }
    return versionName;
  }


  public static void deleteFile(File file) {
    if (file.exists()) {
      if (file.isFile()) {
        file.delete();
      } else if (file.isDirectory()) {
        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
          deleteFile(files[i]);
        }
      }
      file.delete();
    } else {

    }
  }
}
