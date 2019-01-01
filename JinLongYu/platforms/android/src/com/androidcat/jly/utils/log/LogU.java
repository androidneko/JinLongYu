package com.androidcat.jly.utils.log;

import android.text.TextUtils;

import com.androidcat.jly.application.Configs;


/**
 * @ClassName LogU
 * @Description 打印log处理工具类
 * @date 2014-9-15
 */
public final class LogU {

	private static final String TAG = "TyHce_LogU:";

	private static final String BOUNDRY = "----";

	private LogU() {

	}

	public static void d(String msg) {
		if (Configs.isLoggable()) {
			if (TextUtils.isEmpty(msg)){
				android.util.Log.d(TAG, BOUNDRY + "msg is null" + BOUNDRY);
			} else {
				android.util.Log.d(TAG, BOUNDRY + msg + BOUNDRY);
			}
		}
	}

	public static void e(String msg) {
		if (Configs.isLoggable()) {
			if (TextUtils.isEmpty(msg)){
				android.util.Log.e(TAG, BOUNDRY + "msg is null" + BOUNDRY);
			} else {
				android.util.Log.e(TAG, BOUNDRY + msg + BOUNDRY);
			}
		}
	}

	public static void d(String msg, String msgb) {
		if (Configs.isLoggable()) {
			if (TextUtils.isEmpty(msgb)){
				android.util.Log.d(TAG, BOUNDRY + "msg is null" + BOUNDRY);
			} else {
				android.util.Log.d(TAG, BOUNDRY + msg + ":" + msgb + BOUNDRY);
			}
		}
	}

	public static void e(String msg, String msgb) {
		if (Configs.isLoggable()) {
			if (TextUtils.isEmpty(msgb)){
				android.util.Log.e(TAG, BOUNDRY + "msg is null" + BOUNDRY);
			} else {
				android.util.Log.e(TAG, BOUNDRY + msg + ":" + msgb + BOUNDRY);
			}
		}
	}

	public static void i(String msg, String msgb) {
		if (Configs.isLoggable()) {
			if (TextUtils.isEmpty(msgb)){
				android.util.Log.i(TAG, BOUNDRY + "msg is null" + BOUNDRY);
			} else {
				android.util.Log.i(TAG, BOUNDRY + msg + ":" + msgb + BOUNDRY);
			}
		}
	}

	public static void w(String msg, String msgb) {
		if (Configs.isLoggable()) {
			if (TextUtils.isEmpty(msgb)){
				android.util.Log.w(TAG, BOUNDRY + "msg is null" + BOUNDRY);
			} else {
				android.util.Log.w(TAG, BOUNDRY + msg + ":" + msgb + BOUNDRY);
			}
		}
	}

	public static void e(String tag2, String src, Exception e) {
		if (Configs.isLoggable()) {
			if (TextUtils.isEmpty(src)){
				android.util.Log.e(TAG, BOUNDRY + "msg is null" + BOUNDRY);
			} else {
				android.util.Log.e(TAG, BOUNDRY + tag2 + ":" + src+"\n"+e.toString() + BOUNDRY);
			}
		}
	}

}
