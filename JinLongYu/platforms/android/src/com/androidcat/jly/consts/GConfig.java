package com.androidcat.jly.consts;

import com.google.gson.Gson;

/**
 * Created by androidcat on 2018/7/3.
 */

public class GConfig {

  private static GConfig config = new GConfig();

  public static GConfig getConfig() {
    return config;
  }

  public static void load(String json){
    config = new Gson().fromJson(json, GConfig.class);
  }

  public static String getDomian(){

    if (config.comm.env.equals("prod")){
      return config.comm.prod.host;
    }
    if (config.comm.env.equals("test")){
      return config.comm.test.host;
    }
    if (config.comm.env.equals("dev")){
      return config.comm.dev.host;
    }
    if (config.comm.env.equals("https")){
      return config.comm.https.host;
    }
    return config.comm.prod.host;
  }

  public static String getTraceUrl(){
    if (config.comm.env.equals("prod")){
      return config.comm.prod.traceUrl;
    }
    if (config.comm.env.equals("test")){
      return config.comm.test.traceUrl;
    }
    if (config.comm.env.equals("dev")){
      return config.comm.dev.traceUrl;
    }
    if (config.comm.env.equals("https")){
      return config.comm.https.traceUrl;
    }
    return config.comm.prod.traceUrl;
  }

  public String versionName = "1.0";
  public int versionCode = 0;

  public Comm comm = new Comm();
  public Android android = new Android();
  public Ios ios = new Ios();


  public class Comm{
    public boolean debug = true;
    public String env = "test";
    public String keyVer = "2";
    public String appId = "8a88a80f65b2e4b80165b2e7633b0000";
    public String appKey = "";
    public int mode = 1;

    public Prod prod = new Prod();
    public Test test = new Test();
    public Dev dev = new Dev();
    public Https https = new Https();

    public class Prod{
      public String host = "https://iot.whty.com.cn";
      public String traceUrl = "http://10.8.15.33:8888/pre/collectMsg";
    }

    public class Test{
      public String host = "http://yfzx.whty.com.cn:3008";
      public String traceUrl = "http://10.8.15.33:8888/pre/collectMsg";
    }

    public class Dev{
      public String host = "http://yfzx.whty.com.cn:3008";
      public String traceUrl = "http://10.8.15.33:8888/pre/collectMsg";
    }

    public class Https{
      public String host = "https://iot.whty.com.cn";
      public String traceUrl = "http://10.8.15.33:8888/pre/collectMsg";
    }
  }

  public class Android{
    public String os = "01";
  }

  public class Ios{
    public String os = "01";
    public String mode = "";
  }

}
