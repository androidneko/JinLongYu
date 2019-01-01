
import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController, ModalController, Platform } from 'ionic-angular';
import { BasePage } from '../base/base';
import { DbServiceProvider } from './../../providers/db-service/db-service';
import { AppGlobal, AppServiceProvider } from './../../providers/app-service/app-service';
import { TyNetworkServiceProvider } from './../../providers/ty-network-service/ty-network-service';
import { DeviceIntefaceServiceProvider } from './../../providers/device-inteface-service/device-inteface-service';
import { Md5 } from 'ts-md5/dist/md5';

/**
 * Generated class for the RegistPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-login',
  templateUrl: 'login.html',
})

export class LoginPage extends BasePage {
  loginPassword: string = "";
  phoneNumber: string = "";
  valcode: string = "";
  valcodebtntext: String = "获取验证码";//验证码按钮文字
  isCountingDown: boolean = false;
  timeCount: any = 60;
  intervalid: any;
  showBackButton: boolean = false;
  loginType = 1;

  constructor(
    public platform: Platform,
    public modalCtrl:ModalController,
    public toastCtrl: ToastController, 
    public navCtrl: NavController, 
    public navParams: NavParams, 
    public net: TyNetworkServiceProvider, 
    public db: DbServiceProvider, 
    public device: DeviceIntefaceServiceProvider) {
    super(navCtrl, navParams,device, toastCtrl);
    this.db.getString("username",ret=>{
      this.phoneNumber = ret;
    });
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad LoginPage');
  }

  ionViewWillUnload() {
    this.stopCounting();
  }

  getVerifyCodeClicked(phoneNum: string): void {
    if (this.isCountingDown) {
      return;
    }
    console.log("获取验证码");
    if (!(/^1[34578]\d{9}$/.test(phoneNum))) {
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
    this.net.httpPost(AppGlobal.API.smsCode, { "telephone": phoneNo, "smsType": "1" }, resp => {
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

  checkInfoValid() {
    if (this.checkPhoneNum()){
      if (this.loginType == 1){
        return this.checkValCode();
      }else {
        return this.checkPassword();
      }
    }
  }

  focusp() {
    this.loginPassword = "";
  }

  checkPhoneNum(){
    if (this.phoneNumber.toString().length == 0) {
      this.toast("请输入手机号");
      return false;
    }
    if (this.phoneNumber.toString().length != 11) {
      this.toast("手机号码长度不对");
      return false;
    }
    if (!(/^1[23456789]\d{9}$/.test(this.phoneNumber.toString()))) {
      this.toast("请输入正确的手机号");
      return false;
    }
    return true;
  }

  checkValCode() {
    if (this.valcode.toString().length == 0) {
      this.toast("请输入验证码");
      return false;
    }
    if (this.valcode.toString().length != 6) {
      this.toast("验证码长度不对");
      return false;
    }
    if (!(/^[0-9]{6}$/.test(this.valcode.toString()))) {
      this.toast("请输入正确的验证码");
      return false;
    }

    return true;
  }

  checkPassword(){
    if (this.loginPassword.length == 0) {
      this.toast("请输入密码");
      return false;
    }
    if (this.loginPassword.length < 6 || this.loginPassword.length > 20) {
      this.toast("密码长度不对");
      return false;
    }
    return true;
  }

  loginClicked() {
    //账号登录
    if (this.checkInfoValid()) {
        return this.login()
      .then(()=>{
        return this.getUserInfo();
      })
      .then(()=>{
        this.bindJpush();
        //用户信息整体保存，整体更新，整体读取
        //用户信息更新保存场景:登录、申请成功，修改用户头像（首页和我的用户信息页面）
        this.db.saveString(JSON.stringify(AppServiceProvider.getInstance().userinfo),
          AppServiceProvider.getInstance().userinfo.username+"_userinfo");
        //最终跳转页面
        this.setRoot("LoginPage","RootPage");
      });  
    }
  }

  login() {
    return new Promise((resolve,reject)=>{
      let loginParam = {};
      if (this.loginType == 1){
        loginParam = {
          userId:this.phoneNumber,
          loginType:this.loginType+"",
          captcha:this.valcode
        };
      }else {
        loginParam = {
          userId:this.phoneNumber,
          loginType:this.loginType+"",
          password:Md5.hashStr(this.loginPassword).toString().toLowerCase()
          //password:this.loginPassword
        };
      }
      this.net.httpPost(AppGlobal.API.login, loginParam, (resp) => {
        this.db.saveString(this.phoneNumber,"username");
        this.device.push("keep",{key:"vfile_username",value:this.phoneNumber});

        this.db.saveString(Md5.hashStr(this.loginPassword).toString().toLowerCase(),"password");
        this.db.saveString(resp.data.token,this.phoneNumber + "_" + AppGlobal.Config.comm.env + "_token");
        AppServiceProvider.getInstance().userinfo.username = this.phoneNumber;
        AppServiceProvider.getInstance().userinfo.token = resp.data.token;
        AppServiceProvider.getInstance().userinfo.pasFlag = resp.data.pasFlag;
        resolve();
      }, (error) => {
        this.toast(error);
      }, true);
    });
  }

  getUserInfo() {
    return new Promise((resolve,reject)=>{
      this.net.httpPost(AppGlobal.API.userInfo, {}, 
        resp => {
        AppServiceProvider.getInstance().userinfo.realName = resp.data.personName;
        AppServiceProvider.getInstance().userinfo.companyNo = resp.data.personNo;
        AppServiceProvider.getInstance().userinfo.avatar = AppGlobal.picturePrefix +  resp.data.profilePhoto;
        AppServiceProvider.getInstance().userinfo.companyId = resp.data.companyId;
        AppServiceProvider.getInstance().userinfo.deptUuid = resp.data.deptUuid;
        AppServiceProvider.getInstance().userinfo.gender = resp.data.gender;

        resolve();
      },
        error => {
          resolve();
          this.toast(error);
        }, true);
    });
  }

  bindJpush(){
    this.device.push("jpushId","",jpushId=>{
      if (jpushId){
        this.net.httpPost(AppGlobal.API.bindJpush, {
          type:"1",
          registrationId:jpushId,
          tags:this.platform.is("android")?"android":"ios",
          alias:AppServiceProvider.getInstance().userinfo.username
        },()=>{
          console.log("bindJpush-->success");
        },err=>{},false);
      }
    });
  }

  forgetpassword() {
    //this.push("ApplyPage");
    if (this.loginType==1)return;
    console.log("忘记密码")
    this.push("ForgetPasswordPage");
  }

  selectSms(){
    this.loginType = 1;
  }

  selectPwd(){
    this.loginType = 0;
  }

  legalCliked(){
    //const modal = this.modalCtrl.create("ApplySuccessAlertPage");
    const modal = this.modalCtrl.create("UserProtocolPage");
    modal.present();
  }

}
