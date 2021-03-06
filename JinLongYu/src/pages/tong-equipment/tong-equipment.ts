import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController } from 'ionic-angular';
import { TydatePipe } from '../../pipes/tydate/tydate';
import { BasePage } from '../base/base';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';
import { AppGlobal, AppServiceProvider } from '../../providers/app-service/app-service';
import { DeviceIntefaceServiceProvider } from '../../providers/device-inteface-service/device-inteface-service';

/**
 * Generated class for the TongEquipmentPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  providers:[TydatePipe],
  selector: 'page-tong-equipment',
  templateUrl: 'tong-equipment.html',
})
export class TongEquipmentPage extends BasePage{

  param:any = {
    "loginName":"",
    "id":"",
    "sessionId":"",
    "equipmentCode":"",
    "equipmentName":"",
    "optDate":"",
    "maintenanceRecords":"",
    "type":"机械楼",
    "remark":""
  }
  equipmentCode:string = "M101";

  maintenanceRecords: any = [ { name: '', options: [] } ];

  constructor(
    public navCtrl: NavController, 
    public navParams: NavParams,
    public net: TyNetworkServiceProvider,
    private datePipe:TydatePipe,
    public device: DeviceIntefaceServiceProvider,
    public toastCtrl: ToastController) {
      super(navCtrl,navParams,toastCtrl);
      this.initPickers();
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad TongEquipmentPage');
    this.initParam();
  }

  scan(){
    this.device.push("qrCodeScan","扫描仓库二维码",(qrcode)=>{
      this.equipmentCode = qrcode;
      this.getGearInfo();
    },(err)=>{
      this.toast(err);
    });
  }

  initPickers(){
    if (!AppGlobal.dict){
      this.toast("配置信息加载失败，请退出重新进入加载");
      return;
    }
    this.initPicker(this.maintenanceRecords,AppGlobal.dict.sys_pfcsbwhjl);
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
    this.param['userName'] = AppServiceProvider.getInstance().userinfo.userName;
  }

  getGearInfo(){
    this.net.httpPost(AppGlobal.API.getEquipmentByCode, 
      {
        loginName:AppServiceProvider.getInstance().userinfo.loginName,
        sessionId:AppServiceProvider.getInstance().userinfo.token,
        equipmentCode:this.equipmentCode,
        type:"机械楼"
      }, 
      (resp) => {
        if (resp.content && resp.content != null){
          this.param = resp.content;
          this.param['loginName'] = AppServiceProvider.getInstance().userinfo.loginName;
          this.param['sessionId'] = AppServiceProvider.getInstance().userinfo.token;
          this.param['optDate'] = this.datePipe.transform(new Date(),"yyyy-MM-dd HH:mm:ss");
          this.param['userName'] = AppServiceProvider.getInstance().userinfo.userName;
        }
    }, (error) => {
      this.toast(error);
    }, true);
  }

  post(){
    this.net.httpPost(AppGlobal.API.saveEquipmentMaintenanceByCode, this.param, 
      (resp) => {
        this.toast("提交成功!");
    }, (error) => {
      this.toast(error);
    }, true);
  }

}
