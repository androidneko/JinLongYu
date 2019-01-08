import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController } from 'ionic-angular';
import { BasePage } from '../base/base';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';
import { AppGlobal } from '../../providers/app-service/app-service';
import { DeviceIntefaceServiceProvider } from '../../providers/device-inteface-service/device-inteface-service';
import { DbServiceProvider } from '../../providers/db-service/db-service';
import { Md5 } from '../../../node_modules/ts-md5/dist/md5';

/**
 * Generated class for the ForgetPasswordPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-forget-password',
  templateUrl: 'forget-password.html',
})
export class ForgetPasswordPage extends BasePage {
  phone: string = "";
  verifyCode: string = "";
  password: string = "";
  confirmPassword: string = "";
  valcodebtntext: string = "获取验证码";

  isCountingDown: boolean = false;
  timeCount: number = 60;
  intervalid: any;
  roleType:string;

  constructor(
    public device:DeviceIntefaceServiceProvider,
    public toastCtrl: ToastController, 
    public navCtrl: NavController, 
    public navParams: NavParams, 
    public db: DbServiceProvider, 
    private net: TyNetworkServiceProvider) {
    super(navCtrl, navParams,toastCtrl);
    // this.db.getString("username",ret=>{
    //   this.phone = ret;
    // });
  }

  getVerifyCodeClicked(phoneNum: string): void {
    if (this.isCountingDown) {
      return;
    }
    console.log("获取验证码");
    if (!(/^1[23456789]\d{9}$/.test(phoneNum))) {
      this.toast("请输入正确的手机号");
      return;
    }
    this.getVerifyCode(phoneNum);
  }

  countingDown() {
    this.isCountingDown = true;
    this.intervalid = setInterval(() => {
      this.timeCount--;
      if (this.timeCount == 0) {
        this.stopCounting();
      } else
        this.valcodebtntext = "再次发送(" + this.timeCount + ")";
    }, 1000);
  }

  stopCounting() {
    if (this.intervalid != null) {
      console.log("timer is clear");
      this.isCountingDown = false;
      this.timeCount = 60;
      this.valcodebtntext = "发送验证码";
      clearInterval(this.intervalid);
      this.intervalid = null;
    }
  }

  getVerifyCode(phoneNo) {
    console.log("cellPhone:" + phoneNo);
    this.net.httpPost(AppGlobal.API.smsCode, { "telephone": phoneNo, "smsType": "2" }, resp => {
      //let obj = JSON.parse(msg);
      //console.log("getVerifyCode: " + msg);
      if (resp.returnCode == AppGlobal.returnCode.succeed) {
        this.countingDown();
      } else {
        this.toast(resp.returnDes);
        this.stopCounting();
      }
    }, error => {
      console.log("getVerifyCode error = " + error);
      this.toast(error);
      this.stopCounting();
    }, true);
  }

  done() {
    if (this.checkIfInputOk(this.phone, this.verifyCode, this.password, this.confirmPassword)) {
      this.net.httpPost(AppGlobal.API.resetPassword,
        {
            "userId": this.phone,
            "captcha": this.verifyCode,
            "password":Md5.hashStr(this.password).toString().toLowerCase()
            //"password": this.password
        }, resp => {
         // console.log("resetPasswordAppWechat msg = " + msg);
          //let obj = JSON.parse(msg);
          if (resp.returnCode == AppGlobal.returnCode.succeed) {
            this.toast("重置密码成功");
            this.navCtrl.pop();
          } else {
            this.toast(resp.returnDes);
          }
        }, error => {
          this.toast(error);
        }, true);
    }
  }

  checkIfInputOk(phoneNum, verifyCode, newPassword, confirmPassword) {
    if (!(/^1[34578]\d{9}$/.test(phoneNum))) {
      this.toast("请输入正确的手机号");
      return false;
    }
    if (verifyCode.length != 6) {
      this.toast("验证码输入有误，请输入6位数字验证码");
      return false;
    }
    if (newPassword.length < 6 || newPassword.length > 20) {
      this.toast("密码长度只能是6-20位");
      return false;
    }
    var reg = /^[0-9A-Za-z]{6,20}$/;
    if(!reg.test(newPassword))
    {
      this.toast("密码由数字和字母组成");
      return false;
    }
    if(!reg.test(confirmPassword))
    {
      this.toast("密码由数字和字母组成");
      return false;
    }
    if (confirmPassword != newPassword) {
      this.toast("两次密码输入不一致");
      return false;
    }
    return true;
  }

}
