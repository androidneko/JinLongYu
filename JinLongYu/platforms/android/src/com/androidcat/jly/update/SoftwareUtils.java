package com.androidcat.jly.update;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.TextUtils;

import com.androidcat.jly.utils.FileUtils;


/**
 * 软件更新工具类
 *
 * @author pxw
 *
 */
public class SoftwareUtils {

	/**
	 * 判断程序是否安装
	 *
	 * @param context
	 *            上下文
	 * @param packageName
	 *            程序包名
	 * @return
	 */
	public static boolean isApplicationInstalled(final Context context, final String packageName) {
		if (TextUtils.isEmpty(packageName)) {
			return false;
		}
		if (null == context) {
			throw new IllegalArgumentException("context may not be null.");
		}
		try {
			context.getPackageManager().getPackageInfo(packageName, 0);
			return true;
		} catch (final NameNotFoundException e) {
			// Application not installed.
		}
		return false;
	}

	/**
	 * 取软件版本号
	 *
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		return getPackageInfo(context).versionCode;
	}

	public static int getVersionCode(Context context, String packageName) {
		return getPackageInfo(context, packageName).versionCode;
	}

	/**
	 * 取软件版名
	 *
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		return getPackageInfo(context).versionName;
	}

	/**
	 * 判断是版本是否更新
	 *
	 * @param context
	 * @param versionCodeNew
	 * @return
	 */
	public static boolean hasNewVersion(Context context, int versionCodeNew) {
		if (null == context) {
			throw new IllegalArgumentException("context may not be null.");
		}
		try {
			int versionCode = getVersionCode(context);
			if (versionCode < versionCodeNew) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean hasNewVersion(Context context, String packageName, int versionCodeNew) {
		if (null == context) {
			throw new IllegalArgumentException("context may not be null.");
		}
		try {
			int versionCode = getVersionCode(context, packageName);
			if (versionCode < versionCodeNew) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo packageInfo = null;
		try {
			PackageManager pm = context.getPackageManager();
			packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			packageInfo = new PackageInfo();
			packageInfo.versionCode = 1;
			packageInfo.versionName = "1.0";
		}
		return packageInfo;
	}

	private static PackageInfo getPackageInfo(Context context, String packageName) {
		PackageInfo packageInfo = null;
		try {
			PackageManager pm = context.getPackageManager();
			packageInfo = pm.getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			packageInfo = new PackageInfo();
			packageInfo.versionCode = 1;
			packageInfo.versionName = "1.0";
		}
		return packageInfo;
	}

	/**
	 * 更新文件路径
	 */
	private static String getApkFilePath(String fileName) {
		return FileUtils.getAppDir() + "/" + fileName;
	}

	/**
	 * 检测是否有SD卡[有测清空程序目录下文件]
	 *
	 * @param context
	 * @return
	 */
	public static boolean hasSDCard(Context context) {
		return FileUtils.hasSDCard(context);
	}

	/**
	 * 启动apk下载过程，在下载完成后自动弹出安装确认
	 *
	 * @param context
	 *            上下文
	 * @param downloadUrl
	 *            apk下载地址
	 * @param fileName
	 *            保存文件名
	 */
	public static void downloadApk(final Context context, String downloadUrl, final String fileName) {
		//downloadApk(context, downloadUrl, fileName, null);
	}

	/**
	 * 启动apk下载过程，在下载完成后自动弹出安装确认
	 *
	 * @param context
	 *            上下文
	 * @param downloadUrl
	 *            apk下载地址
	 * @param fileName
	 *            保存文件名
	 * @param stateListener
	 *            状态监听对象
	 */
	/*public static void downloadApk(final Context context, final String downloadUrl, final String fileName,
			final UpdateDialogUtils.StateListener stateListener) {
		final UpdateDialogUtils utils = new UpdateDialogUtils(context);
		utils.setMessage("正在下载，请稍候...");
		utils.setParams(downloadUrl, SoftwareUtils.getApkFilePath(fileName));
		utils.setStateListener(new UpdateDialogUtils.StateListener() {
			@Override
			public void onSucess() {
				utils.dismiss();
				TraceLogUtil.logNetSend(downloadUrl, TraceConstants.downloadApk, AppConfig.currentToken);
				if (stateListener != null) {
					stateListener.onSucess();
				} else {
					startInstall(context, fileName);
				}
			}

			@Override
			public void onFailure(String errorMsg) {
				utils.dismiss();
				TraceLogUtil.logNetError(SoftwareUtils.getApkFilePath(fileName), downloadUrl, AppConfig.currentToken);
				if (stateListener != null) {
					stateListener.onFailure(errorMsg);
				} else {
					showErrorDialog(context,"下载失败");
				}
			}
		});
		utils.setCancelable(false);
		utils.setNegativeButton(ResourceUtil.getStringId("R.string.app_cancel"), new ButtonClickListener() {
			@Override
			public void onButtonClick(int button, View v) {
				utils.dismiss();
				return;
			}
		});
		utils.show();
	}*/

	/**
	 * 启动安装
	 */
	public static void startInstall(final Context context, String fileName) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + getApkFilePath(fileName)),
				"application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 错误信息提示
	 */
	public static void showErrorDialog(Context context, String message) {
		//TextDialog.createDialog(context).setMessage(message).setPositiveButton(null).show();
	}

}
