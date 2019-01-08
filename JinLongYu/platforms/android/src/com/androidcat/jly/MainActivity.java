/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.androidcat.jly;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.androidcat.jly.ui.CheckPermissionActivity;
import com.androidcat.utilities.LogUtil;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaPreferences;
import org.json.JSONObject;

public class MainActivity extends CheckPermissionActivity {
  private static final String TAG = "MainActivity";
  private CallbackContext mCallback = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    super.init();
    LogUtil.d(TAG, "onCreate launchUrl1 = " + launchUrl);

    // enable Cordova apps to be started in the background
    Bundle extras = getIntent().getExtras();
    if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
      moveTaskToBack(true);
    }
    registerEventReceiver();
    //load url交给父类check update
    loadUrl(launchUrl);
  }

  public CordovaPreferences getCordovaPreferences() {
    return preferences;
  }

  public void setCallBack(CallbackContext callBack) {
    mCallback = callBack;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    if (requestCode == 0 && resultCode == 1 && mCallback != null) {
      mCallback.success("webViewClose");
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    unregisterEventReceiver();
  }

  private EventReceiver eventReceiver = new EventReceiver();

  private void registerEventReceiver() {
    IntentFilter filter = new IntentFilter();
    filter.addAction("androidcat.nativeEvent");
    this.registerReceiver(eventReceiver, filter);
    Log.e("Amap", "registerEventReceiver");
  }

  private void unregisterEventReceiver() {
    this.unregisterReceiver(eventReceiver);
    Log.e("Amap", "unregisterEventReceiver");
  }

  class EventReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      String eventName = intent.getStringExtra("eventName");
      String data = intent.getStringExtra("data");
      fireWindowEvent(eventName, data);
    }
  }

  private void fireWindowEvent(String eventName, Object data) {
    String method = "";
    if (data instanceof JSONObject) {
      method = String.format("javascript:cordova.fireWindowEvent('%s', %s );", eventName, data.toString());
    } else {
      method = String.format("javascript:cordova.fireWindowEvent('%s','%s');", eventName, data.toString());
    }
    this.appView.loadUrl(method);
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    if (newConfig.fontScale != 1) {
      getResources();
    }
    super.onConfigurationChanged(newConfig);
  }

  @Override
  public Resources getResources() {
    Resources res = super.getResources();
    if (res.getConfiguration().fontScale != 1) {//非默认值
      Configuration newConfig = new Configuration();
      newConfig.setToDefaults();//设置默认
      res.updateConfiguration(newConfig, res.getDisplayMetrics());
    }
    return res;
  }
}
