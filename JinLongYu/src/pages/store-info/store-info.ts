import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController, AlertController, ModalController, Events } from 'ionic-angular';
import { BasePage } from '../base/base';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';
import { AppGlobal, AppServiceProvider } from '../../providers/app-service/app-service';
import { DeviceIntefaceServiceProvider } from '../../providers/device-inteface-service/device-inteface-service';

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
    public events:Events,
    public device: DeviceIntefaceServiceProvider,
    public toastCtrl: ToastController) {
    super(navCtrl,navParams,toastCtrl);
    
    events.subscribe('tabChanged',(data)=>{
      this.qrCode = data.qrCode;
      this.getWareHouseInfo();
    });
  }

  ionViewWillEnter(){
    this.picUrl = AppGlobal.picturePrefix;
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad StoreInfoPage');
    this.getDict();
  }

  ionViewWillUnload(){
    this.events.unsubscribe('tabChanged');
  }

  scan(){
    this.device.push("qrCodeScan","扫描仓库二维码",(qrcode)=>{
      this.qrCode = qrcode;
      this.getWareHouseInfo();
    },(err)=>{
      this.toast(err);
    });
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
        this.info.storekeeperPic = this.picUrl+this.info.storekeeperPic;
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
