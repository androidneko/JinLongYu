import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController, AlertController, ModalController } from 'ionic-angular';
import { BasePage } from '../base/base';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';
import { AppGlobal, AppServiceProvider } from '../../providers/app-service/app-service';

/**
 * Generated class for the StoreInfoPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-store-info',
  templateUrl: 'store-info.html',
})
export class StoreInfoPage extends BasePage{

  qrCode:string = "A101";
  picUrl:string = "";

  info:any = {
    "searchValue": null,
    "storekeeperPic":"",
    "createBy": "",
    "createTime": null,
    "updateBy": "",
    "updateTime": null,
    "remark": "",
    "warehouseId": 0,
    "warehouseCode": "",
    "storekeeperName": "",
    "storekeeperId": 0,
    "storageCapacity": "",
    "netLength": "",
    "netWidth": "",
    "grainHeapHeight": "",
    "overgroundCageVolume": "",
    "grainGateVolume": "",
    "airBoxVolume": "",
    "overgroundCageVolumeA": "",
    "grainGateVolumeA": "",
    "airBoxVolumeA": "",
    "overgroundCageVolumeB": null,
    "grainGateVolumeB": "",
    "airBoxVolumeB": "",
    "varieties": "",
    "warehousingDate": "",
    "stock": "",
    "waterContent": "",
    "bulkDensity": "",
    "averageGrainTemperature": "",
    "fumigationState": "",
    "ventilationSystemImg": "",
    "measuringPointImg": "",
    "qcode": "A102.jpg",
    "storageType": "",
    "createDateTimeStr": "",
    "updateDateTimeStr": "",
    "createTimeStr": "",
    "updateTimeStr": ""
  };

  constructor(
    public navCtrl: NavController, 
    public navParams: NavParams,
    public alertCtrl:AlertController,
    public modalCtrl: ModalController,
    public net: TyNetworkServiceProvider,
    public toastCtrl: ToastController) {
    super(navCtrl,navParams,toastCtrl);
  }

  ionViewWillEnter(){
    this.picUrl = AppGlobal.picturePrefix;
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad StoreInfoPage');
    this.getDict();
    this.getWareHouseInfo();
  }

  getDict(){
    this.net.httpPost(AppGlobal.API.getDict, {loginName:AppServiceProvider.getInstance().userinfo.loginName}, (resp) => {
      AppGlobal.dict = resp.content;
    }, (error) => {
      this.toast(error);
    }, true);
  }

  getWareHouseInfo(){
    this.net.httpPost(AppGlobal.API.getWarehouseByCode, 
      {
        loginName:AppServiceProvider.getInstance().userinfo.loginName,
        sessionId:AppServiceProvider.getInstance().userinfo.token,
        warehouseCode:this.qrCode
      }, 
      (resp) => {
        this.info = resp.content;
        this.info.ventilationSystemImg = this.picUrl+this.info.ventilationSystemImg;
        this.info.measuringPointImg = this.picUrl+this.info.measuringPointImg;
        AppGlobal.wareHouse = this.info;
    }, (error) => {
      this.toast(error);
    }, true);
  }

  aletBigImg(imgBuf:String){
    const modal = this.modalCtrl.create("ShowPicPage",{picUrl:imgBuf});
    modal.present();
  }
}
