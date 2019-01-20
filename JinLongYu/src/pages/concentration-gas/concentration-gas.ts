import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController } from 'ionic-angular';
import { BasePage } from '../base/base';
import { TydatePipe } from '../../pipes/tydate/tydate';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';
import { AppGlobal, AppServiceProvider } from '../../providers/app-service/app-service';

/**
 * Generated class for the ConcentrationGasPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  providers:[TydatePipe],
  selector: 'page-concentration-gas',
  templateUrl: 'concentration-gas.html',
})
export class ConcentrationGasPage extends BasePage{

  param:any = {
    "loginName":"",
    "sessionId":"",
    "warehouseCode":"",
    "optDate":"",
    "concentrationPoint01":"",
    "concentrationPoint02":"",
    "concentrationPoint03":"",
    "concentrationPoint04":"",
    "fumigationSite":"",
    "status":""
  }

  fumigationSites: any = [ { name: '', options: [] } ];
  status: any = [ { name: '', options: [] } ];

  constructor(
    public navCtrl: NavController, 
    public navParams: NavParams,
    public net: TyNetworkServiceProvider,
    private datePipe:TydatePipe,
    public toastCtrl: ToastController) {
      super(navCtrl,navParams,toastCtrl);
      this.initPickers();
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad ConcentrationGasPage');
    this.initParam();
  }

  initPickers(){
    if (!AppGlobal.dict){
      this.toast("配置信息加载失败，请退出重新进入加载");
      return;
    }
    this.initPicker(this.fumigationSites,AppGlobal.dict.sys_xzxc);
    this.initPicker(this.status,AppGlobal.dict.sys_sqzt);
  }

  initPicker(picker,dict){
    if (dict){
      dict.forEach(element => {
        picker[0].options.push({text:element.dictLabel,value:element.dictValue});
      });
    }
  }

  initParam(){
    this.param['loginName'] = AppServiceProvider.getInstance().userinfo.loginName;
    this.param['sessionId'] = AppServiceProvider.getInstance().userinfo.token;
    this.param['optDate'] = this.datePipe.transform(new Date(),"yyyy-MM-dd HH:mm:ss");
    this.param['warehouseCode'] = AppGlobal.wareHouse.warehouseCode;
    this.param['userName'] = AppServiceProvider.getInstance().userinfo.userName;
  }

  post(){
    this.net.httpPost(AppGlobal.API.saveConcentrationExhaustGasByCode, this.param, 
      (resp) => {
        this.toast("提交成功!");
    }, (error) => {
      this.toast(error);
    }, true);
  }
}
