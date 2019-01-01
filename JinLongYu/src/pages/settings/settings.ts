import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController, AlertController, Events } from 'ionic-angular';
import { BasePage } from '../base/base';
import { DeviceIntefaceServiceProvider } from '../../providers/device-inteface-service/device-inteface-service';
import { DbServiceProvider } from '../../providers/db-service/db-service';
import { AppServiceProvider } from '../../providers/app-service/app-service';

/**
 * Generated class for the SettingsPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-settings',
  templateUrl: 'settings.html',
})
export class SettingsPage extends BasePage{

  hasSetPwd = "1";

  constructor(
    public events:Events,
    public navCtrl: NavController, 
    public toastCtrl: ToastController, 
    public alert:AlertController,
    public device:DeviceIntefaceServiceProvider,
    public db: DbServiceProvider,
    public navParams: NavParams) {
    super(navCtrl, navParams,device,toastCtrl);
  }

  ionViewWillEnter(){
    this.hasSetPwd = AppServiceProvider.getInstance().userinfo.pasFlag;
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad SettingsPage');
  }

  pwdSetting(){
    if (this.hasSetPwd == "1"){
      this.navCtrl.push("SetPwdPage");
    }
    if (this.hasSetPwd == "0"){
      this.navCtrl.push("ModifyPwdPage");
    }
  }

  logout(){
    let alert = this.alert.create({
      title:'您确定退出此登陆账号吗？',
      buttons:[{
        text:'取消'
      },{
        text:'确定',
        handler:()=>{
          this.events.publish('logoutNotification');
        }
      }]
    });

    alert.present();
  }
}
