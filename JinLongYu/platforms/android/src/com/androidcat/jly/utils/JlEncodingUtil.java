package com.androidcat.jly.utils;

import android.util.Log;

import com.androidcat.utilities.LogUtil;
import com.androidcat.jly.consts.GConfig;
import com.androidcat.jly.utils.log.LogU;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by androidcat on 2017/12/11.
 */

public class JlEncodingUtil {

  //请求加密方法
  public static String encodeRequest(String actionInfo){
    try{
      String key = GConfig.getConfig().comm.appKey;
      long now = System.currentTimeMillis();
      //1.生成随机数
      String random =  DesTools.generalStringToAscii(8)+DesTools.generalStringToAscii(8);
      //random = "49575151515750564957515151575056";

      LogU.e("encypt","random:"+random);
      //2.生成过程密钥
      String processKey = DesTools.desecb(key,random,0);
      LogU.e("processKey","processKey:"+processKey);
      //3. 将actionInfo转换16进制后，补80
      actionInfo = DesTools.padding80(DesTools.bytesToHexString(actionInfo.getBytes("UTF-8")));
      LogU.e("actionInfo","actionInfo padding80:"+actionInfo);
      //4. 将字符串编码成16进制数字,适用于所有字符（包括中文）
      actionInfo = DesTools.encodeHexString(actionInfo);
      LogU.e("actionInfo","actionInfo encodeHexString:"+actionInfo);
      // 加密
      actionInfo = DesTools.desecb(processKey, actionInfo,0);
      // 最终生成密文
      String end = random + actionInfo;

      LogU.e("encypt","encypt data:"+end);
      LogU.e("encypt","costs:"+(System.currentTimeMillis()-now));
      return end;
    }catch (Exception e){
      LogU.e("encode request failed");
      return "encode request failed";
    }
  }

  public static String decodeResponse(String data){
    try{
      String key = GConfig.getConfig().comm.appKey;
      long now = System.currentTimeMillis();
      // 获取随机数
      String randData = data.substring(0, 32);
      Log.e("randData","randData:"+randData);
      // 获取应用密文
      String singData = data.substring(32, data.length());
      Log.e("singData","singData:"+singData);
      // 获取过程密钥
      String processKey = DesTools.desecb(key, randData,0);
      Log.e("processKey","processKey:"+processKey);
      // 解密singData
      String actionInfoString = DesTools.desecb(processKey, singData,1);
      Log.e("actionInfoString","actionInfoString:"+actionInfoString);
      // 将16进制数字解码成字符串,适用于所有字符（包括中文）
      actionInfoString = DesTools.hexStringToString(actionInfoString);
      // 最后一个'80'出现的位置
      int num = actionInfoString.lastIndexOf("80");
      // 截取actionInfoString
      if (num != -1) {
        actionInfoString = actionInfoString.substring(0, num);
      }
      // actionInfoString转换为字符串
      actionInfoString = new String(DesTools.hexToBytes(actionInfoString), "UTF-8");

      Log.e("decypt","decode data:"+actionInfoString);
      Log.e("decypt"," costs:"+(System.currentTimeMillis()-now));
      return actionInfoString;
    }catch (Exception e){
      LogU.e("decode request failed");
      return "decode request failed";
    }
  }

  //请求加密方法
  public static String encodeRequestV2(String actionInfo){
    try{
      String key = GConfig.getConfig().comm.appKey;
      // 加密
      long now = System.currentTimeMillis();
      actionInfo = DesTools.encrypt3des(key, actionInfo);
      LogUtil.e("encypt","encypt data:"+actionInfo);
      LogUtil.e("encypt","costs:"+(System.currentTimeMillis()-now));
      return actionInfo;
    }catch (Exception e){
      LogUtil.e("err","encode request failed");
      return "encode request failed";
    }
  }

  public static String decodeResponseV2(String data){
    try{
      String key = GConfig.getConfig().comm.appKey;
      long now = System.currentTimeMillis();
      // 解密singData
      String actionInfoString = DesTools.decrypt3des(key, data);
      LogUtil.e("decypt","decode data:"+actionInfoString);
      LogUtil.e("decypt"," costs:"+(System.currentTimeMillis()-now));
      return actionInfoString;
    }catch (Exception e){
      LogUtil.e("err","decode request failed");
      return "decode request failed";
    }
  }

  public static void testNewDes(){
    String key = "77c3052b141a481dd2f377c51571812c";
    String actionInfo = "{dataInfo: \"{\"endDate\":\"2018-07-05\",\"startDate\":\"2018-06-05\"}\"}杨凡";
    DesTools.str2bytes("77c3052b141a481dd2f377c5");
//    String en = DesTools.encrypt3des(key,actionInfo);
//    LogU.e("decypt","encrypt3des:"+en);
//    LogU.e("decypt","decrypt3des:"+DesTools.decrypt3des(key,en));
    decodeResponseV2(encodeRequestV2(actionInfo));

  }


  public static List<String> sort(JSONObject jsonObject) throws JSONException {
    List<String> lstSort=new ArrayList<String>();
    //Set<String> sets=jsonObject.keySet();
    Iterator<String> iterator=jsonObject.keys();
    while (iterator.hasNext()){
      String key=iterator.next();
      Object value= jsonObject.get(key);
      if(value instanceof JSONObject||value instanceof JSONArray){
        String v=jsonObject.getString(key);
        v=v.replaceAll(":","=");
        v=v.replaceAll("\"","");
        lstSort.add(key+"="+v+"&");
        //去掉引号、空格、: =
      }else{
        value=jsonObject.getString(key);
        lstSort.add(key+"="+value+"&");
      }
    }

    Collections.sort(lstSort);
    return lstSort;
  }

  public static String getSort(List<String> lstSrc, String key) {
    StringBuilder sbTmp = new StringBuilder();
    for (int i = 0; i < lstSrc.size(); i++) {
      sbTmp.append(lstSrc.get(i));
    }
    sbTmp.append("key=" + key);
    String str=sbTmp.toString();
    str=str.replaceAll(" ","");
    LogUtil.e("CommandUtil","签名元数据:"+str);
    return ConvertUtil.encrypByMd5(str).toUpperCase();
  }
}
