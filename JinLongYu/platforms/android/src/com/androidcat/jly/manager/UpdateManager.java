package com.androidcat.jly.manager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.androidcat.jly.R;
import com.androidcat.jly.persistense.JepayDatabase;
import com.androidcat.jly.persistense.bean.KeyValue;
import com.androidcat.jly.utils.VersionUpdateUtil;
import com.androidcat.jly.utils.dialog.DialogUtils;
import com.androidcat.jly.utils.log.LogU;
import com.androidcat.jly.view.NumberProgressBar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zt on 2015-10-21.
 */
public class UpdateManager {
  private static final String TAG = "UpdataManager";
  private static final String savePath = "/sdcard/updateAPK/"; //apk保存到SD卡的路径
  private static final String saveFileName = savePath + "JlHceWallet.apk"; //完整路径名

  private static final int DOWNLOADING = 1; //表示正在下载
  private static final int DOWNLOADED = 2; //下载完毕
  private static final int DOWNLOAD_FAILED = 3; //下载失败
  private static final int FILE_NOT_EXIST = 4; //下载地址连接不上

  private Dialog alertDialog;
  private NumberProgressBar mProgress; //下载进度条控件

  private int progress; //下载进度
  private boolean cancelFlag = false; //取消下载标志位
  private String updateDescription = "本次更新以下内容" + "\n"; //更新内容描述信息

  private Context context;
  //private VersionUpdateResponse updateResponse;
  private VersionUpdateUtil versionUpdateUtil;
  /**
   * 更新UI的handler
   */
  @SuppressLint("HandlerLeak")
  private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case DOWNLOADING:
          mProgress.setProgress(progress);
          break;
        case DOWNLOADED:
          if (alertDialog != null)
            alertDialog.dismiss();
          installAPK();
          break;
        case DOWNLOAD_FAILED:
          if (context != null)
            Toast.makeText(context, "下载失败，请确认网络和权限是否正常。", Toast.LENGTH_LONG).show();
          alertDialog.dismiss();
          break;
        case FILE_NOT_EXIST:
          if (context != null)
            Toast.makeText(context, "连接失败，请确认下载地址是否无误", Toast.LENGTH_LONG).show();
          alertDialog.dismiss();
          break;
        default:
          break;
      }
    }
  };

  public UpdateManager(Context context, VersionUpdateUtil versionUpdateUtil) {
    this.context = context;
    this.versionUpdateUtil = versionUpdateUtil;
    this.updateDescription += versionUpdateUtil.versionLog;
  }

  /**
   * 显示更新对话框
   */
  public void showNoticeDialog(final String tag) {
    try {
      alertDialog = DialogUtils.getUpdateDialog(context, versionUpdateUtil.versionName, "立即升级",
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            final LinearLayout layout_progress = (LinearLayout) alertDialog.findViewById(R.id.layout_progress);
            final LinearLayout layout_btn = (LinearLayout) alertDialog.findViewById(R.id.layout_btn);
            if (null != layout_progress) {
              layout_progress.setVisibility(View.VISIBLE);
            }
            layout_btn.setVisibility(View.GONE);
            showDownloadDialog();
          }
        }, "稍后升级", new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            //int number = SharePreferencesUtil.getIntValue(tag, 0);
            //SharePreferencesUtil.setValue(tag, ++number);
            alertDialog.dismiss();
            logCancel();
          }
        });

      TextView tv = (TextView) alertDialog.findViewById(R.id.tv_text);
      tv.setText(updateDescription);

      //如果下载的安装包，但是未安装，则先检查在判断是否下载更新
      PackageManager pm = context.getPackageManager();
      PackageInfo info = pm.getPackageArchiveInfo(saveFileName,
        PackageManager.GET_ACTIVITIES);
      if (info != null) {
        String version = info.versionName == null ? "0" : info.versionName;
        if (version.equals(versionUpdateUtil.versionName)) {
          File apkFile = new File(saveFileName);
          if (apkFile.exists()) {
            TextView tv_hint = (TextView) alertDialog.findViewById(R.id.tv_hint);
            TextView tv_btn_cancel = (TextView) alertDialog.findViewById(R.id.tv_nfc_cancel);
            TextView tv_btn_ok = (TextView) alertDialog.findViewById(R.id.tv_nfc_confirm);
            tv_btn_ok.setText("立即安装");
            tv_btn_cancel.setText("稍后安装");
            tv_hint.setText("温馨提示：发现本机有最新安装包，点击按钮选择是否安装！");
            tv_hint.setVisibility(View.GONE);
            tv_btn_ok.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                installAPK();
              }
            });
            tv_btn_cancel.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                //int number = SharePreferencesUtil.getIntValue(tag, 0);
                //SharePreferencesUtil.setValue(tag, ++number);
                if (versionUpdateUtil.isForce.equals("1")) {
                  //强制升级不能记录用户跳过升级，否则就会影响强制升级策略
                  //logCancel();
                  System.exit(0);
                } else {
                  logCancel();
                  alertDialog.dismiss();
                }
              }
            });
          }
        }
      }

      //是否强制更新
      if ("1".equals(versionUpdateUtil.isForce)) {
        TextView tv_btn_cancel = (TextView) alertDialog.findViewById(R.id.tv_nfc_cancel);
        tv_btn_cancel.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            //强制升级不能记录用户跳过升级，否则就会影响强制升级策略
            alertDialog.dismiss();
            System.exit(0);
          }
        });
        //如果是强制更新，在对话框未消失前点击Back直接退出应用
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
          @Override
          public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
              System.exit(0);
            }
            return false;
          }
        });
      }

      alertDialog.show();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private void logCancel(){
    LogU.d("-------用户跳过升级，暂不升级--------");
    //记录用户跳过升级，暂不升级
    KeyValue keyValue = new KeyValue();
    keyValue.key = "skipUpdate_"+versionUpdateUtil.versionName;
    keyValue.value = "true";
    keyValue.time = System.currentTimeMillis();
    JepayDatabase database = JepayDatabase.getInstance(context);
    database.saveCacheData(keyValue);
  }

  //显示进度条更新
  public void showDownloadDialog() {

    mProgress = (NumberProgressBar) alertDialog.findViewById(R.id.progress);
    //downloadAPK();
    downloadApkFile(versionUpdateUtil.versionPath);
  }

  //下载APK
  public void downloadAPK() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        final OkHttpClient client = new OkHttpClient();
        try {
          Request request = new Request.Builder()
            .url(versionUpdateUtil.versionPath)
            .build();
          client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
              mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
              if (!response.isSuccessful()) {
                mHandler.sendEmptyMessage(FILE_NOT_EXIST);
                return;
              }
              try {
                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                  System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                File path = new File(savePath);
                if (!path.exists()) {
                  path.mkdir();
                }
                FileOutputStream fos = new FileOutputStream(ApkFile);
                int count = 0;
                byte buf[] = new byte[1024];
                InputStream is = response.body().byteStream();
                do {
                  int numread = is.read(buf);
                  count += numread;
                  progress = (int) (((float) count / response.body().contentLength()) * 100);
                  //更新进度
                  mHandler.sendEmptyMessage(DOWNLOADING);
                  if (numread <= 0) {
                    //下载完成通知安装
                    mHandler.sendEmptyMessage(DOWNLOADED);
                    break;
                  }
                  fos.write(buf, 0, numread);
                } while (!cancelFlag); //点击取消就停止下载.

                fos.close();
                is.close();
              } catch (Exception e) {
                mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
                e.printStackTrace();
              }
            }
          });
        } catch (Exception e) {
          mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
          e.printStackTrace();
        }
      }
    }).start();
  }

  private void downloadApkFile(final String downloadUrl) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        File file = new File(savePath);
        if (!file.exists()) {
          file.mkdir();
        }
        File zipFile = new File(saveFileName);
        saveStream(zipFile, downloadUrl);
      }
    }).start();
  }

  private void saveStream(final File hexFile, final String versionPath) {
    android.util.Log.d(TAG, "saveStream");
    final OkHttpClient client = new OkHttpClient();
    try {
      Request request = new Request.Builder()
        .url(versionPath)
        .build();
      client.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
          mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
          if (!response.isSuccessful()){
            mHandler.sendEmptyMessage(FILE_NOT_EXIST);
            return;
          }
          Headers responseHeaders = response.headers();
          for (int i = 0; i < responseHeaders.size(); i++) {
            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
          }
          try {
            FileOutputStream fos = new FileOutputStream(hexFile);
            int count = 0;
            byte buf[] = new byte[1024];
            long length = response.body().contentLength();
            InputStream is = response.body().byteStream();
            do {
              int numread = is.read(buf);
              count += numread;
              progress = (int) (((float) count / length) * 100);
              //更新进度
              mHandler.sendEmptyMessage(DOWNLOADING);
              if (numread <= 0) {
                //下载完成通知安装
                mHandler.sendEmptyMessage(DOWNLOADED);
                break;
              }
              fos.write(buf, 0, numread);
            } while (true);

            fos.close();
            is.close();
          } catch (Exception e) {
            mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
            e.printStackTrace();
          }
        }
      });
    } catch (Exception e) {
      mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
      e.printStackTrace();
    }
  }

  /**
   * 下载完成后自动安装apk
   */
  public void installAPK() {
    File apkFile = new File(saveFileName);
    if (!apkFile.exists()) {
      return;
    }
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    Uri uri = null;
    if (Build.VERSION.SDK_INT >= 24) {
      uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", apkFile);
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//增加读写权限
    } else {
      uri = Uri.parse("file://" + apkFile.toString());
    }
    intent.setDataAndType(uri, "application/vnd.android.package-archive");
    context.startActivity(intent);
    System.exit(0);
  }

}
