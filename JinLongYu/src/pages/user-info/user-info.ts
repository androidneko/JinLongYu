import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController, ActionSheetController, AlertController } from 'ionic-angular';
import { BasePage } from '../base/base';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';
import { DeviceIntefaceServiceProvider } from '../../providers/device-inteface-service/device-inteface-service';
import { AppGlobal, AppServiceProvider } from '../../providers/app-service/app-service';
import { Camera, CameraOptions } from '../../../node_modules/@ionic-native/camera';
import { DbServiceProvider } from '../../providers/db-service/db-service';

/**
 * Generated class for the UserInfoPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-user-info',
  templateUrl: 'user-info.html',
})
export class UserInfoPage extends BasePage {

  gender: string = "";
  avatar:string = "";
  maxLength = 1 * 1024 * 1024;
  userInfo: any = {
    profilePhoto: "",
    userId: "",
    gender: "",
    personName: "",
    deptName: "",
    personNo: "",
    companyName:""
  };

  constructor(
    public actionSheet: ActionSheetController,
    public camera: Camera,
    public toastCtrl: ToastController,
    public navCtrl: NavController,
    public alertCtrl: AlertController,
    public navParams: NavParams,
    public db:DbServiceProvider,
    public net: TyNetworkServiceProvider,
    public device: DeviceIntefaceServiceProvider) {
    super(navCtrl, navParams, device, toastCtrl);
  }

  ionViewWillEnter(){
    this.avatar = AppServiceProvider.getInstance().userinfo.avatar;
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad UserInfoPage');
    this.getUserInfo();
  }

  getUserInfo() {
    this.net.httpPost(AppGlobal.API.userInfo, {}, resp => {
      this.userInfo = resp.data;
      this.userInfo.profilePhoto = AppGlobal.picturePrefix + this.userInfo.profilePhoto;
      this.translateGender(this.userInfo.gender);
      this.updateUserInfo(this.userInfo);
    },
      error => {
        this.toast(error);
      }, true);
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
    this.net.httpPost(AppGlobal.API.base64PicUpload, { fileContent: imageStr}, (resp) => {
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
      AppServiceProvider.getInstance().userinfo.avatar = AppGlobal.picturePrefix + imgUrl;
      //用户信息更新保存场景:登录、申请成功，修改用户头像（首页和我的用户信息页面）
      this.db.saveString(JSON.stringify(AppServiceProvider.getInstance().userinfo),AppServiceProvider.getInstance().userinfo.username+"_userinfo");
      this.avatar = AppServiceProvider.getInstance().userinfo.avatar;
      this.toast("修改成功");
    }, (error) => {
      this.toast(error);
    }, true);
  }

  translateGender(str) {
    if (str == '1') {
      this.gender = '男';
    } else if (str == '2') {
      this.gender = '女';
    } else {
      this.gender = '保密';
    }
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
    let alertCtrl = this.alertCtrl.create({
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
}
