import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, Events, AlertController, ToastController } from 'ionic-angular';
import { BasePage } from '../base/base';

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
  avatar:string = "";
  constructor(
    public navCtrl: NavController, 
    public events:Events,
    public alert:AlertController,
    public toastCtrl: ToastController,
    public navParams: NavParams) {
      super(navCtrl,navParams,toastCtrl);
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad SettingsPage');
  }

  gotoModifyPwd(){
    this.navCtrl.push("ModifyPwdPage");
  }

  exit(){
    let alert = this.alert.create({
      title:'您确定退出此登陆账号吗？',
      buttons:[{
        text:'取消'
      },{
        text:'确定',
        handler:()=>{
          this.events.publish('logoutNotification');
        }
      }],
      enableBackdropDismiss:false
    });

    alert.present();
  }
}
