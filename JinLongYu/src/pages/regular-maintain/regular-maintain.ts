import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController } from 'ionic-angular';
import { BasePage } from '../base/base';
import { AppGlobal, AppServiceProvider } from '../../providers/app-service/app-service';

/**
 * Generated class for the RegularMaintainPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-regular-maintain',
  templateUrl: 'regular-maintain.html',
})
export class RegularMaintainPage extends BasePage{

  posts:string = "";

  constructor(public navCtrl: NavController, public navParams: NavParams,public toastCtrl: ToastController) {
    super(navCtrl,navParams,toastCtrl);
  }

  ionViewWillEnter(){
    this.posts = AppServiceProvider.getInstance().userinfo.posts;
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad RegularMaintainPage');
  }

  checkWareHouse(){
    if (AppGlobal.wareHouse && AppGlobal.wareHouse.warehouseCode){
        return true;
    }else {
      this.toast("仓库信息缺失，请先去扫码");
      return false;
    }
  }

  gotoOpenAir(){
    if(this.posts == '管理人员'){
      return;
    }
    if (this.checkWareHouse()){
      this.navCtrl.push("RmAirOnPage");
    }
  }

  gotoCloseAir(){
    if(this.posts == '管理人员'){
      return;
    }
    if (this.checkWareHouse()){
      this.navCtrl.push("RmAirOffPage");
    }
  }

  gotoShowAir(){
    if (this.checkWareHouse()){
      this.navCtrl.push("RmAirShowPage");
    }
  }

  gotoOpenCooling(){
    if(this.posts == '管理人员'){
      return;
    }
    if (this.checkWareHouse()){
      this.navCtrl.push("RmCoolingOnPage");
    }
  }

  gotoCloseCooling(){
    if(this.posts == '管理人员'){
      return;
    }
    if (this.checkWareHouse()){
      this.navCtrl.push("RmCoolingOffPage");
    }
  }

  gotoShowCooling(){
    if (this.checkWareHouse()){
      this.navCtrl.push("RmCoolingShowPage");
    }
  }
}
