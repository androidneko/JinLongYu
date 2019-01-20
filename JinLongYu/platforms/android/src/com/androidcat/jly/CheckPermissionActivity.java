package com.androidcat.jly;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.androidcat.utilities.LogUtil;
import com.androidcat.utilities.permission.AndPermission;
import com.androidcat.utilities.permission.Permission;
import com.androidcat.utilities.permission.PermissionCodes;
import com.androidcat.utilities.permission.PermissionListener;
import com.androidcat.utilities.permission.PermissionUtils;
import com.androidcat.utilities.permission.Rationale;
import com.androidcat.utilities.permission.RationaleListener;
import com.androidcat.utilities.permission.SettingService;
import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;

import org.apache.cordova.CordovaActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by androidcat on 2018/6/28.
 */

public class CheckPermissionActivity extends CordovaActivity {
  public static final String TAG = "CheckPermission";
  private Toast mToast;
  private boolean isLaunchFromSetting = false;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //检查权限
    checkPermission(); //we put this in onResume
  }

  protected void checkPermission() {

      List<String> deniedList = new ArrayList<String>();

      if (!AndPermission.hasPermission(this, Permission.STORAGE)){
        for(int i = 0; i < Permission.STORAGE.length; i++){
          deniedList.add(Permission.STORAGE[i]);
        }
      }
      if (!AndPermission.hasPermission(this, Permission.CAMERA)){
        for(int i = 0; i < Permission.CAMERA.length; i++){
          deniedList.add(Permission.CAMERA[i]);
        }
      }
      if (!AndPermission.hasPermission(this, Permission.PHONE)){
        for(int i = 0; i < Permission.PHONE.length; i++){
          deniedList.add(Permission.PHONE[i]);
        }
      }
      /*if (!AndPermission.hasPermission(this, Permission.CONTACTS)){
        for(int i = 0; i < Permission.CONTACTS.length; i++){
          deniedList.add(Permission.CONTACTS[i]);
        }
      }
      if (!AndPermission.hasPermission(this, Permission.LOCATION)){
        for(int i = 0; i < Permission.LOCATION.length; i++){
          deniedList.add(Permission.LOCATION[i]);
        }
      }*/
      if (deniedList.size() > 0){
        AndPermission.with(this)
          .requestCode(PermissionCodes.PERMISSION_ALL)
          .permission(deniedList.toArray(new String[deniedList.size()]))
          .rationale(new RationaleListener() {
            @Override
            public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
              AndPermission.rationaleCustomDialog(CheckPermissionActivity.this,rationale).show();
              //AndPermission.rationaleDialog(SplashActivity.this, rationale).show();
            }
          })
          .callback(permissionCallback)
          .start();
      } else {
        //checkLoactionSetting();
      }
  }

  private PermissionListener permissionCallback = new PermissionListener() {
    @Override
    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
      for (String per : grantPermissions){
        com.androidcat.utilities.LogUtil.e(TAG, "您已授权访问:" + per);
      }
      com.androidcat.utilities.LogUtil.e(TAG, "您已允许所需全部权限，开始启动..");
      //checkLoactionSetting();
    }

    @Override
    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
      for (String per : deniedPermissions){
        //Logger.file("您已拒绝访问:"+per);
        com.androidcat.utilities.LogUtil.e(TAG, "您已拒绝访问:" + per);
      }
      switch (requestCode){
        case PermissionCodes.PERMISSION_ALL:
          LogUtil.d(TAG,"您已拒绝位置访问");
          // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
          if (PermissionUtils.hasDeniedPermission(deniedPermissions)) {
            // 第三种：自定义dialog样式。
            SettingService settingService =
              AndPermission.defineSettingDialog(CheckPermissionActivity.this, PermissionCodes.PERMISSION_SETTING);
            showPermissionSettingDialog(settingService);
            // 你的dialog点击了确定调用：
            // settingService.execute();
            // 你的dialog点击了取消调用：
            // settingService.cancel();
          }
          break;
        default:
          LogUtil.e(TAG, "unhandle requestCode = " + requestCode);
          break;
      }
    }
  };

  @Override
  protected void onResume() {
    super.onResume();
    if (isLaunchFromSetting){
      isLaunchFromSetting = false;
      LogUtil.e(TAG, "您前往设置所需权限并返回，再次检查权限...");
      checkPermission();
    }
  }

  private void showPermissionSettingDialog(final SettingService settingService) {
    final NormalDialog dialog = new NormalDialog(this);
    dialog.content(getString(R.string.request_permissions_prompt))
      .contentTextColor(getResources().getColor(R.color.text_black))
      .title("")
      .btnText("去授权", "退出") //
      .style(NormalDialog.STYLE_TWO)//
      .showAnim(new FlipVerticalSwingEnter())//
      .show();
    dialog.setOnBtnClickL(new OnBtnClickL() {
      @Override
      public void onBtnClick() {
        dialog.dismiss();
        settingService.execute();
        isLaunchFromSetting = true;
        //finish();
        /*if (AndPermission.hasPermission(CheckPermissionActivity.this, Permission.LOCATION)){
          checkLoactionSetting();
        }
        //showToast(getString(R.string.grant_permission_prompt));
        checkPermission();*/
      }
    }, new OnBtnClickL() {
      @Override
      public void onBtnClick() {
        dialog.dismiss();
        //settingService.execute();
        finish();
      }
    });
    dialog.setCanceledOnTouchOutside(false);
    dialog.setCancelable(false);
  }

  public void showToast(String text) {
    if (mToast == null) {
      mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
    } else {
      mToast.setText(text);
      mToast.setDuration(Toast.LENGTH_SHORT);
    }
    mToast.show();
  }
}
