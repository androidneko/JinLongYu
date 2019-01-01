package com.androidcat.jly.application;

import java.util.HashMap;
import java.util.Map;

/**
 * Log打印，URL配置类
 *
 * @author pxw
 *
 */
public class Configs {
	/**
	 * Log打印开关
	 */
	public static boolean loggable = true;

	/**
	 * PAY_URLS值，PAY_URLS[0]为正式环境，PAY_URLS[1]准生产环境，PAY_URLS[2]调试环境
	 */
	private static final String PAY_URLS[] = new String[] {
			"http://remotepaysh.qdone.com.cn/newMpi/newPaymentServer",
			"http:///mpisht.qdone.com.cn/newMpi/newPaymentServer",
			"http://mpisht.qdone.com.cn/newMpi/newPaymentServer"
	};

	/**
	 * 生产环境
	 */
	public static final int URL_NORMAL = 0;

	/**
	 * 准生产环境
	 */
	public static final int URL_BACKUP = 1;

	/**
	 * 局域网调试环境
	 */
	public static final int URL_LOCAL = 2;

	/**
	 * URL Index
	 */
	private static int urlIndex = 0;

	/**
	 * 位置Map更加强大,不仅包括以前的经度、纬度，更有精确度（米）、详细地址、省、市
	 */
	private static Map<String, String> locHm = new HashMap<String, String>();

	/**
	 * 手机高
	 */
	private static int height = 800;

	/**
	 * 手机宽
	 */
	private static int width = 480;

	/**
	 * local http prot
	 */
	private static int httpServerPort = 15625;

	/**
	 * 设置请求接口环境
	 *
	 * @param index
	 */
	public static void setPayUrl(int index) {
		urlIndex = index;
	}

	/**
	 * 取当前请求接口地址
	 *
	 * @return
	 */
	public static String getPayUrl() {
		return PAY_URLS[urlIndex];
	}

	/**
	 * 取打印开关值
	 *
	 * @return
	 */
	public static boolean isLoggable() {
		return loggable;
	}

	/**
	 * 设置手机高完值
	 *
	 * @param h
	 *            高
	 * @param w
	 *            宽
	 */
	public static void setDisplay(int h, int w) {
		height = h;
		width = w;
	}

	/**
	 * 取高
	 *
	 * @return
	 */
	public static String getHeight() {
		return "" + height;
	}

	/**
	 * 取宽
	 *
	 * @return
	 */
	public static String getWidth() {
		return "" + width;
	}

	/**
	 * set httpServerPort
	 *
	 * @param port
	 */
	public static void setPort(int port) {
		httpServerPort = port;
	}

	/**
	 * get port value
	 *
	 * @return
	 */
	public static int getPort() {
		return httpServerPort;
	}

	public static Map<String, String> getLocHm() {
		return locHm;
	}

	public static void setLocHm(Map<String, String> locHm) {
		Configs.locHm = locHm;
	}

}
