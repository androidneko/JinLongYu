import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController, Events } from 'ionic-angular';
import { BasePage } from '../base/base';
import { AppGlobal, AppServiceProvider } from '../../providers/app-service/app-service';
import { DeviceIntefaceServiceProvider } from '../../providers/device-inteface-service/device-inteface-service';

/**
 * Generated class for the DailyMaintainPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-daily-maintain',
  templateUrl: 'daily-maintain.html',
})
export class DailyMaintainPage extends BasePage{

  posts:string = "";
  constructor(public navCtrl: NavController, public navParams: NavParams,public events:Events,public device: DeviceIntefaceServiceProvider,public toastCtrl: ToastController) {
    super(navCtrl,navParams,toastCtrl);
  }

  ionViewWillEnter(){
    this.posts = AppServiceProvider.getInstance().userinfo.posts;
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad DailyMaintainPage');
  }

  scan(){
    this.device.push("qrCodeScan","扫描仓库二维码",(qrcode)=>{
      this.events.publish('triggerTab', {index:1,qrCode:qrcode});
    },(err)=>{
      this.toast(err);
    });
  }

  checkWareHouse(){
    if (AppGlobal.wareHouse && AppGlobal.wareHouse.warehouseCode){
        return true;
    }else {
      this.toast("仓库信息缺失，请先去扫码");
      return false;
    }
  }

  gotoCustody(){
    if(this.posts == '管理人员'){
      return;
    }
    if (this.checkWareHouse()){
      this.navCtrl.push("RegularCustodyPage");
    }
  }

  gotoShowCustody(){
    if (this.checkWareHouse()){
      this.navCtrl.push("RegularCustodyShowPage");
    }
  }

  gotofumitaion(){
    if(this.posts == '管理人员'){
      return;
    }
    if (this.checkWareHouse()){
      this.navCtrl.push("FumigationPage");
    }
  }

  gotoFumitationRecord(){
    if (this.checkWareHouse()){
      this.navCtrl.push("FumitationRecordsPage");
    }
  }

  gotoCg(){
    if(this.posts == '管理人员'){
      return;
    }
    if (this.checkWareHouse()){
      this.navCtrl.push("ConcentrationGasPage");
    }
  }

  gotoCgRecord(){
    if (this.checkWareHouse()){
      this.navCtrl.push("ConcentrationGasRecordsPage");
    }
  }

  gotoPingGears(){
    if(this.posts == '管理人员'){
      return;
    }
    if (this.checkWareHouse()){
      this.navCtrl.push("PingEquipmentPage");
    }
  }

  gotoPingGearsRecord(){
    if (this.checkWareHouse()){
      this.navCtrl.push("PingEquipmentRecordsPage");
    }
  }

  gotoTongGears(){
    if(this.posts == '管理人员'){
      return;
    }
    if (this.checkWareHouse()){
      this.navCtrl.push("TongEquipmentPage");
    }
  }

  gotoTongGearsRecord(){
    if (this.checkWareHouse()){
      this.navCtrl.push("TongEquipmentRecordsPage");
    }
  }
}
