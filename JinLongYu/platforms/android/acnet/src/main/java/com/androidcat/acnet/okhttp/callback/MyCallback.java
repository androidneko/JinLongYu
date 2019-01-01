package com.androidcat.acnet.okhttp.callback;

import com.androidcat.acnet.okhttp.MyOkHttp;
import com.androidcat.acnet.okhttp.util.LogUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by tsy on 16/9/18.
 */
public class MyCallback implements Callback {

  private IResponseHandler mResponseHandler;

  public MyCallback(IResponseHandler responseHandler) {
    mResponseHandler = responseHandler;
  }

  @Override
  public void onFailure(Call call, final IOException e) {
    LogUtils.e("onFailure", e);
    String err = "";
    if (e instanceof java.net.ConnectException) {
      //err = "当前网络不可用，请检查你的网络设置";
      err = "服务暂时不可用,请稍后再试";
    } else if (e instanceof java.net.UnknownHostException) {
      err = "连接已断开，请检查网络是否畅通";
    } else if (e instanceof java.net.SocketTimeoutException) {
      err = "请求超时，请检查网络是否畅通";
    } else if ("Service Temporarily Unavailable".equals(e.getMessage())) {
      err = "服务暂时不可用,请稍后再试";
    } else {
      err = "请求失败，请稍候再试";
    }
    final String errMsg = err;
    MyOkHttp.mHandler.post(new Runnable() {
      @Override
      public void run() {
        mResponseHandler.onFailure(0, errMsg);
      }
    });
  }

  @Override
  public void onResponse(Call call, final Response response) {
    if (response.isSuccessful()) {
      mResponseHandler.onSuccess(response);
    } else {
      LogUtils.e("onResponse fail status=" + response.code()+"-->"+response.message());
      String err = "";
      if(response.code() == 502){
        err = "服务暂时不可用,请稍后再试";
      }else {
        err = "服务暂时不可用,请稍后再试";
      }
      final String error = err;
      MyOkHttp.mHandler.post(new Runnable() {
        @Override
        public void run() {
          mResponseHandler.onFailure(response.code(), error);
        }
      });
    }
  }
}
