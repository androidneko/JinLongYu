package com.androidcat.jly.utils;

import android.content.Context;

import com.androidcat.acnet.manager.BaseManager;
import com.androidcat.acnet.okhttp.callback.RawResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2018/4/11.
 */

public class JPushUtil {

  public static void registerJpush(Context context, final String url, String userNo, String registrationId){
    Map<String, String> headers = new HashMap<String, String>();
    headers.put("Content-Type", "application/json");

    //String actionInfo = "{\"ACTION_NAME\":\"pushFacade|registPushInfo\", \"dataInfo\":{\"userNo\":\"13697186022\", \"registrationId\":\"13065ffa4e25362a2b1\", \"tags\":\"android\", \"alias\":\"13697186022\"}}";
    String actionInfo = "";
    String actionName = "pushFacade|registPushInfo";
    try{
      final JSONObject actionInfoObj = new JSONObject();
      actionInfoObj.put("userNo", userNo);
      actionInfoObj.put("registrationId", registrationId);
      actionInfoObj.put("tags", "android");
      actionInfoObj.put("alias", userNo);
      actionInfoObj.put("type", "3");

      final JSONObject root = new JSONObject();
      root.put("ACTION_NAME", actionName);
      root.put("dataInfo", actionInfoObj);
      actionInfo = root.toString();
    }catch (JSONException e) {
      e.printStackTrace();
    }

    android.util.Log.d("test", "actionInfo = " + actionInfo);

    String params = AppUtils.buildRequest(context, actionInfo, actionName, null, true);
    BaseManager manager = new BaseManager(context);
    manager.post(url, params, new RawResponseHandler() {
      @Override
      public void onSuccess(int statusCode, String response) {
        android.util.Log.d("test", "onSuccess response = " + response + " url = " + url);
      }

      @Override
      public void onStart(int code) {

      }

      @Override
      public void onFailure(int statusCode, String errorMsg) {
        android.util.Log.d("test", "onError response = " + errorMsg + " url = " + url);
      }
    });

  }
}
