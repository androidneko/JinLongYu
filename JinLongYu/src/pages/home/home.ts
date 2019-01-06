import { Component, ViewChild } from '@angular/core';
import { IonicPage, NavController, Tabs, NavParams, Events } from 'ionic-angular';
import { DeviceIntefaceServiceProvider } from '../../providers/device-inteface-service/device-inteface-service';
import { AppServiceProvider } from '../../providers/app-service/app-service';
import { BasePage } from '../base/base';

/**
 * Generated class for the HomePage tabs.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage extends BasePage{

  storeInfoRoot = 'StoreInfoPage'
  regularMaintainRoot = 'RegularMaintainPage'
  dailyMaintainRoot = 'DailyMaintainPage'
  userInfoRoot = 'UserInfoPage'

  @ViewChild('tabs')  tabs:Tabs;
  constructor(public device:DeviceIntefaceServiceProvider,
    public navCtrl: NavController,
    public navParams: NavParams,
    public events:Events) {
    super(navCtrl,navParams);
    events.subscribe('logoutNotification',()=>{
      AppServiceProvider.getInstance().userinfo.appType = "";
      this.device.push("updateUserInfo",JSON.stringify(AppServiceProvider.getInstance().userinfo),()=>{
        this.navCtrl.setRoot("HomePage");
      });
    });
  }

  ionViewDidEnter(){

  }

  onTabChanged(){
    if (this.tabs.getSelected().root == "CollectionPage"){
    }
  }
}
