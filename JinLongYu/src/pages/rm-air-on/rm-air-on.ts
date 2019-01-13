import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController } from 'ionic-angular';
import { BasePage } from '../base/base';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';
import { AppGlobal, AppServiceProvider } from '../../providers/app-service/app-service';
import { TydatePipe } from '../../pipes/tydate/tydate';

/**
 * Generated class for the RmAirOnPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  providers:[TydatePipe],
  selector: 'page-rm-air-on',
  templateUrl: 'rm-air-on.html',
})
export class RmAirOnPage extends BasePage{

  param:any = {
    "loginName":"",
    "id":"",
    "sessionId":"",
    "warehouseCode":"",
    "startTime":"",
    "warehouseFanPower":"",
    "warehouseMechincalPower":"",
    "powerSiloFan":"",
    "powerSiloMechincal":"",
    "ventilationPurpose":"",
    "remark":""
  }

  whFanPowers: any = [ { name: '', options: [] } ];
  whMachinePowers: any = [ { name: '', options: [] } ];

  siloFanPowers: any = [ { name: '', options: [] } ];
  siloMachinePowers: any = [ { name: '', options: [] } ];

  purposes: any = [ { name: '', options: [] } ];

  type = "平房仓";

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
    console.log('ionViewDidLoad RmAirOnPage');
    this.initParam();
    this.getOperationRecord();
  }

  initPickers(){
    if (!AppGlobal.dict){
      this.toast("配置信息加载失败，请退出重新进入加载");
      return;
    }
    this.initPicker(this.whFanPowers,AppGlobal.dict.sys_pfcfsgl);
    this.initPicker(this.whMachinePowers,AppGlobal.dict.sys_pfcfjgl);
    this.initPicker(this.siloFanPowers,AppGlobal.dict.sys_tcfsgl);
    this.initPicker(this.siloMachinePowers,AppGlobal.dict.sys_tcfjgl);
    this.initPicker(this.purposes,AppGlobal.dict.sys_tfmd);
  }

  initPicker(picker,dict){
    if (dict){
      dict.forEach(element => {
        picker[0].options.push({text:element.dictLabel,value:element.dictValue});
      });
    }
  }

  initParam(){
    this.type = AppGlobal.wareHouse.storageType;
    this.param['loginName'] = AppServiceProvider.getInstance().userinfo.loginName;
    this.param['sessionId'] = AppServiceProvider.getInstance().userinfo.token;
    this.param['startTime'] = this.datePipe.transform(new Date(),"yyyy-MM-dd HH:mm:ss");
    this.param['warehouseCode'] = AppGlobal.wareHouse.warehouseCode;
    this.param['userName'] = AppServiceProvider.getInstance().userinfo.userName;
  }

  getOperationRecord(){
    this.net.httpPost(AppGlobal.API.getMechanicalVentilationByCodeOpen, 
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
    // if (this.type == '平房仓'){
    //   if (!this.param.warehouseFanPower){
    //     this.toast("");
    //     return;
    //   }
    //   if (!this.param.warehouseFanPower){
    //     this.toast("");
    //     return;
    //   }
    // }
    // if (this.type == '筒仓'){
    //   if (!this.param.warehouseFanPower){
    //     this.toast("");
    //     return;
    //   }
    //   if (!this.param.warehouseFanPower){
    //     this.toast("");
    //     return;
    //   }
    // }
    
    // if (!this.param.ventilationPurpose){
    //   this.toast("请选择通风目的");
    //   return;
    // }
    this.net.httpPost(AppGlobal.API.saveOpenMechanicalVentilationByCode, this.param, 
      (resp) => {
        this.toast("提交成功!");
    }, (error) => {
      this.toast(error);
    }, true);
  }
}
