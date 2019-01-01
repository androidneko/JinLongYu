package com.androidcat.jly.utils;

/**
 * Created by Administrator on 2018/7/24.
 */

public class FrontBackUtil {

  public boolean isFront = false;
  private static FrontBackUtil instance = null;
  private FrontBackUtil(){

  }

  public static FrontBackUtil getInstance(){
    if (instance == null){
      instance = new FrontBackUtil();
    }
    return instance;
  }

}
