import { BasePage } from './../base/base';
import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams,ToastController, Events } from 'ionic-angular';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';
import { AppGlobal, AppServiceProvider } from '../../providers/app-service/app-service';
import { DeviceIntefaceServiceProvider } from '../../providers/device-inteface-service/device-inteface-service';


/**
 * Generated class for the ModifyPwdPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-modify-pwd',
  templateUrl: 'modify-pwd.html',
})
export class ModifyPwdPage extends BasePage {
  oldPassword:string = "";
  newPassword:string = "";
  confirmPassword:string = "";
  roleType:string = "";
  phoneNumber:string = "";

  constructor(
    public device:DeviceIntefaceServiceProvider,
    public toastCtrl: ToastController,
    public navCtrl: NavController,
    public events:Events, 
    public navParams: NavParams,
    private net:TyNetworkServiceProvider,
    ) {
    super(navCtrl,navParams,toastCtrl);
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad ModifyPwdPage');
  }

  done(){
    if(!this.checkIfPwdOk(this.oldPassword, this.newPassword,this.confirmPassword)){
      return;
    }
    this.net.httpPost(AppGlobal.API.modifyPwd,
      {
        loginName:AppServiceProvider.getInstance().userinfo.loginName,
        sessionId:AppServiceProvider.getInstance().userinfo.token,
        oldPassword:this.oldPassword,
        // pwd:Md5.hashStr(this.oldPassword).toString().toLowerCase(),
        // newPwd:Md5.hashStr(this.newPassword).toString().toLowerCase()
        password:this.newPassword
      },
      (resp) => {
        this.toast("修改成功");
        this.events.publish('logoutNotification');
      },
      (error) => {
        this.toast(error);
      },true);
  }

  checkIfPwdOk(oldPassword, newPassword,confirmPassword){
    if(oldPassword.length==0){
      this.toast("原密码不能为空");
      return false;
    }
    if(oldPassword.length < 6 || oldPassword.length > 20){
      this.toast("密码长度只能是6-20位");
      return false;
    }
    if(newPassword.length==0){
      this.toast("新密码不能为空");
      return false;
    }
    if(newPassword.length < 6 || newPassword.length > 20){
      this.toast("密码长度只能是6-20位");
      return false;
    }
    var reg = /^[0-9A-Za-z]{6,20}$/;
      if(!reg.test(newPassword))
      {
        this.toast("密码由字母或数字组成");
        return;
      }
    if(confirmPassword != newPassword){
      this.toast("两次密码输入不一致");
      return false;
    }
    return true;
  }

}
