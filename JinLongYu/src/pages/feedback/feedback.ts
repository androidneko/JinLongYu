import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController, ActionSheetController, LoadingController, AlertController } from 'ionic-angular';
import { BasePage } from './../base/base';
import { AppGlobal, AppServiceProvider } from '../../providers/app-service/app-service';
import { DeviceIntefaceServiceProvider } from '../../providers/device-inteface-service/device-inteface-service';
import { Camera, CameraOptions } from '../../../node_modules/@ionic-native/camera';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';

/**
 * Generated class for the FeedbackPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-feedback',
  templateUrl: 'feedback.html',
})
export class FeedbackPage extends BasePage {
  //title:string="";
  textArea: string = "";
  phoneNumber: string = "";

  //dataArr =[];    //解决不能转file的问题
  imageArr = [];
  maxLength = 1 * 1024 * 1024;
  img1: string = "assets/imgs/add_pic.png";
  img2: string = "assets/imgs/add_pic.png";
  img3: string = "assets/imgs/add_pic.png";
  img4: string = "assets/imgs/add_pic.png";
  imgIndex: number = 0;
  imgCount: number = 0;
  deleteFlag = [false, false, false, false];

  constructor(
    public net: TyNetworkServiceProvider,
    public device: DeviceIntefaceServiceProvider,
    public toastCtrl: ToastController,
    public navCtrl: NavController,
    public loadingCtrl: LoadingController,
    public alertCtrl: AlertController,
    public actionSheet: ActionSheetController,
    public camera: Camera,
    public navParams: NavParams) {
    super(navCtrl, navParams, device, toastCtrl);
    for (var i = 0; i < 4; i++) {
      //this.dataArr.push("assets/imgs/add_pic.png");
      this.imageArr.push("assets/imgs/add_pic.png");
    }
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad FeedbackPage');
    //this.length = this.textArea.length;
  }

  ionViewWillEnter(){
    if ((/^1[23456789]\d{9}$/.test(AppServiceProvider.getInstance().userinfo.username.toString())))  
      this.phoneNumber = AppServiceProvider.getInstance().userinfo.username;
  }

  deleteClick(i) {
    //this.dataArr[i] = "assets/imgs/add_pic.png";
    this.imageArr[i] = "assets/imgs/add_pic.png";
    this.updataImageCount();
  }

  updataImageCount() {
    var count = 0;

    //部分手机的forEach是异步的
    for (var i = 0; i < this.imageArr.length; i++) {
      let item = this.imageArr[i];
      if (item.substring(0, 6) != 'assets') {
        count++;
      }
    }
    this.imgCount = count;
  }

  done() {
    console.log('submit clicked');
    if (this.isInfoLegal()) {
      this.sendFeedback();
    }
  }

  addClick(index) {
    this.imgIndex = index;
    let item = this.imageArr[index];
    if (item.substring(0, 6) == 'assets') {
      this.openCamera();
    }
  }

  hasPhoto(index){
    let item = this.imageArr[index];
    return "assets/imgs/add_pic.png" != item;
  }

  showLoading(str) {
    let loading = this.loadingCtrl.create({
      spinner: 'hide',
      content: str
    });
    loading.present();

    setTimeout(() => {
      loading.dismiss();
    }, 1000);
  }

  checkPhoneNum() {
    if (this.phoneNumber.toString().length == 0) {
      this.toast("请输入手机号");
      return false;
    }
    if (this.phoneNumber.toString().length != 11) {
      this.toast("手机号码长度不对");
      return false;
    }
    if (!(/^1[23456789]\d{9}$/.test(this.phoneNumber.toString()))) {
      this.toast("请输入正确的手机号");
      return false;
    }
    return true;
  }

  isInfoLegal() {
    if (!this.checkPhoneNum()) {
      return;
    }

    if (this.textArea.length < 10) {
      this.toast('请输入不少于10个字的描述');
      return false;
    }

    let ret = AppServiceProvider.getInstance().isEmojiCharacter(this.textArea.toString())
    if(ret){
      this.toast("意见反馈中不能含有表情符号");
      return false;
    }

    return true;
  }

  getImgWithIndex(options) {
    console.log('in');
    this.camera.getPicture(options).then((imageData) => {
      console.log('success');
      if (this.isLargerThanMaxStreamFile(imageData)) {
        return;
      }
      //this.dataArr[this.imgIndex] = imgStr;
      this.imageArr[this.imgIndex] = 'data:image/jpeg;base64,' + imageData;
      this.updataImageCount();
    }, (err) => {
      console.log('error');
      //this.toast(err);
    });
  }

  openCamera() {
    console.log('open click 1');
    let options: CameraOptions = {
      quality: 20,
      destinationType: this.camera.DestinationType.DATA_URL,
      encodingType: this.camera.EncodingType.JPEG,
      mediaType: this.camera.MediaType.PICTURE,
      sourceType: this.camera.PictureSourceType.PHOTOLIBRARY,
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
      handler: () => { }
    }];

    let actionSheet = this.actionSheet.create({
      buttons: buttons
    });
    actionSheet.present();
  }

  sendFeedback() {
    this.uploadImage((str)=>{
      let param = {
        logPutFlag: "1",
        content: this.textArea,
        phone: this.phoneNumber,
        appVersion:AppGlobal.commInfo.appVersion,
        phoneModel:AppGlobal.commInfo.model
      };
      if (str){
        param['picturesUrl'] = str; 
      }
      this.net.httpPost(AppGlobal.API.feedback,param , (msg) => {
        //网络请求成功
        //console.log(msg);
        this.navCtrl.pop();
        this.toast("提交成功");
      }, (error) => {
        //提示问题
        this.toast(error);
      }, true);
    });
  }

  uploadImage(success) {
    var imgArr = [];
    for (var index = 0; index < this.imageArr.length; index++) {
      let item = this.imageArr[index];
      let header = item.substring(0, 10);
      if (header == 'data:image') {
        imgArr.push(item);
      }
    }

    var nameArr = [];
    if (imgArr.length > 0) {
      let loading = this.loadingCtrl.create();
      loading.present();

      this.net.uploadFile(imgArr, (item, response) => {
        console.log(response);
        nameArr.push(response.imgUrl);
      }, (error) => {
        console.log(error);
        loading.dismiss();
        if (error == '会话过期，请重新登录!') {
          this.toast(error);
        } else {
          this.toast('图片上传失败');
        }

      }, (progress) => {
        console.log('progress:' + progress);
        loading.data.content = progress + "%";
      }, () => {
        //complete
        loading.dismiss();
        if (success) {
          var nameStr;
          if (nameArr.length > 0) {
            nameStr = "";
            // nameArr.forEach((element,index) => {
            //   nameStr = nameStr + element +"|";
            // });
            for (var index = 0; index < nameArr.length; index++) {
              let element = nameArr[index];
              nameStr = nameStr + element + "|";
            }
            nameStr = nameStr.substring(0, nameStr.length - 1);
          }
          success(nameStr);
        }
      });
    } else {
      if (success) {
        success(null);
      }
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
