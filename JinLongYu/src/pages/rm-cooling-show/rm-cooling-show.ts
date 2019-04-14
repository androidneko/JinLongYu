import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController } from 'ionic-angular';
import { BasePage } from '../base/base';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';
import { AppServiceProvider, AppGlobal } from '../../providers/app-service/app-service';

/**
 * Generated class for the RmCoolingShowPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-rm-cooling-show',
  templateUrl: 'rm-cooling-show.html',
})
export class RmCoolingShowPage extends BasePage{
  param:any = {
    "loginName":"",
    "sessionId":"",
    "id":"",
    "showRemark":""
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
    console.log('ionViewDidLoad RmCoolingShowPage');
    this.param['loginName'] = AppServiceProvider.getInstance().userinfo.loginName;
    this.param['sessionId'] = AppServiceProvider.getInstance().userinfo.token;
    this.getOperationRecord("");
  }

  records(){
    this.getShowTimeList();
  }

  getShowTimeList(){
    this.net.httpPost(AppGlobal.API.getGrainCoolingTimeList, 
      {
        loginName:AppServiceProvider.getInstance().userinfo.loginName,
        sessionId:AppServiceProvider.getInstance().userinfo.token,
        warehouseCode:AppGlobal.wareHouse.warehouseCode
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
    this.net.httpPost(AppGlobal.API.getGrainCoolingByCode, 
      {
        loginName:AppServiceProvider.getInstance().userinfo.loginName,
        sessionId:AppServiceProvider.getInstance().userinfo.token,
        warehouseCode:AppGlobal.wareHouse.warehouseCode,
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

  back(){
    this.navCtrl.pop();
    // this.net.httpPost(AppGlobal.API.saveGrainCoolingById, this.param, 
    //   (resp) => {
    //     this.toast("提交成功!");
    // }, (error) => {
    //   this.toast(error);
    // }, true);
  }
}
