import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController } from 'ionic-angular';
import { BasePage } from '../base/base';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';
import { AppGlobal, AppServiceProvider } from '../../providers/app-service/app-service';

/**
 * Generated class for the RmAirShowPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-rm-air-show',
  templateUrl: 'rm-air-show.html',
})
export class RmAirShowPage extends BasePage{
  param:any = {
    "loginName":"",
    "sessionId":"",
    "id":"",
    "showRemark":""
  }

  constructor(
    public navCtrl: NavController, 
    public navParams: NavParams,
    public net: TyNetworkServiceProvider,
    public toastCtrl: ToastController) {
      super(navCtrl,navParams,toastCtrl);
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad RmAirShowPage');
    this.param['loginName'] = AppServiceProvider.getInstance().userinfo.loginName;
    this.param['sessionId'] = AppServiceProvider.getInstance().userinfo.token;
  }


  post(){
    this.net.httpPost(AppGlobal.API.saveShowMechanicalVentilationById, this.param, 
      (resp) => {
        this.toast("提交成功!");
    }, (error) => {
      this.toast(error);
    }, true);
  }
}
