package com.androidcat.jly.application;

import android.app.Application;

import com.androidcat.jly.consts.GConfig;
import com.androidcat.jly.manager.ConfigManager;
import com.androidcat.jly.utils.DeviceUuidFactory;
import com.androidcat.utilities.LogUtil;
import com.androidcat.utilities.persistence.SharePreferencesUtil;

public class JlHceApp extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    //万物之初，看配置
    ConfigManager.getInstance(this).initConfig();
    LogUtil.GLOBAL = GConfig.getConfig().comm.debug;
    //确定身份
    DeviceUuidFactory.obj(this);
    //初始化推送
    //JPushInterface.setDebugMode(true);
    //JPushInterface.init(JlHceApp.this);
    SharePreferencesUtil.init(JlHceApp.this);
  }

  @Override
  public void onTerminate() {
    super.onTerminate();
  }

}
