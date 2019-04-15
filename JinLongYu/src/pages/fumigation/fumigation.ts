import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController } from 'ionic-angular';
import { BasePage } from '../base/base';
import { TydatePipe } from '../../pipes/tydate/tydate';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';
import { AppGlobal, AppServiceProvider } from '../../providers/app-service/app-service';

/**
 * Generated class for the FumigationPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  providers:[TydatePipe],
  selector: 'page-fumigation',
  templateUrl: 'fumigation.html',
})
export class FumigationPage extends BasePage{

  param:any = {
    "loginName":"",
    "id":"",
    "sessionId":"",
    "warehouseCode":"",
    "administrationTime":"",
    "closedDays":"",
    "fumigant":"",
    "dosage":"",
    "inventory":"",
    "participants":""
  }

  fumigants: any = [ { name: '', options: [] } ];
  closedDays: any = [ { name: '', options: [] } ];

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
    this.initPicker(this.fumigants,AppGlobal.dict.sys_xzjmc);
    this.initPicker(this.closedDays,AppGlobal.dict.sys_mbts);
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
    this.param['administrationTime'] = this.datePipe.transform(new Date(),"yyyy-MM-dd HH:mm:ss");
    this.param['warehouseCode'] = AppGlobal.wareHouse.warehouseCode;
    this.param['userName'] = AppServiceProvider.getInstance().userinfo.userName;
  }

  getOperationRecord(){
    this.net.httpPost(AppGlobal.API.getFumigationOperationByCode, 
      {
        loginName:AppServiceProvider.getInstance().userinfo.loginName,
        sessionId:AppServiceProvider.getInstance().userinfo.token,
        warehouseCode:AppGlobal.wareHouse.warehouseCode
      }, 
      (resp) => {
        if (resp.content && resp.content != null && resp.content.id != null){
          this.param = resp.content;
          this.param['loginName'] = AppServiceProvider.getInstance().userinfo.loginName;
          this.param['sessionId'] = AppServiceProvider.getInstance().userinfo.token;
        }
    }, (error) => {
      this.toast(error);
    }, true);
  }

  post(){
    this.net.httpPost(AppGlobal.API.saveFumigationOperationByCode, this.param, 
      (resp) => {
        this.toast("提交成功!");
    }, (error) => {
      this.toast(error);
    }, true);
  }
}
