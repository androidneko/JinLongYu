package com.androidcat.acnet.manager;

import android.content.Context;
import android.os.Handler;

import com.androidcat.acnet.consts.InterfaceUrl;
import com.androidcat.acnet.okhttp.MyOkHttp;
import com.androidcat.acnet.okhttp.callback.DownloadResponseHandler;
import com.androidcat.acnet.okhttp.callback.MyDownloadCallback;
import com.androidcat.acnet.okhttp.callback.RawResponseHandler;
import com.androidcat.utilities.LogUtil;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: FuelMore
 * Author: androidcat
 * Email:androidcat@126.com
 * Created at: 2017-7-18 16:50:23
 * add function description here...
 */
public class BaseManager {
    public static final String BASE_URL = "http://120.25.249.105:8080/api/v1/create";
    protected Context context;
    protected Handler handler;
    protected MyOkHttp myOkHttp;

    public BaseManager(){}

    public BaseManager(Context context){
        this.context = context;
        this.myOkHttp = MyOkHttp.getInstance();
    }

    public BaseManager(Context context,Handler handler){
        this(context);
        this.handler = handler;
    }

    protected String getUrl(int code){
    return InterfaceUrl.getUrl(code);
  }

    protected Map<String,String> getHeaders(){
      Map<String,String> headers = new HashMap<>();
      headers.put("APP_ID",context.getPackageName());
      headers.put("ENCODE_METHOD","");
      headers.put("SIGN_METHOD","");
      headers.put("Authorization",context.getPackageName());
      return headers;
    }

    protected void post(int code,String json,final RawResponseHandler responseHandler){
      LogUtil.e("BaseManager", "request url:" + getUrl(code) + "\nrequest json:" + json);
      post(getUrl(code),json,responseHandler);
    }

  public void post(String url,String json,Map<String,String> headers,final RawResponseHandler responseHandler){
    LogUtil.e("BaseManager", "request url:" + url + "\nrequest json:" + json);
    myOkHttp.post()
      .url(url)
      .jsonParams(json)
      .headers(headers)
      .tag(context)
      .enqueue(new RawResponseHandler() {
        @Override
        public void onSuccess(int statusCode, String response) {
          responseHandler.onSuccess(statusCode,response);
        }

        @Override
        public void onStart(int code) {
          responseHandler.onStart(code);
        }

        @Override
        public void onFailure(int statusCode, String error_msg) {
          responseHandler.onFailure(statusCode,error_msg);
        }
      });
  }

  public void post(String url,String json,final RawResponseHandler responseHandler){
    LogUtil.e("BaseManager", "request url:" + url + "\nrequest json:" + json);
    myOkHttp.post()
      .url(url)
      .jsonParams(json)
      .tag(context)
      .enqueue(new RawResponseHandler() {
      @Override
      public void onSuccess(int statusCode, String response) {
        responseHandler.onSuccess(statusCode,response);
      }

      @Override
      public void onStart(int code) {
        responseHandler.onStart(code);
      }

      @Override
      public void onFailure(int statusCode, String error_msg) {
        responseHandler.onFailure(statusCode,error_msg);
      }
    });
  }

  public void download(String url, String dir, String fileName, DownloadResponseHandler callback){
    myOkHttp.download().url(url).fileDir(dir).fileName(fileName).tag(callback).enqueue(callback);
  }

    protected String getPostJson(Object obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

}
