package com.androidcat.jly.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.androidcat.jly.application.Configs;
import com.androidcat.jly.bean.PayCommonInfo;
import com.androidcat.jly.consts.GConfig;
import com.androidcat.jly.device.utils.ToolUtils;
import com.androidcat.jly.utils.log.LogU;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public class AppUtils {

  /**
   * ERROR_UNKNOW
   */
  private static final String ERROR_UNKNOW = "未知错误";

  /**
   * ERROR_UNKNOW CODE
   */
  private static final String ERROR_UNKNOW_KEY = "999999";

  /**
   * VOLUME SharedPreferences
   */
  private static final String VOLUME_SHARED = "VOLUME";

  /**
   * 音量值
   */
  private static final String VOLUME_VALUE = "VALUE";

  /**
   * 调节媒体音量至最大，以使客户端与设备通讯效果最佳
   */
  public static void adjustVolume(Context context) {
    AudioManager autoManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

    int current = autoManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    context.getSharedPreferences(VOLUME_SHARED, Context.MODE_PRIVATE).edit().putInt(VOLUME_VALUE, current).commit();

    int maxVolume = autoManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    autoManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_SHOW_UI);
  }

  /**
   * 恢复程序运行前的音量
   */
  public static void restoreVolume(Context context) {
    AudioManager autoManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

    int previous = context.getSharedPreferences(VOLUME_SHARED, Context.MODE_PRIVATE).getInt(VOLUME_VALUE, 1);
    autoManager.setStreamVolume(AudioManager.STREAM_MUSIC, previous, AudioManager.FLAG_PLAY_SOUND);
  }

  /**
   * 程序退出调用
   */
  public static void appExit() {
    Process.killProcess(Process.myPid());
  }

  public static final int LOW_LEVEL = 1;// 弱
  public static final int MIDDLE_LEVEL = 2;// 中
  public static final int HIGH_LEVEL = 3;// 强

  /**
   * @Title getPswLevel
   * @Description 判断密码强弱程度 --> \\p{Alnum}:字母或数字 ；\\p{Alpha}:字母；\\P{Alnum}:符号
   * @param @param password 密码
   * @param @return
   * @return int 密码强弱程度：1:弱；2：中；3：强
   * @throws
   */
  public static int getPswLevel(String password) {
    int level = LOW_LEVEL;
    if (TextUtils.isEmpty(password)) {
      return level;
    } else {
      if (password.matches("^\\d{8,16}$") || password.matches("^[a-zA-Z]{8,16}$")
        || password.matches("^\\P{Alnum}{8,16}$")) {// 1类字符组合 弱
        level = LOW_LEVEL;
      } else if (password.matches("^\\p{Alnum}{8,16}$") || password.matches("^[\\p{Alpha}\\P{Alnum}]{8,16}$")
        || password.matches("^[\\d\\P{Alnum}]{8,16}$")) {// 2类字符组合 中
        level = MIDDLE_LEVEL;
      } else if (password.matches("^[\\p{Alnum}\\P{Alnum}]{8,16}$")) {// 3类字符组合
        // 强
        level = HIGH_LEVEL;
      } else if (password.matches("^[\\p{Alnum}\\P{Alnum}]{0,7}$")) {// 小于6位
        // 弱
        level = LOW_LEVEL;
      }
    }
    return level;
  }

  /**
   * 解析本地未配置错误信息
   *
   * @param result
   * @return
   */
  public static String getCommonError(JSONObject result) {
    String key = ERROR_UNKNOW_KEY;
    String tips = null;
    if (result != null) {
      key = result.optString("ACTION_RETURN_CODE", ERROR_UNKNOW_KEY);
      tips = result.optString("ACTION_RETURN_TIPS", null);
      if (tips == null) {
        tips = result.optString("ACTION_RETURN_MESSAGE", null);
      }
    }
    if (!TextUtils.isEmpty(tips)) {
      if (!tips.contains(key)) {
        return tips + "(" + key + ")";
      } else {
        return tips;
      }
    }
    return ERROR_UNKNOW + "(" + key + ")";
  }

  public static String getRequestError(JSONObject result) {
    String returnCode = "";
    String returnTips = "";
    if (result != null) {
      returnCode = result.optString("ACTION_RETURN_CODE");
      returnTips = result.optString("ACTION_RETURN_TIPS");
    }
    return returnTips + "(" + returnCode + ")";
  }

  /**
   * 取字替换应用名后的字符串
   *
   * @param context
   * @param strName
   * @return
   */
  public static String getAppStringById(Context context, String strName) {
    try {
      final String string = ResourceUtil.getStringById(context, strName);
      final String subStr = getApplicationName(context);
      return String.format(string, subStr);
    } catch (Exception e) {
      e.printStackTrace();
      return "error";
    }
  }

  /**
   * 获取包信息
   *
   * @param context
   * @return
   */
  public static PackageInfo getPackageName(Context context) {
    PackageInfo info = null;
    try {
      info = context.getPackageManager().getPackageInfo(
        context.getPackageName(), 0);
    } catch (NameNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return info;
  }

  /**
   * 获取应用程序名称
   *
   * @param context
   * @return
   */
  public static String getApplicationName(Context context) {
    try {
      PackageManager pm = context.getPackageManager();
      PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
      return packageInfo.applicationInfo.loadLabel(pm).toString();
    } catch (NameNotFoundException e) {
      e.printStackTrace();
      return "ERROR";
    }
  }


  /**
   * 组建请求数据格式
   * @param actionInfo
   * @param actionName
   * @param appId
   * @param isSecret
   * @return
   */
  public static String buildRequest(Context mContext, final String actionInfo, String actionName, String appId,
                                    boolean isSecret) {
    try {
      final JSONObject root = new JSONObject();
      root.put("ACTION_NAME", actionName);

      LogU.e("AppUtils", "ACTION_INFO:" + actionInfo);
      String encodeInfo = encodeActionInfo(isSecret, actionInfo);
      root.put("ACTION_INFO", encodeInfo);
      //置换invoker 和 token//不再置换
      root.put("ACTION_INVOKER", getSignToken(mContext, encodeInfo));
      root.put("ACTION_TOKEN", getInvoker(mContext, appId));
      LogU.d("请求报文：" + root.toString());
      return root.toString();
    } catch (JSONException e) {
      e.printStackTrace();
      return "{}";
    }
  }


  private static String encodeActionInfo(boolean isSecret, final String info) {
    if (!isSecret)
      return info;
    //long now = System.currentTimeMillis();
    String ret = JlEncodingUtil.encodeRequestV2(info);
    //Log.e("encode","encode costs:"+(System.currentTimeMillis()-now));
    return ret;
  }

  public static JSONObject getInvoker(Context mContext, final String appId) {
    PackageManager pm = mContext.getPackageManager();
    PackageInfo pi;
    final JSONObject invoker = new JSONObject();
    if (TextUtils.isEmpty(PayCommonInfo.sessionId)) {
      PayCommonInfo.setSessionId("nosessionId");
    }
    addData(invoker, "USERID", PayCommonInfo.userId);
    addData(invoker, "SESSIONID", PayCommonInfo.sessionId);
    try {
      pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
      //emalls2.0新增字段[1]
      addData(invoker, "OUTVERSION", pi.versionName);
    } catch (NameNotFoundException e) {
      e.printStackTrace();
    }
    addData(invoker, "CSN", "");
    addData(invoker, "PHONE", "");
    addData(invoker, "OSNAME", GConfig.getConfig().android.os);
    addData(invoker, "ST", System.currentTimeMillis() + "");
    TelephonyManager tm = ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE));
    addData(invoker, "IMEI", getUniqueIdetifier(mContext, tm));
    addData(invoker, "OSVER", Build.VERSION.RELEASE);
    addData(invoker, "OSDESCRIPT", Build.MODEL);
    addData(invoker, "VER", GConfig.getConfig().comm.keyVer);
    addData(invoker, "OS", GConfig.getConfig().android.os);
    //emalls2.0新增字段[2]
    addData(invoker, "PHONE_VENDER", Build.MANUFACTURER);
    //emalls2.0新增字段[3]
    addData(invoker, "INFO_CPU", Build.CPU_ABI);
    //emalls2.0新增字段[4]
    addData(invoker, "YYS", getYYS(mContext));
    //emalls2.0新增字段[5]
    addData(invoker, "LATITUDE", Configs.getLocHm().get("Latitude"));
    //emalls2.0新增字段[6]
    addData(invoker, "LONGITUDE", Configs.getLocHm().get("Longitude"));
    //emalls2.0新增字段[7]
    addData(invoker, "ADDRESS", Configs.getLocHm().get("AddrStr"));
    //emalls2.0新增字段[8]
    addData(invoker, "PROJECT_NAME", ResourceUtil.getStringById(mContext, "R.string.app_name"));
    //emalls2.0新增字段[9]
    addData(invoker, "FBL", getFBL(mContext));
    LogU.e("AppUtils", "invoker: " + invoker.toString());
    return invoker;
  }

  public static JSONObject getInvoker2(Context mContext, final String appId) {
    PackageManager pm = mContext.getPackageManager();
    PackageInfo pi;
    final JSONObject invoker = new JSONObject();
    addData(invoker, "osname", GConfig.getConfig().android.os);
    try {
      pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
      addData(invoker, "outversion", pi.versionName);
    } catch (NameNotFoundException e) {
      e.printStackTrace();
    }
    TelephonyManager tm = ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE));
    addData(invoker, "imei", getUniqueIdetifier(mContext, tm));
    addData(invoker, "innerversion", GConfig.getConfig().comm.keyVer);
    addData(invoker, "phoneVender", Build.MANUFACTURER);
    addData(invoker, "infoCpu", Build.CPU_ABI);
    addData(invoker, "carrierOperator", getYYS(mContext));
    addData(invoker, "latitude", Configs.getLocHm().get("Latitude"));
    addData(invoker, "longitude", Configs.getLocHm().get("Longitude"));
    addData(invoker, "address", Configs.getLocHm().get("AddrStr"));
    addData(invoker, "projectName", ResourceUtil.getStringById(mContext, "R.string.app_name"));
    addData(invoker, "resolution", getFBL(mContext));
    LogU.e("AppUtils", "invoker2: " + invoker.toString());
    return invoker;
  }

  /**
   * 请求报文的ACTION_TOKEN部分
   * @param actionInfo
   * @return
   */
  public static JSONObject getSignToken(Context mContext, String actionInfo) {
    JSONObject signToken = new JSONObject();
    String timeStamp = System.currentTimeMillis() + "";

    addData(signToken, "TIMESTAMP", timeStamp);
    addData(signToken, "SIGN", AppUtils.encryptMD5(PayCommonInfo.sessionId + timeStamp + actionInfo));
    LogU.d("AppUtils", "signToken: " + signToken.toString());
    return signToken;
  }


  /**
   * 上传图片的ACTION_TOKEN部分
   * @param imgName
   * @return
   */
  public static String getImgToken(String imgName) {
    JSONObject signToken = new JSONObject();
    String timeStamp = System.currentTimeMillis() + "";
    addData(signToken, "USERID", PayCommonInfo.userId);
    addData(signToken, "TIMESTAMP", timeStamp);
    LogU.d("PayCommonInfo.sessionId:" + PayCommonInfo.sessionId);
    LogU.d("imgName:" + imgName);
    addData(signToken, "SIGN", AppUtils.encryptMD5(PayCommonInfo.sessionId + timeStamp + imgName));
    LogU.e("AppUtils", "signToken: " + signToken.toString());
    return signToken.toString();
  }

  /**
   * 获取设备唯一标识
   * @param tm
   * @return
   */
  @SuppressLint({"NewApi", "MissingPermission"})
  public static String getUniqueIdetifier(TelephonyManager tm) {
    String result = "";
    //根据不同的手机设备返回IMEI，MEID或者ESN码,非手机设备：最开始搭载Android系统都手机设备，而现在也出现了非手机设备：如平板电脑、电子书、电视、音乐播放器等。这些设备没有通话的硬件功能，系统中也就没有TELEPHONY_SERVICE，自然也就无法通过上面的方法获得DEVICE_ID。
    result = tm.getDeviceId();
    if (TextUtils.isEmpty(result)) {
      //在Android 2.3可以通过android.os.Build.SERIAL获取，非手机设备可以通过该接口获取
      result = Build.SERIAL;
    }
    return result;
  }

  /**
   * 获取设备唯一标识
   * @param mContext
   * @return
   */
  public static String getUniqueIdetifier(Context mContext, TelephonyManager tm) {

    return Build.SERIAL;
  }

  /**
   * 生成16位随机数，放到IMEI中。存放在SharedPreferences中，当卸载和清除数据时重新生成。
   * @return
   */
  private static String genRandomForVerify(Context mContext) {
    if (TextUtils.isEmpty(mContext.getSharedPreferences("ForVerify", Activity.MODE_PRIVATE).getString("RandomForVerify", ""))) {
      String tempRan = ToolUtils.getRandomStr(8);
      LogU.e("tempRan", tempRan);
      mContext.getSharedPreferences("ForVerify", Activity.MODE_PRIVATE).edit().putString("RandomForVerify", tempRan).commit();
      return tempRan;
    } else {
      return mContext.getSharedPreferences("ForVerify", Activity.MODE_PRIVATE).getString("RandomForVerify", "");
    }
  }

  /**
   * 获取SIM卡运营商
   *
   * @param context
   * @return
   */
  public static String getYYS(Context context) {
    TelephonyManager tm = (TelephonyManager) context
      .getSystemService(Context.TELEPHONY_SERVICE);
    String yys = "null";
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

      return yys;
    }
    String IMSI = tm.getSubscriberId();
    if (IMSI == null || IMSI.equals("")) {
			return yys;
		}
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
			yys = "中国移动";
		} else if (IMSI.startsWith("46001")) {
			yys = "中国联通";
		} else if (IMSI.startsWith("46003")) {
			yys = "中国电信";
		}
		return yys;
	}

	/**
	 * 获取屏幕分辨率
	 * @param context
	 * @return
	 */
	public static String getFBL(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowMgr.getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		return height + "X" + width;
	}


	private static void addData(JSONObject data, String tag, String value) {
		if (data != null) {
			try {
				String temp = value;
				if (TextUtils.isEmpty(temp)) {// format null or empty string
					temp = "";
				}
				data.put(tag, temp);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getSortedActionInfo(String actionInfo) throws JSONException{
    JSONObject info = new JSONObject(actionInfo);
    Iterator<String> iteratorKeys = info.keys();
    SortedMap map = new TreeMap();
    while (iteratorKeys.hasNext()) {
      String key = iteratorKeys.next().toString();
      String vlaue = info.optString(key);
      map.put(key, vlaue);
    }
    JSONObject json2 = new JSONObject(map);
    return json2.toString();
  }

	public static JSONObject getActionInfo(final Map<String, Object> infoMap) throws JSONException {
		JSONObject info = new JSONObject();
		if (infoMap != null && !infoMap.isEmpty()) {
			Iterator<Entry<String, Object>> it = infoMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Object> entry = (Entry<String, Object>) it.next();
				info.put(entry.getKey().trim(), entry.getValue());
			}
		}
		return info;
	}

	/**
	 * 对字符串进行MD5加密。
	 */
	public static String encryptMD5(String strInput) {
    LogU.d("AppUtils", "signToken before md5: " + strInput);
		StringBuffer buf = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(strInput.getBytes("UTF-8"));
			byte b[] = md.digest();
			buf = new StringBuffer(b.length * 2);
			for (int i = 0; i < b.length; i++) {
				if (((int) b[i] & 0xff) < 0x10) { /* & 0xff转换无符号整型 */
					buf.append("0");
				}
				buf.append(Long.toHexString((int) b[i] & 0xff)); /* 转换16进制,下方法同 */
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return buf.toString();
	}


	public static boolean isDeviceReady(Context context) {
		return true;
	}

	public static boolean isDeviceValidate(Activity personalInfoActivity) {
		return false;
	}

	public static boolean isCertificateIdValid(String certificateid) {
		if (TextUtils.isEmpty(certificateid)) {
			return false;
		}
		return certificateid.matches("^\\d{14}([0-9[xX]])$|^\\d{17}([0-9[xX]])$");
	}

	public static boolean isNewPwdUnValid(final String pwd) {
		try {
			if (pwd.length() < 8 || pwd.length() > 16) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isNewPwdUnValid2(final String pwd) {
		try {
			if (pwd.length() < 6 || pwd.length() > 16) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @Title getImgDir
	 * @Description 获取电子签名文件保存的路径，e货2.0的图片也存在这里。退出应用时清空目录
	 * @param @param activity
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getImgDir(final Context activity) {
		String cacheDir = "";
		if (hasSDCard()) {
			cacheDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/data/"
					+ activity.getPackageName();// filePath:/sdcard/
		} else {
			cacheDir = Environment.getDataDirectory().getAbsolutePath() + "/data/" + activity.getPackageName();// filePath:
																												// /data/data/
		}
		return cacheDir;
	}

	private static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		return true;
	}

	public static String loadTextFromResource(Context context, int resourceId)
            throws UnsupportedEncodingException {

        InputStream is = context.getResources().openRawResource(resourceId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is,
                "UTF-8"));
        StringBuilder result = new StringBuilder();
        String readLine = null;

        try {
            while ((readLine = br.readLine()) != null) {
                result.append(readLine);
            }

            is.close();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

	public static Intent getNewIntent(Activity mActivity, Class<? extends Activity> destKlazz) {
		return new Intent(mActivity, destKlazz);
	}

	/**
     * 程序是否在前台运行
     *
     * @return
     */
	public static boolean isAppOnForeground(Context mContext) {
		ActivityManager activityManager = (ActivityManager) mContext.getApplicationContext().getSystemService(
				Context.ACTIVITY_SERVICE);
		String packageName = mContext.getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName) && appProcess.importance ==
					RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 *
	 * @param dir
	 *            将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	/**
	 * 输出log到SD卡，对于超长的内容Logcat和debug都不能显示完全，可以输出文件
	 *
	 * @param fileName
	 * @param content
	 */
	public static void logFile(String fileName, String content) {
		try {
			String PATH = Environment.getExternalStorageDirectory().getPath() + "/QDPAY/LogFile/";
			File dir = new File(PATH);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			long current = System.currentTimeMillis();
			String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current)).substring(11, 16);
			final String FILE_NAME = fileName + time;
			final String FILE_NAME_SUFFIX = ".logfile";
			String filePath = PATH + FILE_NAME + FILE_NAME_SUFFIX;
			File file = new File(filePath);
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			pw.println(content);
			pw.close();
			LogU.d(filePath + " log success!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
     * 追加文件
     */
	public static void logFileAppend(String fileName, String content) {
		try {
			String PATH = Environment.getExternalStorageDirectory().getPath() + "/QDPAY/LogFile/";
			File dir = new File(PATH);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			long current = System.currentTimeMillis();
			String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date(current));
			String time2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current)).substring(11, 16);
			final String FILE_NAME = fileName + time;
			final String FILE_NAME_SUFFIX = ".logfile";
			String filePath = PATH + FILE_NAME + FILE_NAME_SUFFIX;
			File file = new File(filePath);
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(file, true);
			writer.write("[" + time2 + "]" + content + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 *
	 * @param context
	 * @return true 表示开启
	 */
	public static final boolean isGpsOpen(final Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gpsOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean networkOpen = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gpsOpen || networkOpen) {
			return true;
		}
		return false;
	}

	/*public static void showTipDialog(final Context context, String message, final String key, String postName, ButtonClickListener positiveListener) {
		final RootTipDialog mRootTipDialog = new RootTipDialog(context, cn.com.qdone.android.payment.R.layout.root_remind);
		mRootTipDialog.setMessage(message);
		mRootTipDialog.setTextSize(16);
		mRootTipDialog.setPositiveButton(postName, positiveListener);
		mRootTipDialog.show();

		mRootTipDialog.setCheckedListener(new CompoundButton.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked){
					AccountUtil.getInstance().rememberInfo(context, key, true);
				}else{
					AccountUtil.getInstance().rememberInfo(context, key, false);
				}
			}

		});
	}*/

	/**
	 * 检测网络是否接入代理
	 * @return
	 */
	public static boolean isHttpProxy() {
		@SuppressWarnings("deprecation")
		String proHost = android.net.Proxy.getDefaultHost();
		@SuppressWarnings("deprecation")
		int proPort = android.net.Proxy.getDefaultPort();

		if(proHost == null && proPort == -1) {
			return false;
		}

		return true;
	}

	/**
	 * 检测手机是否已获取root权限
	 * @return
	 */
	public static String getRootAuth() {
        java.lang.Process process = null;
        DataOutputStream os = null;
        try {
        process = Runtime.getRuntime().exec("su");
        os = new DataOutputStream(process.getOutputStream());
        os.writeBytes("exit\n");
        os.flush();
//        process.waitFor();
        } catch (Exception e) {
                e.printStackTrace();
                return null;
        } finally {
            try {
                   if (os != null) {
                      os.close();
                   }
                   process.destroy();
            } catch (Exception e) {}
        }
        return "0";
    }
}
