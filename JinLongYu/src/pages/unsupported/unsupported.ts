import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, Platform } from 'ionic-angular';
import { AppGlobal } from '../../providers/app-service/app-service';

/**
 * Generated class for the UnsupportedPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-unsupported',
  templateUrl: 'unsupported.html',
})
export class UnsupportedPage {

  tip:string = "";
  count:number = 3;

  constructor(
    public platform: Platform,
    public navCtrl: NavController, 
    public navParams: NavParams) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad UnsupportedPage');

    if (AppGlobal.commInfo.isRooted){
      this.tip = "您的手机已被root，为了您的数据安全，系统将在 "+this.count+" 秒后退出。";
      setInterval(()=>{
        this.countdown();
     },1000);
    }else {
      this.tip = "很抱歉，您的手机不支持NFC-HCE功能，无法使用本APP哦！";
    }
  }

  countdown(){
    if (this.count >= 0){
      this.tip = "您的手机已被root，为了您的数据安全，系统将在 "+this.count+" 秒后退出。";
      this.count--;
    }else {
      this.exit();
    }
  }

  exit(){
    this.platform.exitApp();
  }
}
