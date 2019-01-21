
import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController, Platform } from 'ionic-angular';
import { BasePage } from '../base/base';
import { DbServiceProvider } from './../../providers/db-service/db-service';
import { AppGlobal, AppServiceProvider } from './../../providers/app-service/app-service';
import { TyNetworkServiceProvider } from './../../providers/ty-network-service/ty-network-service';
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
  loginName: string = "";

  constructor(
    public platform: Platform,
    public toastCtrl: ToastController, 
    public navCtrl: NavController, 
    public navParams: NavParams, 
    public net: TyNetworkServiceProvider, 
    public db: DbServiceProvider, 
    ) {
    super(navCtrl, navParams, toastCtrl);
    this.db.getString("username",ret=>{
      this.loginName = ret;
    });
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad LoginPage');
  }

  ionViewWillUnload() {
  }

  keydown(event) {
    if (event.keyCode == 13) {
      //返回确定按钮
      event.target.blur();
      return false;
    }
  }

  checkInfoValid() {
    return this.checkPassword();
  }


  checkPhoneNum(){
    if (this.loginName.toString().length == 0) {
      this.toast("请输入账号");
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

  tryLogin() {
    //账号登录
    if (this.checkInfoValid()) {
      //this.setRoot("LoginPage","HomePage");
        return this.login()
      .then(()=>{
        //用户信息整体保存，整体更新，整体读取
        //用户信息更新保存场景:登录、申请成功，修改用户头像（首页和我的用户信息页面）
        this.db.saveString(JSON.stringify(AppServiceProvider.getInstance().userinfo),
          AppServiceProvider.getInstance().userinfo.username+"_userinfo");
        //最终跳转页面
        this.setRoot("LoginPage","HomePage");
      });  
    }
  }

  login() {
    return new Promise((resolve,reject)=>{
      let loginParam = {};
      loginParam = {
        loginName:this.loginName,
        //password:Md5.hashStr(this.loginPassword).toString().toLowerCase()
        password:this.loginPassword
      };
      this.net.httpPost(AppGlobal.API.login, loginParam, (resp) => {
        this.db.saveString(this.loginName,"username");

        //this.db.saveString(Md5.hashStr(this.loginPassword).toString().toLowerCase(),"password");
        this.db.saveString(resp.sessionId,this.loginName + "_" + AppGlobal.Config.comm.env + "_token");
        AppServiceProvider.getInstance().userinfo.loginName = this.loginName;
        AppServiceProvider.getInstance().userinfo.userName = resp.content.userName;
        AppServiceProvider.getInstance().userinfo.token = resp.sessionId;
        AppServiceProvider.getInstance().userinfo.avatar = AppGlobal.picturePrefix +  resp.content.avatar;
        resolve();
      }, (error) => {
        this.toast(error);
      }, true);
    });
  }

}
