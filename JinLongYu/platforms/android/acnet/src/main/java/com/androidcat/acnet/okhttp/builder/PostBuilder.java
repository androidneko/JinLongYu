package com.androidcat.acnet.okhttp.builder;

import com.androidcat.acnet.okhttp.MyOkHttp;
import com.androidcat.acnet.okhttp.callback.IResponseHandler;
import com.androidcat.acnet.okhttp.callback.MyCallback;
import com.androidcat.acnet.okhttp.util.LogUtils;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * post builder
 * Created by tsy on 16/9/18.
 */
public class PostBuilder extends OkHttpRequestBuilderHasParam<PostBuilder> {

  private String mJsonParams = "";

  public PostBuilder(MyOkHttp myOkHttp) {
    super(myOkHttp);
  }

  /**
   * json格式参数
   *
   * @param json
   * @return
   */
  public PostBuilder jsonParams(String json) {
    this.mJsonParams = json;
    return this;
  }

  @Override
  public void enqueue(IResponseHandler responseHandler) {
    try {
      if (mUrl == null || mUrl.length() == 0) {
        throw new IllegalArgumentException("url can not be null !");
      }

      Request.Builder builder = new Request.Builder().url(mUrl);
      appendHeaders(builder, mHeaders);

      if (mTag != null) {
        builder.tag(mTag);
      }

      if (mJsonParams.length() > 0) {      //上传json格式参数
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mJsonParams);
        builder.post(body);
      } else {        //普通kv参数
        FormBody.Builder encodingBuilder = new FormBody.Builder();
        appendParams(encodingBuilder, mParams);
        builder.post(encodingBuilder.build());
      }

      Request request = builder.build();

      mMyOkHttp.getOkHttpClient()
        .newCall(request)
        .enqueue(new MyCallback(responseHandler));
    } catch (Exception e) {
      LogUtils.e("Post enqueue error:" + e.getMessage());
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
      responseHandler.onFailure(0, err);
    }
  }

  //append params to form builder
  private void appendParams(FormBody.Builder builder, Map<String, String> params) {

    if (params != null && !params.isEmpty()) {
      for (String key : params.keySet()) {
        builder.add(key, params.get(key));
      }
    }
  }
}
