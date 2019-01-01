import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { AppServiceProvider } from '../../providers/app-service/app-service';

/**
 * Generated class for the UserCenterPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-user-center',
  templateUrl: 'user-center.html',
})
export class UserCenterPage {
  avatar:string = "";
  info:any = {
    company:"天喻信息",
    name:"- - - -",
    num:"- - - -"
  }

  constructor(public navCtrl: NavController, public navParams: NavParams) {
  }

  ionViewWillEnter(){
    this.avatar = AppServiceProvider.getInstance().userinfo.avatar;
    if (AppServiceProvider.getInstance().userinfo.realName)
      this.info.name = AppServiceProvider.getInstance().userinfo.realName;
    if (AppServiceProvider.getInstance().userinfo.companyNo)
      this.info.num = AppServiceProvider.getInstance().userinfo.companyNo;
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad UserCenterPage');
  }

  back(){
    this.navCtrl.pop();
  }

  gotoRecords(){
    this.navCtrl.push("TyacRecordsPage");
  }

  gotoSettings(){
    this.navCtrl.push("SettingsPage");
  }

  gotoHelp(){
    this.navCtrl.push("HelpPage");
  }

  gotoFeedback(){
    this.navCtrl.push("FeedbackPage");
  }

  gotoAbout(){
    this.navCtrl.push("AboutPage");
  }

  gotoUserInfo(){
    this.navCtrl.push("UserInfoPage");
  }
}
