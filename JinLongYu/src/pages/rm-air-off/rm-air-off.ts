import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController } from 'ionic-angular';
import { BasePage } from '../base/base';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';
import { TydatePipe } from '../../pipes/tydate/tydate';
import { AppGlobal, AppServiceProvider } from '../../providers/app-service/app-service';

/**
 * Generated class for the RmAirOffPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  providers:[TydatePipe],
  selector: 'page-rm-air-off',
  templateUrl: 'rm-air-off.html',
})
export class RmAirOffPage extends BasePage{

  param:any = {
    "loginName":"",
    "sessionId":"",
    "warehouseCode":"",
    "endTime":""
  }

  constructor(
    public navCtrl: NavController, 
    public navParams: NavParams,
    public net: TyNetworkServiceProvider,
    private datePipe:TydatePipe,
    public toastCtrl: ToastController) {
      super(navCtrl,navParams,toastCtrl);
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad RmAirOffPage');
    this.param['loginName'] = AppServiceProvider.getInstance().userinfo.loginName;
    this.param['sessionId'] = AppServiceProvider.getInstance().userinfo.token;
    this.param['endTime'] = this.datePipe.transform(new Date(),"yyyy-MM-dd HH:mm:ss");
    this.param['warehouseCode'] = AppGlobal.wareHouse.warehouseCode;
  }

  post(){
    this.net.httpPost(AppGlobal.API.saveCloseMechanicalVentilationByCode, this.param, 
      (resp) => {
        this.toast("关机成功!");
    }, (error) => {
      this.toast(error);
    }, true);
  }
}
