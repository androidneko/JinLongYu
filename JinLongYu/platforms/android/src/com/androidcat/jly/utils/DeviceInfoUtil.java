package com.androidcat.jly.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.androidcat.jly.application.Configs;

import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


/**
 * @ClassName DeviceInfoUtil
 * @Description 获取终端信息
 * @author xuxiang
 * @date 2014-9-15
 */
public class DeviceInfoUtil {
	/**
	 * default SCREEN
	 */
	private static final String DEFAULT_SCREEN = "0000";
	/**
	 * default IP
	 */
	private static final String DEFAULT_IP = "00000000";

	/**
	 * 取终端信息值  维度（16）+经度（16）+IP（8）+分辨率（宽|高）（8）
	 *
	 * @return
	 */
	public static String getDeviceInfoField() {
		final StringBuilder sb = new StringBuilder();
		sb.append(paddPosRight(Configs.getLocHm().get("Latitude"), "29.5754297789240"));
		sb.append(paddPosRight(Configs.getLocHm().get("Longitude"), "114.218927345210"));

		String ip = getLocalIpAddress();
		sb.append(formatIP(ip));

		String accuracy = paddACLeft(Configs.getHeight());
		sb.append(accuracy);
		accuracy = paddACLeft(Configs.getWidth());
		sb.append(accuracy);

		return sb.toString();
	}

	/**
	 * 取手机IP
	 * @return 返回手机IP
	 */
	private static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
		}
		return null;
	}

	private static String formatIP(String input) {
		if(TextUtils.isEmpty(input)) {
			return DEFAULT_IP;
		}
		try {
			StringBuilder sb = new StringBuilder();

			String[] vals = input.split("\\.");
			for(int i=0; i<4;i++) {
				sb.append(Tool.intToHex(Integer.valueOf(vals[i])));
			}
			return sb.toString();
		} catch (NumberFormatException e) {
			return DEFAULT_IP;
		}
	}

	private static String paddACLeft(String src) {
		if (TextUtils.isEmpty(src)) {
			return DEFAULT_SCREEN;
		}
		final StringBuilder sb = new StringBuilder();
		int len = src.length();
		if (len < 4) {
			int time = 4 - len;
			for (int i = 0; i < time; i++) {
				sb.append("0");
			}
			sb.append(src);
		} else {
			sb.append(src.substring(0, 4));
		}
		return sb.toString();
	}

	private static String paddPosRight(String src, String defValue) {
		if (TextUtils.isEmpty(src)) {
			return defValue;
		}
		final StringBuilder sb = new StringBuilder();
		int len = src.length();
		if (len < 16) {
			sb.append(src);
			int time = 16 - len;
			for (int i = 0; i < time; i++) {
				sb.append("0");
			}
		} else {
			sb.append(src.substring(0, 16));
		}
		return sb.toString();
	}

  /**
   * 获取保存的设备信息
   * @param mContext
   * @return
   */
  public static JSONObject getDeviceInfo(Context mContext) {
    JSONObject data = new JSONObject();
    SharedPreferences prefernces;
    prefernces = mContext.getSharedPreferences(AppUtils.getPackageName(mContext).packageName, 0);
    try{
      data.put("DEVICE_ID", prefernces.getString("DEVICE_ID", null));
      data.put("DEVICE_NAME", prefernces.getString("DEVICE_NAME", null));
      data.put("CONNECT_MODE", prefernces.getString("CONNECT_MODE", null));
      data.put("BLUETOOTH_MAC", prefernces.getString("BLUETOOTH_MAC", null));
      data.put("BLUETOOTH_NAME", prefernces.getString("BLUETOOTH_NAME", null));
      data.put("SOFT_KEYBOARD", prefernces.getBoolean("SOFT_KEYBOARD", false));
    }catch(Exception e){
      e.printStackTrace();
    }
    return data;
  }

}
