import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController } from 'ionic-angular';
import { BasePage } from '../base/base';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';
import { TydatePipe } from '../../pipes/tydate/tydate';
import { AppGlobal, AppServiceProvider } from '../../providers/app-service/app-service';

/**
 * Generated class for the RmCoolingOnPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  providers:[TydatePipe],
  selector: 'page-rm-cooling-on',
  templateUrl: 'rm-cooling-on.html',
})
export class RmCoolingOnPage extends BasePage{

  param:any = {
    "loginName":"",
    "id":"",
    "sessionId":"",
    "warehouseCode":"",
    "startTime":"",
    "powerGrainCooler":"",
    "coolingPurpose":"",
    "outletTemperature":"",
    "remark":""
  }

  coolingMachinePowers: any = [ { name: '', options: [] } ];
  purposes: any = [ { name: '', options: [] } ];

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
    console.log('ionViewDidLoad RmCoolingOnPage');
    this.initParam();
    this.getOperationRecord();
  }

  initPickers(){
    if (!AppGlobal.dict){
      this.toast("配置信息加载失败，请退出重新进入加载");
      return;
    }
    this.initPicker(this.coolingMachinePowers,AppGlobal.dict.sys_gljgl);
    this.initPicker(this.purposes,AppGlobal.dict.sys_jwmd);
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
    this.param['startTime'] = this.datePipe.transform(new Date(),"yyyy-MM-dd HH:mm:ss");
    this.param['warehouseCode'] = AppGlobal.wareHouse.warehouseCode;
    this.param['userName'] = AppServiceProvider.getInstance().userinfo.userName;
  }

  getOperationRecord(){
    this.net.httpPost(AppGlobal.API.getGrainCoolingByCodeOpen, 
      {
        loginName:AppServiceProvider.getInstance().userinfo.loginName,
        sessionId:AppServiceProvider.getInstance().userinfo.token,
        warehouseCode:AppGlobal.wareHouse.warehouseCode
      }, 
      (resp) => {
        if (resp.content && resp.content != null){
          this.param = resp.content;
          this.param['loginName'] = AppServiceProvider.getInstance().userinfo.loginName;
          this.param['sessionId'] = AppServiceProvider.getInstance().userinfo.token;
        }
    }, (error) => {
      this.toast(error);
    }, true);
  }

  post(){
    this.net.httpPost(AppGlobal.API.saveOpenGrainCoolingByCode, this.param, 
      (resp) => {
        this.toast("提交成功!");
    }, (error) => {
      this.toast(error);
    }, true);
  }
}
