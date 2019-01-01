package com.androidcat.jly.manager;

import android.content.Context;

import com.androidcat.acnet.manager.BaseManager;
import com.androidcat.acnet.okhttp.MyOkHttp;
import com.androidcat.acnet.okhttp.callback.DownloadResponseHandler;
import com.androidcat.acnet.okhttp.callback.RawResponseHandler;
import com.androidcat.utilities.LogUtil;
import com.androidcat.utilities.Utils;
import com.androidcat.utilities.persistence.FileUtil;
import com.google.gson.Gson;
import com.androidcat.jly.bean.ConfigResponse;
import com.androidcat.jly.consts.GConfig;
import com.androidcat.jly.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * Project: bletravel_remote
 * Author: androidcat
 * Email:androidcat@126.com
 * Created at: 2017-1-16 18:38:18
 * add function description here...
 */
public class ConfigManager {
    private static final String TAG = "WalletXmlManager_Logger";
    private static final String FILE_NAME = "config.json";
    private static final String ASSETS_PATH = "file:///android_asset/";
    private static ConfigManager manager;
    private Context context;
    private IReadConfigListener listener;
    private ConfigResponse configResponse;
    private int tryTime = 0;
    private static final int MAX_TIME = 1;

    private ConfigManager(Context context) {
        this.context = context;
    }

    public static ConfigManager getInstance(Context context) {
        if (manager == null) {
            manager = new ConfigManager(context);
        }
        return manager;
    }

    public void setListener(IReadConfigListener listener) {
        this.listener = listener;
    }

    public void initConfig() {
        if (listener != null) {
            listener.onReadConfigStart();
        }
        loadConfig();
        //compareConfigVersion();
    }

    private void loadConfig(){
      if (tryTime > MAX_TIME){
        LogUtil.d(TAG, "超过最大尝试次数，退出...");
        if (listener != null){
          listener.onReadConfigFail("加载配置超过最大尝试次数");
        }
        //重置尝试次数
        tryTime = 0;
        return;
      }
      LogUtil.d(TAG, "初始化，开始读取文件系统Config文件...tryTime:"+tryTime);
      tryTime++;
      String json = "";
      String fileJson = FileUtil.readFromSD(context.getFilesDir().getPath(),FILE_NAME);
      String assetsJson = "";
      if (Utils.isNull(fileJson)){
        LogUtil.d(TAG, "文件系统Config文件不存在，读取assets...");
        json = FileUtil.readAssetsTxt(context,FILE_NAME);
      }else {
        assetsJson = FileUtil.readAssetsTxt(context,FILE_NAME);
        GConfig fConf = new Gson().fromJson(fileJson,GConfig.class);
        GConfig aConf = new Gson().fromJson(assetsJson,GConfig.class);

        if (aConf.versionCode > fConf.versionCode){
          LogUtil.d(TAG, "文件系统Config版本低于assets版本...");
          FileUtil.copyFilesFromAssets(context,FILE_NAME,context.getFilesDir().getPath());
          LogUtil.d(TAG, "copyFilesFromAssets...");
          json = assetsJson;
        }else {
          json = fileJson;
        }
      }
      if (!Utils.isNull(json)){
        GConfig.load(json);
        LogUtil.d(TAG, "本地Config文件版本：" + GConfig.getConfig().versionCode);
        if (listener != null) {
          listener.onReadConfigSuccess(new Gson().toJson(GConfig.getConfig()));
        }
      }
    }

    private void compareConfigVersion() {
        if (configResponse == null) {
          if (!Utils.isNetworkAvailable(context)) {
            if (listener != null) {
              listener.onReadConfigFail("网络连接断开，请检查网络状态");
            }
            return;
          }

          LogUtil.d(TAG, "开始读取服务端Config文件版本...");
          Map<String, String> headers = new HashMap<String, String>();
          headers.put("Content-Type", "application/json");
          String method = "/checkConfig";
          String actionName = "";
          String actionInfo = "";
          String params = AppUtils.buildRequest(context, actionInfo, actionName, null, false);
          BaseManager manager = new BaseManager(context);
          manager.post(/*GConfig.getDomian()+method*/"", params, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
//              configResponse = new Gson().fromJson(response,ConfigResponse.class);
//              compareConfigVersion();
              JSONObject test = new JSONObject();
              try {
                test.put("fileCode",0);
                test.put("fileName","config.json");
                test.put("filePath","http://192.168.1.160:8080/demos/config.json");
                response = test.toString();
                configResponse = new Gson().fromJson(response,ConfigResponse.class);
                compareConfigVersion();
              } catch (JSONException e) {
                e.printStackTrace();
              }
            }

            @Override
            public void onStart(int code) {
            }

            @Override
            public void onFailure(int statusCode, String errorMsg) {
              JSONObject test = new JSONObject();
              try {
                test.put("fileCode",1);
                test.put("fileName","config.json");
                test.put("filePath","http://192.168.1.160:8080/demos/config.json");
                String response = test.toString();
                configResponse = new Gson().fromJson(response,ConfigResponse.class);
                compareConfigVersion();
              } catch (JSONException e) {
                e.printStackTrace();
              }
//              if (listener != null) {
//                listener.onReadConfigFail(errorMsg);
//              }
//              LogUtil.d(TAG, "onError response = " + errorMsg + " url = " + GConfig.getDomian());
            }
          });

          return;
        }

        LogUtil.d(TAG, "服务端Config文件版本：" + configResponse.fileCode);
        int cacheVer = GConfig.getConfig().versionCode;
        if (cacheVer < configResponse.fileCode) {
          LogUtil.d(TAG, "有新版，下载服务端更新文件。" );
          downloadConfig(configResponse.filePath,context.getFilesDir().getPath(),configResponse.fileName);
        } else {
            //服务端的xml版本和本地一致，使用之前读本地缓存的list即可
            if (listener != null) {
                listener.onReadConfigSuccess(new Gson().toJson(GConfig.getConfig()));
            }
          //重置尝试次数
          tryTime = 0;
          LogUtil.d(TAG, "服务端Config文件版本<=本地缓存，流程结束");
        }
    }

    private void downloadConfig(String url,String dir,String fileName) {
        MyOkHttp myOkHttp = MyOkHttp.getInstance();
        myOkHttp.download().url(url).filePath(dir+File.separator+fileName).enqueue(new DownloadResponseHandler() {
          @Override
          public void onFinish(File downloadFile) {
            LogUtil.d(TAG, "下载服务端更新文件成功，重新装载配置信息..." );
            loadConfig();
            compareConfigVersion();
          }

          @Override
          public void onProgress(long currentBytes, long totalBytes) {

          }

          @Override
          public void onFailure(String error_msg) {
            LogUtil.d(TAG, "下载服务端更新文件失败..."+error_msg);
            if (listener != null) {
              listener.onReadConfigFail("服务端返回异常，下载Config失败");
            }
          }
        });
    }

    public interface IReadConfigListener {
        void onReadConfigStart();

        void onReadConfigSuccess(String config);

        void onReadConfigFail(String err);
    }

}
