import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController } from 'ionic-angular';
import { BasePage } from '../base/base';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';
import { AppServiceProvider, AppGlobal } from '../../providers/app-service/app-service';

/**
 * Generated class for the TongEquipmentRecordsPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-tong-equipment-records',
  templateUrl: 'tong-equipment-records.html',
})
export class TongEquipmentRecordsPage extends BasePage{
  param:any = {
    "loginName":"",
    "sessionId":"",
    "equipmentCode":"",
    "id":"",
    "remark":""
  }

  timeList: any = [ { name: '', options: [] } ];

  constructor(
    public navCtrl: NavController, 
    public navParams: NavParams,
    public net: TyNetworkServiceProvider,
    public toastCtrl: ToastController) {
      super(navCtrl,navParams,toastCtrl);
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad TongEquipmentRecordsPage');
    this.param['loginName'] = AppServiceProvider.getInstance().userinfo.loginName;
    this.param['sessionId'] = AppServiceProvider.getInstance().userinfo.token;
    this.getOperationRecord("");
  }

  records(){
    this.getShowTimeList();
  }

  getShowTimeList(){
    this.net.httpPost(AppGlobal.API.getEquipmentMaintenanceTimeList, 
      {
        loginName:AppServiceProvider.getInstance().userinfo.loginName,
        sessionId:AppServiceProvider.getInstance().userinfo.token,
        equipmentCode:this.param.equipmentCode
      }, 
      (resp) => {
        if (resp.content && resp.content != null){
            this.gotoSelect(resp.content);
        }else {
          this.toast("暂无历史记录");
        }
    }, (error) => {
      this.toast(error);
    }, true);
    
  }

  gotoSelect(records){
    this.navCtrl.push("SelectRecordPage",{callback:this.selectCallback,timeList:records});
  }

  selectCallback = (item)=>{
    this.getOperationRecord(item.id+"");
  }

  getOperationRecord(id){
    this.net.httpPost(AppGlobal.API.getEquipmentMaintenanceByCode, 
      {
        loginName:AppServiceProvider.getInstance().userinfo.loginName,
        sessionId:AppServiceProvider.getInstance().userinfo.token,
        equipmentCode:this.param.equipmentCode,
        id:id
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

}
