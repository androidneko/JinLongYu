import { Component, ViewChild } from '@angular/core';
import { IonicPage, NavController, Tabs, NavParams, Events } from 'ionic-angular';
import { DeviceIntefaceServiceProvider } from '../../providers/device-inteface-service/device-inteface-service';
import { AppServiceProvider } from '../../providers/app-service/app-service';
import { BasePage } from '../base/base';
import { DbServiceProvider } from '../../providers/db-service/db-service';

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
    public db: DbServiceProvider,
    public events:Events) {
    super(navCtrl,navParams);
    events.subscribe('logoutNotification',()=>{
      console.log("----events come:logoutNotification----");
      AppServiceProvider.getInstance().userinfo.token = "";
      this.db.saveString("", AppServiceProvider.getInstance().userinfo.userName + "_token", () => {
        this.navCtrl.setRoot("LoginPage");
      });
    });
  }

  ionViewDidEnter(){

  }

  ionViewWillUnload() {
    //避免该页面多次进入后多次监听登出事件，切换登录可能发生该状况
    //this.events.unsubscribe('logoutNotification');
  }

  onTabChanged(){
    if (this.tabs.getSelected().root == "CollectionPage"){
    }
  }
}
