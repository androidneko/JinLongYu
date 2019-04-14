import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, AlertController, Events, LoadingController, ToastController } from 'ionic-angular';
import { AppServiceProvider } from '../../providers/app-service/app-service';
import { BasePage } from '../base/base';

/**
 * Generated class for the UserInfoPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-user-info',
  templateUrl: 'user-info.html',
})
export class UserInfoPage extends BasePage{
  avatar:string = "";
  constructor(
    public navCtrl: NavController, 
    public loadingCtrl: LoadingController,
    public events:Events,
    public alert:AlertController,
    public toastCtrl: ToastController,
    public navParams: NavParams) {
      super(navCtrl,navParams,toastCtrl);
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad UserInfoPage');
  }

  ionViewWillEnter(){
    this.avatar = AppServiceProvider.getInstance().userinfo.avatar;
  }

  gotoAbout(){
    this.navCtrl.push("AboutPage");
  }

  gotoSettings(){
    this.navCtrl.push("SettingsPage");
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

  checkUpdate(){
    let loading = this.loadingCtrl.create({
      spinner: 'ios',
    });
    loading.present();
    setTimeout(() => {
      this.toast("当前已是最新版本");
      loading.dismiss();
    }, 2000);
  }
}
