import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController, AlertController, Events, ActionSheetController, ModalController } from 'ionic-angular';
import { BasePage } from '../base/base';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';
import { DbServiceProvider } from '../../providers/db-service/db-service';
import { DeviceIntefaceServiceProvider } from '../../providers/device-inteface-service/device-inteface-service';
import { AppServiceProvider, AppGlobal } from '../../providers/app-service/app-service';
import { Camera, CameraOptions } from '../../../node_modules/@ionic-native/camera';

/**
 * Generated class for the RootPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-root',
  templateUrl: 'root.html',
})
export class RootPage extends BasePage {
  maxLength = 1 * 1024 * 1024;
  avatar: string = AppServiceProvider.getInstance().userinfo.avatar;
  info: any = {
    company: "天喻信息",
    name: "- - - -",
    num: "- - - -"
  }
  isFirstLoginSkipped = "false";
  notApplied = true;

  pasFlag = "";

  constructor(
    public actionSheet: ActionSheetController,
    public camera: Camera,
    public toastCtrl: ToastController,
    public modalCtrl: ModalController,
    public events: Events,
    public net: TyNetworkServiceProvider,
    public db: DbServiceProvider,
    public device: DeviceIntefaceServiceProvider,
    public alert: AlertController,
    public navCtrl: NavController,
    public navParams: NavParams) {
    super(navCtrl, navParams, device, toastCtrl);
    events.subscribe('logoutNotification', () => {
      this.logout()
        .then(() => {
          AppServiceProvider.getInstance().userinfo.token = "";
          AppServiceProvider.getInstance().userinfo.realName = "";
          AppServiceProvider.getInstance().userinfo.companyNo = "";
          AppServiceProvider.getInstance().userinfo.pasFlag = "";
          AppServiceProvider.getInstance().userinfo.avatar = "";
          //用户信息整体保存，整体更新，整体读取
          //用户信息更新保存场景:登录、申请成功，修改用户头像（首页和我的用户信息页面）
          this.db.saveString(JSON.stringify(AppServiceProvider.getInstance().userinfo),
            AppServiceProvider.getInstance().userinfo.username+"_userinfo");
          this.db.saveString("", AppServiceProvider.getInstance().userinfo.username + "_token", () => {
            this.navCtrl.setRoot("LoginPage");
          });
        });
    });

  }

  ionViewWillEnter(){
    if (AppServiceProvider.getInstance().userinfo.realName)
      this.info.name = AppServiceProvider.getInstance().userinfo.realName;
    if (AppServiceProvider.getInstance().userinfo.companyNo)
      this.info.num = AppServiceProvider.getInstance().userinfo.companyNo;
      
    this.avatar = AppServiceProvider.getInstance().userinfo.avatar;
    this.pasFlag = AppServiceProvider.getInstance().userinfo.pasFlag;
    this.checkIfIsFirstLogin();
    this.checkIfInnerAppletApplied();
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad RootPage');
    this.bindJpush();
  }

  ionViewWillUnload() {
    //避免该页面多次进入后多次监听登出事件，切换登录可能发生该状况
    this.events.unsubscribe('logoutNotification');
  }

  checkIfIsFirstLogin() {
    this.db.getString(AppServiceProvider.getInstance().userinfo.username+"_"+"firstLoginSkipped", ret => {
      this.isFirstLoginSkipped = ret;
      console.log('checkIfIsFirstLogin...isFirstLoginSkipped-->'+this.isFirstLoginSkipped+"----this.pasFlag-->"+this.pasFlag);
      if (this.isFirstLoginSkipped !== "true" && this.pasFlag === "1") {
        this.showSetPwdTip();
      }
    });
  }

  checkIfInnerAppletApplied() {
    return new Promise((resolve, reject) => {
      this.db.getString(AppServiceProvider.getInstance().userinfo.username+"_"+AppGlobal.commInfo.deviceId + "_acaState", ret => {
        console.log('acaState-->'+ret);
        if (ret == "false"){
          this.notApplied = false;
        }
      });
    });
  }

  showSetPwdTip() {
    let alert = this.alert.create({
      title: '是否去设置登录密码？',
      buttons: [{
        text: '取消',
        handler: () => {
          this.db.saveString("true", AppServiceProvider.getInstance().userinfo.username+"_"+"firstLoginSkipped");
        }
      }, {
        text: '好的',
        handler: () => {
          this.db.saveString("true", AppServiceProvider.getInstance().userinfo.username+"_"+"firstLoginSkipped");
          this.navCtrl.push("SetPwdPage");
        }
      }],
      enableBackdropDismiss: false
    });

    alert.present();
  }

  gotoUserCenter() {
    this.navCtrl.push("UserCenterPage");
  }

  changeAvatar() {
    this.openCamera();
  }

  openCamera() {
    console.log('open click 1');
    let options: CameraOptions = {
      quality: 20,
      destinationType: this.camera.DestinationType.DATA_URL,
      encodingType: this.camera.EncodingType.JPEG,
      mediaType: this.camera.MediaType.PICTURE,
      sourceType: this.camera.PictureSourceType.PHOTOLIBRARY,
      correctOrientation:true,
      targetWidth:720,
      targetHeight:720,
      allowEdit: true
    };
    var buttons = [{
      text: "拍照",
      handler: () => {
        options.sourceType = this.camera.PictureSourceType.CAMERA;
        this.getImgWithIndex(options);
      }
    }, {
      text: "从相册中选择",
      handler: () => {
        console.log('photolibary');
        options.sourceType = this.camera.PictureSourceType.PHOTOLIBRARY;
        this.getImgWithIndex(options);
      }
    }, {
      text: "取消",
      role: 'cancel',
      handler: () => {
      }
    }];

    let actionSheet = this.actionSheet.create({
      buttons: buttons
    });
    actionSheet.present();
  }

  getImgWithIndex(options) {
    console.log('in');
    this.camera.getPicture(options).then((imageData) => {
      if (this.isLargerThanMaxStreamFile(imageData)) {
        return;
      }
      console.log('success');
      let base64Image = 'data:image/jpeg;base64,' + imageData;
      this.uploadHeaderImage(base64Image);
    }, (err) => {
      console.log('error');
      this.toast(err);
    });
  }

  //上传头像
  uploadHeaderImage(imageStr) {
    this.net.httpPost(AppGlobal.API.base64PicUpload, { fileContent: imageStr, extension: "jpeg" }, (resp) => {
      //let obj = JSON.parse(result);
      if (resp.returnCode == AppGlobal.returnCode.succeed) {
        let imgUrl = resp.data.imgUrl;
        this.modifyUserInfo(imgUrl);
      } else {
        this.toast(resp.returnDes);
      }
    }, (error) => {
      this.toast(error);
    }, true);
  }

  modifyUserInfo(imgUrl) {
    this.net.httpPost(AppGlobal.API.modifyUserInfo, {
      operType: "2",
      photoImgUrl: imgUrl
    }, (resp) => {
      this.avatar = AppGlobal.picturePrefix + imgUrl;
      AppServiceProvider.getInstance().userinfo.avatar = AppGlobal.picturePrefix + imgUrl;
      //用户信息更新保存场景:登录、申请成功，修改用户头像（首页和我的用户信息页面）
      this.db.saveString(JSON.stringify(AppServiceProvider.getInstance().userinfo), AppServiceProvider.getInstance().userinfo.username + "_userinfo");
    }, (error) => {
      this.toast(error);
    }, true);
  }

  logout() {
    return new Promise((resolve, reject) => {
      this.net.httpPost(AppGlobal.API.logout, {}, msg => {
        resolve();
      }, error => {
        resolve();
      }, true);
    });
  }

  apply() {
    this.navCtrl.push("ApplyPage", { callback: this.applyCallback });
  }

  //选择条件回调
  applyCallback = (data) => {
    this.getUserInfo();
    if (data === "false") {
      this.notApplied =false;
      this.showSuccess();
    }
    //this.checkIfInnerAppletApplied();
  }

  gotoRecords() {
    this.navCtrl.push("TyacRecordsPage");
  }

  showSuccess() {
    const modal = this.modalCtrl.create("ApplySuccessAlertPage");
    //const modal = this.modalCtrl.create("UserProtocolPage");
    modal.present();
  }

  isLargerThanMaxStreamFile(base64) {
    let length = this.computeStreamFileLength(base64);
    if (length > this.maxLength) {
      //提示图片过大
      this.showAlert('上传文件过大，请控制上传图片在1MB以内，谢谢')
      return true;
    } else {
      return false;
    }
  }

  showAlert(msg) {
    let alertCtrl = this.alert.create({
      title: "提示",
      subTitle: msg,
      buttons: ['知道了']
    });
    alertCtrl.present();
  }

  computeStreamFileLength(base64Str) {
    var headerIndex = base64Str.indexOf('base64,');
    var str = base64Str.substring(headerIndex + 7);
    var equalIndex = str.indexOf('=');
    if (equalIndex > 0) {
      str = str.substring(0, equalIndex);
    }

    let num = str.length - (str.length / 8) * 2;

    var fileLength = parseInt('0' + num);
    return fileLength;
  }

  getUserInfo() {
    this.net.httpPost(AppGlobal.API.userInfo, {}, resp => {
      let userInfo:any = resp.data;
      userInfo.profilePhoto = AppGlobal.picturePrefix + userInfo.profilePhoto;
      this.updateUserInfo(userInfo);
    },
      error => {
        this.toast(error);
      }, false);
  }

  updateUserInfo(info){
    AppServiceProvider.getInstance().userinfo.realName = info.personName;
    AppServiceProvider.getInstance().userinfo.companyNo = info.personNo;
    AppServiceProvider.getInstance().userinfo.avatar = info.profilePhoto;
    AppServiceProvider.getInstance().userinfo.companyId = info.companyId;
    AppServiceProvider.getInstance().userinfo.deptUuid = info.deptUuid;
    AppServiceProvider.getInstance().userinfo.gender = info.gender;
    //用户信息整体保存，整体更新，整体读取
    //用户信息更新保存场景:登录、申请成功，修改用户头像（首页和我的用户信息页面）
    this.db.saveString(JSON.stringify(AppServiceProvider.getInstance().userinfo),AppServiceProvider.getInstance().userinfo.username+"_userinfo");
    this.avatar = AppServiceProvider.getInstance().userinfo.avatar;
    if (AppServiceProvider.getInstance().userinfo.realName)
      this.info.name = AppServiceProvider.getInstance().userinfo.realName;
    if (AppServiceProvider.getInstance().userinfo.companyNo)
      this.info.num = AppServiceProvider.getInstance().userinfo.companyNo;
  }

  bindJpush(){
    this.device.push("jpushId","",jpushId=>{
      if (jpushId){
        this.net.httpPost(AppGlobal.API.bindJpush, {
          type:"1",
          registrationId:jpushId,
          tags:"android",
          alias:AppServiceProvider.getInstance().userinfo.username
        },()=>{
          console.log("bindJpush-->success");
        },err=>{},false);
      }
    });
  }
}
