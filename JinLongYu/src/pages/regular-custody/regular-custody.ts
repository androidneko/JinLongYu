import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController } from 'ionic-angular';
import { AppGlobal, AppServiceProvider } from '../../providers/app-service/app-service';
import { BasePage } from '../base/base';
import { TydatePipe } from '../../pipes/tydate/tydate';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';

/**
 * Generated class for the RegularCustodyPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  providers:[TydatePipe],
  selector: 'page-regular-custody',
  templateUrl: 'regular-custody.html',
})
export class RegularCustodyPage extends BasePage{

  param:any = {
    "loginName":"",
    "id":"",
    "sessionId":"",
    "warehouseCode":"",
    "optDate":"",
    "warehouseRecord":"",
    "siloRecord":"",
    "remark":""
  }

  warehouseRecords: any = [ { name: '', options: [] } ];
  siloRecords: any = [ { name: '', options: [] } ];

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
    console.log('RegularCustodyPage RmAirOnPage');
    this.initParam();
    this.getOperationRecord();
  }

  initPickers(){
    if (!AppGlobal.dict){
      this.toast("配置信息加载失败，请退出重新进入加载");
      return;
    }
    this.initPicker(this.warehouseRecords,AppGlobal.dict.sys_pfcjl);
    this.initPicker(this.siloRecords,AppGlobal.dict.sys_tcjl);
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
    this.param['optDate'] = this.datePipe.transform(new Date(),"yyyy-MM-dd HH:mm:ss");
    this.param['warehouseCode'] = AppGlobal.wareHouse.warehouseCode;
    this.param['userName'] = AppServiceProvider.getInstance().userinfo.userName;
  }

  getOperationRecord(){
    this.net.httpPost(AppGlobal.API.getRegularCustodyByCodeOpen, 
      {
        loginName:AppServiceProvider.getInstance().userinfo.loginName,
        sessionId:AppServiceProvider.getInstance().userinfo.token,
        optDate:this.datePipe.transform(new Date(),"yyyy-MM-dd HH:mm:ss"),
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
    this.net.httpPost(AppGlobal.API.saveRegularCustodyByCode, this.param, 
      (resp) => {
        this.toast("提交成功!");
    }, (error) => {
      this.toast(error);
    }, true);
  }
}
