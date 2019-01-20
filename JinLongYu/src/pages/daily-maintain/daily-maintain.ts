import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController } from 'ionic-angular';
import { BasePage } from '../base/base';
import { AppGlobal } from '../../providers/app-service/app-service';

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

  constructor(public navCtrl: NavController, public navParams: NavParams,public toastCtrl: ToastController) {
    super(navCtrl,navParams,toastCtrl);
  }


  ionViewDidLoad() {
    console.log('ionViewDidLoad DailyMaintainPage');
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
