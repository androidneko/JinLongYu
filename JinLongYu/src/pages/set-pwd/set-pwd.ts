import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController } from 'ionic-angular';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';
import { DeviceIntefaceServiceProvider } from '../../providers/device-inteface-service/device-inteface-service';
import { BasePage } from '../base/base';
import { AppGlobal, AppServiceProvider } from '../../providers/app-service/app-service';
import { Md5 } from '../../../node_modules/ts-md5/dist/md5';
import { DbServiceProvider } from '../../providers/db-service/db-service';

/**
 * Generated class for the SetPwdPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-set-pwd',
  templateUrl: 'set-pwd.html',
})
export class SetPwdPage extends BasePage {
  oldPassword: string = "";
  confirmPassword:string = "";
  
  constructor(
    public toastCtrl: ToastController, 
    public navCtrl: NavController, 
    public navParams: NavParams, 
    public db:DbServiceProvider,
    public net: TyNetworkServiceProvider, 
    public device: DeviceIntefaceServiceProvider) {
    super(navCtrl, navParams,device, toastCtrl);
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad SetPwdPage');
  }

  done() {
    if (!this.checkIfInputOk()){
      return;
    }
    this.net.httpPost(AppGlobal.API.setPwd, { pwd:Md5.hashStr(this.confirmPassword).toString().toLowerCase(), }, resp => {
      //let obj = JSON.parse(msg);
      if (resp.returnCode == AppGlobal.returnCode.succeed) {
        AppServiceProvider.getInstance().userinfo.pasFlag = '0';
        //用户信息整体保存，整体更新，整体读取
        //用户信息更新保存场景:登录、申请成功，修改用户头像（首页和我的用户信息页面）
        this.db.saveString(JSON.stringify(AppServiceProvider.getInstance().userinfo),
            AppServiceProvider.getInstance().userinfo.username+"_userinfo");
        this.toast("设置成功!");
        this.navCtrl.pop();
      } else {
        this.toast(resp.returnDes);
      }
    }, error => {
      console.log("getVerifyCode error = " + error);
      this.toast(error);
    }, true);
  }

  checkIfInputOk() {
    if (this.oldPassword.length < 6 || this.oldPassword.length > 20) {
      this.toast("密码长度只能是6-20位");
      return false;
    }
    var reg = /^[0-9A-Za-z]{6,20}$/;
    if(!reg.test(this.oldPassword))
    {
      this.toast("密码必须包含字母或数字");
      return false;
    }
    if (this.confirmPassword != this.oldPassword) {
      this.toast("两次密码输入不一致");
      return false;
    }
    return true;
  }
}
