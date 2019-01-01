import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController, AlertController, LoadingController } from 'ionic-angular';
import { AppGlobal, AppServiceProvider } from '../../providers/app-service/app-service';
import { BasePage } from '../base/base';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';
import { DeviceIntefaceServiceProvider } from '../../providers/device-inteface-service/device-inteface-service';
import { DbServiceProvider } from '../../providers/db-service/db-service';
import { Md5 } from '../../../node_modules/ts-md5/dist/md5';

/**
 * Generated class for the ApplyPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-apply',
  templateUrl: 'apply.html',
})
export class ApplyPage extends BasePage{
  appletIds:any = ["fb979e4088f046568036c74129701570","612b0749e471443d9d635b641626f0eb"];
  acTypeId;
  ctTypeId;
  acAppId = "2dc404f1de914c52bc3710fa9cf19166";
  ctAppId = "8686d307742f46a39c66d51f80947a96";

  //目前的状态有三种：0-未开卡，1-已开卡，2-移机
  acState = "0"; 
  ctState = "0";

  data:any = {
    sdkId:"",
    personName:"",
    personNo:"",
    gender:"1",
    deptUuid:"0000",
    companyId:"1"
  }

  deviceData:any = {
    deviceId:"",
    brand:"",
    manufacturer:"",
    model:""
  }

  riskData:any = {
    clientType:"THIRD_PARTY",
    ipAddress:"",
    riskAlgorithmVersion:"1.0",
    deviceScore:1,
    accountScore:1,
    walletAccountTenureWeeks:10,
    gaiaAccountTenureWeeks:10,
    osVersion:"",
    networkType:"WIFI",
    deviceTimezone:"GMT-08"
  }

  phoneNum:string = AppServiceProvider.getInstance().userinfo.username;
  genders: any = [
    {
      name: '性别',
      options: [
        { text: '男', value: '1' },
        { text: '女', value: '2' }
      ]
    }
  ];

  // depts:any = [
  //   {
  //     name: '公司',
  //     options: [
  //       { text: '天喻信息', value: '1' },
  //       { text: '聚联网络', value: '2' },
  //       { text: '天喻教育', value: '3' },
  //       { text: '擎动网络', value: '4' },
  //       { text: '百旺金赋', value: '5' }
  //     ]
  //   },
  //   {
  //     name: '部门',
  //     options: [
  //       { text: '系统研发一部', value: 'A13', parentVal: '1' },
  //       { text: '系统研发二部', value: 'A21', parentVal: '1' },
  //       { text: '通信与支付服务事业部', value: 'A22', parentVal: '1' },
  //       { text: '运营部', value: 'B11' , parentVal: '2'},
  //       { text: '人力资源部', value: 'B21' , parentVal: '2'},
  //       { text: '研发部', value: 'B31' , parentVal: '3'},
  //       { text: '测试部', value: 'G21', parentVal: '3' },
  //       { text: '研发中心', value: 'G23' , parentVal: '4'},
  //       { text: '后台管理', value: 'H11' , parentVal: '4'},
  //       { text: '票务', value: 'H21' , parentVal: '5'},
  //       { text: '客服', value: 'L11' , parentVal: '5'}
  //     ]
  //   }
  // ];

  companies: any = [
    {
      name: '公司',
      options: [
        { text: '天喻信息', value: '1' },
        { text: '天喻教育', value: '2' },
        { text: '百旺', value: '3' }
      ]
    }
  ];

  depts:any = [
    {
      name: '部门',
      options: [
        { text: '默认部门', value: '0000' },
        { text: '系统研发一部', value: '9dacc31b7f6b4ee79df593d0cbe678e7' },
        { text: '系统研发二部', value: 'c58e459b9c32483cabde5f30d7f17552' },
        { text: '测试部', value: '341b30790a7046478c96fc34382b1482' },
        { text: '开发部', value: 'cba079ae4232486fac59a4d8239a9b55' }
      ]
    }
  ];

  constructor(
    public toastCtrl: ToastController, 
    public navCtrl: NavController, 
    public navParams: NavParams, 
    public alert:AlertController,
    public loadingCtrl: LoadingController,
    public net: TyNetworkServiceProvider,
    public db:DbServiceProvider, 
    public device: DeviceIntefaceServiceProvider) {
    super(navCtrl, navParams,device, toastCtrl);
    
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad ApplyPage');
    this.getCompaniesAndDepts()
    .then(()=>{ return this.loadCache(); })
    .then(()=>{ return this.loadDeviceInfo(); })
    .then(()=>{ return this.loadCardsState(); });
    //.then(()=>{ return this.getAppTypeList(); })
    // .then(()=>{ return this.getAppletInfo(0); })
    // .then(()=>{ return this.getAppletInfo(1); });
  }

  // getAppTypeList(){
  //   return new Promise((resolve,reject)=>{
  //     this.net.httpPost(AppGlobal.API.appType,{}, resp => {
  //       let info = resp.data;
  //       let list = info.appType;
  //       if (list && list.length > 0){
  //           this.appletIds = [];
  //           list.forEach(element => {
  //             if ("A" == element.appType){
  //               this.acTypeId = element.id;
  //             }
  //             if ("B"==element.appType){
  //               this.ctTypeId = element.id;
  //             }
  //           });
  //           if(this.acTypeId && this.ctTypeId){
  //             this.appletIds.push(this.acTypeId);
  //             this.appletIds.push(this.ctTypeId);
  //           }
  //       }
  //       resolve();
  //     }, error => {
  //       console.log("error = " + error);
  //       resolve();
  //     }, false);
  //   });
  // }

  // getAppletInfo(index){
  //   return new Promise((resolve,reject)=>{
  //     this.net.httpPost(AppGlobal.API.appInfo,{
  //       appTypeId:this.appletIds[index]
  //     }, resp => {
  //       let info = resp.data;
  //       let list = info.appList;
  //       if (list && list.length > 0){
  //           if (index == 0){
  //             this.acAppId = list[0].id;
  //             console.log("更新门禁应用aid成功");
  //           }
  //           if (index == 1){
  //             this.ctAppId = list[0].id;
  //             console.log("更新食堂应用aid成功");
  //           }
  //           if(this.acAppId && this.ctAppId){
  //             this.data.sdkId = this.acAppId + '|' + this.ctAppId;
  //           }
  //       }
  //       resolve();
  //     }, error => {
  //       console.log("error = " + error);
  //       resolve();
  //     }, false);
  //   });
  // }

  apply(){
    if (!this.checkIfInputOk()) {
      return;
    }
    this.loadDeviceInfo()
    .then(()=>{ 
      //this.applyCt();
      this.applyAll();
    })
  }

  applyAll(){
    let taskArr = [];
    if (this.acState != "1"){
      taskArr.push(this.applyNxpAc());
    }
    if (this.ctState != "1"){
      taskArr.push(this.applyNxpCt());
    }
    let loadingConfig = {
      spinner: 'ios',
      content: '加载中'
    };
    let loading = this.loadingCtrl.create(loadingConfig);
    loading.present();
    Promise.all(taskArr)
    .then(
      results=>{
      this.device.push("keep",{key:AppServiceProvider.getInstance().userinfo.username+"_"+AppGlobal.commInfo.deviceId + "_acState",value:this.acState});
      this.device.push("keep",{key:AppServiceProvider.getInstance().userinfo.username+"_"+AppGlobal.commInfo.deviceId + "_ctState",value:this.ctState});
      loading.dismiss();
      this.showApplyResults(results);
    });
  }

  applyNxpAcConfirm(){
    return new Promise((resolve,reject)=>{
      this.net.httpPost(AppGlobal.API.applyConfirm,{sdkId:this.acAppId,personNo:this.data.personNo}, resp => {
        resolve();
      }, error => {
        console.log("applyNxpAcConfirm error = " + error);
        reject();
      }, false);
    });
  }

  applyNxpCtConfirm(){
    return new Promise((resolve,reject)=>{
      this.net.httpPost(AppGlobal.API.applyConfirm,{sdkId:this.ctAppId,personNo:this.data.personNo}, resp => {
        resolve();
      }, error => {
        console.log("applyNxpCtConfirm error = " + error);
        reject();
      }, false);
    });
  }

  applyNxpAc(){
    return new Promise((resolve,reject)=>{
      this.data.sdkId = this.acAppId;
      this.data["deviceData"] = this.deviceData;
      this.data["riskData"] = this.riskData;

      this.net.httpPost(AppGlobal.API.applyNxp, this.data, resp => {
        let data = resp.data;
        if (data) {
          let transitBundle = data.transitBundle;
          this.device.push("keep",{key:this.acAppId,value:JSON.stringify(transitBundle)},()=>{
            //通知平台卡数据保存成功
            this.applyNxpAcConfirm().then(()=>{
              this.acState = "1";
              resolve();
            },()=>{
              this.acState = "0";
              resolve("卡片安装失败，请重新申请");
            });
          },()=>{
            this.acState = "0";
            resolve("卡片安装失败，请重新申请");
          })
        }else {
          this.acState = "0";
          resolve("卡片安装失败，请重新申请");
        }
      }, error => {
        console.log("applyNxpAc error = " + error);
        this.db.saveString(JSON.stringify(this.data),this.phoneNum+"_applyInfo");
        if (error.indexOf('禁用再重新申请') != -1){
          this.acState = "2";
          resolve();
        } else {
          this.acState = "0";
          resolve(error);
        }
      }, false);
    });    
  }

  applyNxpCt(){
    return new Promise((resolve,reject)=>{
      this.data.sdkId = this.ctAppId;
      this.data["deviceData"] = this.deviceData;
      this.data["riskData"] = this.riskData;
      this.net.httpPost(AppGlobal.API.applyNxp, this.data, resp => {
        let data = resp.data;
        if (data) {
          let transitBundle = data.transitBundle;
          this.device.push("keep",{key:this.ctAppId,value:JSON.stringify(transitBundle)},()=>{
            //通知平台卡数据保存成功
            this.applyNxpCtConfirm().then(()=>{
              this.ctState = "1";
              resolve();
            },()=>{
              this.ctState = "0";
              resolve("卡片安装失败，请重新申请");
            });
          },()=>{
            this.ctState = "0";
            resolve("卡片安装失败，请重新申请");
          })
        }else {
          this.ctState = "0";
          resolve("卡片安装失败，请重新申请");
        }
      }, error => {
        console.log("applyNxpCt error = " + error);
        this.db.saveString(JSON.stringify(this.data),this.phoneNum+"_applyInfo");
        if (error.indexOf('禁用再重新申请') != -1){
          this.ctState = "2";
          resolve();
        } else {
          this.ctState = "0";
          resolve(error);
        }
      }, false);
    });
  }

  applyAc() {
    //console.log("dept:"+this.deptInfo);
    if (!this.checkIfInputOk()) {
      return;
    }
    this.data.sdkId = this.acAppId;
    this.net.httpPost(AppGlobal.API.apply, this.data, resp => {
      let data = resp.data;
      if (data) {
        let acData = data[0];
        //let ctData = data[1];
        this.device.push("keep", { "key": "acfile_vcuid", "value": acData.cardNo });
        this.device.push("keep", { "key": "acfile_mf2go_ver", "value": acData.mversion });
        this.device.push("keep", { "key": "acfile_timestamp", "value": acData.timeStamp });
        this.device.push("keep", { "key": "acfile_rk", "value": acData.derivedKey });
        this.device.push("keep", { "key": "acfile_keyNo","value":acData.keyNo});
        this.device.push("keep", { "key": "acfile_status", "value": 1 });
        this.db.saveString("false", AppServiceProvider.getInstance().userinfo.username+"_"+AppGlobal.commInfo.deviceId + "_acaState");

        //AppServiceProvider.getInstance().userinfo.realName = this.data.personName;
        //AppServiceProvider.getInstance().userinfo.companyNo = this.data.personNo;
        AppServiceProvider.getInstance().userinfo.company = this.data.companyId;
        //用户信息整体保存，整体更新，整体读取
        //用户信息更新保存场景:登录、申请成功，修改用户头像（首页和我的用户信息页面）
        this.db.saveString(JSON.stringify(AppServiceProvider.getInstance().userinfo),AppServiceProvider.getInstance().userinfo.username+"_userinfo");
      }
      this.device.push("refreshCards","",success=>{
        if (this.navParams.data.callback) {
          this.navParams.data.callback("false");
        }
      },err=>{
        console.log("申请失败");
      },true);
      
      this.navCtrl.pop();
    }, error => {
      console.log("getVerifyCode error = " + error);
      if (error.indexOf('禁用再重新申请') != -1){
        this.showTipDialog();
      }
      this.db.saveString(JSON.stringify(this.data),this.phoneNum+"_applyInfo");
      this.showErrAlert(error);
    }, true);
  }

  applyCt() {
    //console.log("dept:"+this.deptInfo);
    if (!this.checkIfInputOk()) {
      return;
    }
    this.data.sdkId = this.ctAppId;
    this.net.httpPost(AppGlobal.API.apply, this.data, resp => {
      let data = resp.data;
      if (data) {
        let ctData = data[0];
        //let ctData = data[1];
        this.device.push("keep", { "key": "acfile_vcuid", "value": ctData.cardNo });
        this.device.push("keep", { "key": "acfile_mf2go_ver", "value": ctData.mversion });
        // this.device.push("keep", { "key": "acfile_timestamp", "value": acData.timeStamp });
        // this.device.push("keep", { "key": "acfile_rk", "value": acData.derivedKey });
        this.device.push("keep", { "key": "acfile_ct_timestamp", "value": ctData.timeStamp });
        this.device.push("keep", { "key": "acfile_rk_ct", "value": ctData.derivedKey });
        this.device.push("keep", { "key": "acfile_keyNo","value":ctData.keyNo});
        this.device.push("keep", { "key": "acfile_status", "value": 1 });
        this.db.saveString("false", AppServiceProvider.getInstance().userinfo.username+"_"+AppGlobal.commInfo.deviceId + "_acaState");

        //AppServiceProvider.getInstance().userinfo.realName = this.data.personName;
        //AppServiceProvider.getInstance().userinfo.companyNo = this.data.personNo;
        AppServiceProvider.getInstance().userinfo.company = this.data.companyId;
        //用户信息整体保存，整体更新，整体读取
        //用户信息更新保存场景:登录、申请成功，修改用户头像（首页和我的用户信息页面）
        this.db.saveString(JSON.stringify(AppServiceProvider.getInstance().userinfo),AppServiceProvider.getInstance().userinfo.username+"_userinfo");
      }
      this.device.push("refreshCards","",success=>{
        if (this.navParams.data.callback) {
          this.navParams.data.callback("false");
        }
      },err=>{
        console.log("申请失败");
      },true);
      
      this.navCtrl.pop();
    }, error => {
      console.log("getVerifyCode error = " + error);
      if (error.indexOf('禁用再重新申请') != -1){
        this.showTipDialog();
      }
      this.db.saveString(JSON.stringify(this.data),this.phoneNum+"_applyInfo");
      this.showErrAlert(error);
    }, true);
  }

  showTipDialog(){
    let alert = this.alert.create({
      title: '您已申请过该应用的虚拟卡，是否需要禁用再重新申请?',
      buttons: [{
        text: '取消',
        handler: () => {}
      }, {
        text: '好的',
        handler: () => {
          this.data['disableFlag'] = "1";
          this.applyAll();
        }
      }]
    });

    alert.present();
  }

  checkIfInputOk() {
    if (!this.data.personName) {
      //this.toast("请输入真实姓名");
      this.toast("请联系管理员录入您的个人信息");
      return false;
    }
    if(!this.data.personNo)
    {
      //this.toast("请输入员工工号");
      this.toast("请联系管理员录入您的个人信息");
      return false;
    }
    return true;
  }

  showErrAlert(err:string){
    let alert = this.alert.create({
      title:err,
      buttons:[{
        text:'好的'
      }],
      enableBackdropDismiss:false
    });

    alert.present();
  }

  getCompaniesAndDepts(){
    return new Promise((resolve,reject)=>{
      this.net.httpPost(AppGlobal.API.companyAndDept,{}, resp => {
        let info = resp.data;
        if (info.companyList && info.companyList.length > 0){
          this.companies[0].options = [];
          info.companyList.forEach(element => {
            this.companies[0].options.push({text:element.companyName,value:element.id});
          });
        }
  
        if (info.deptList && info.deptList.length > 0){
          this.depts[0].options = [];
          info.deptList.forEach(element => {
            this.depts[0].options.push({text:element.deptName,value:element.deptUuid});
          });
        }

        resolve();
      }, error => {
        console.log("error = " + error);
        resolve();
      }, true);
    });
  }

  loadCache(){
    return new Promise((resolve,reject)=>{
      this.db.getString(this.phoneNum+"_applyInfo",info=>{
        if (info){
          this.data = JSON.parse(info);
        }else{
          if(AppServiceProvider.getInstance().userinfo.gender){
            this.data.gender = AppServiceProvider.getInstance().userinfo.gender;
          }
          if (AppServiceProvider.getInstance().userinfo.realName){
            this.data.personName = AppServiceProvider.getInstance().userinfo.realName
          }
          if (AppServiceProvider.getInstance().userinfo.companyNo){
            this.data.personNo = AppServiceProvider.getInstance().userinfo.companyNo;
          }
          if (AppServiceProvider.getInstance().userinfo.companyId){
            this.data.companyId = AppServiceProvider.getInstance().userinfo.companyId
          }
          if (AppServiceProvider.getInstance().userinfo.deptUuid){
            this.data.deptUuid = AppServiceProvider.getInstance().userinfo.deptUuid;
          }
        }
        resolve();
      },err=>{resolve();});
    });
  }

  loadDeviceInfo(){
    return new Promise((resolve,reject)=>{
      this.device.push("commInfo","",info=>{
        if (info) {
          console.log("commInfo-->" + JSON.stringify(info));
          this.deviceData.deviceId = info.deviceId;
          this.deviceData.model = info.model;
          this.deviceData.brand = info.brand;
          this.deviceData.manufacturer = info.manufacturer;
          this.riskData.osVersion = info.osVersion;
          this.riskData.networkType = info.networkType;
          this.riskData.ipAddress = info.ipAddress;
        }
        resolve();
      },err=>{resolve();});
    });
  }

  loadCardsState(){
    return new Promise((resolve,reject)=>{
      this.device.push("loadCardsState","",info=>{
        if (info) {
          console.log("loadCardsState-->" + JSON.stringify(info));
          this.acState = info.acState;
          this.ctState = info.ctState;
        }
        resolve();
      },err=>{
        console.log("loadCardsState-->" + err);
      });
    });
  }

  showApplyResults(results){
    console.log("所有任务已完成，进入结果分析阶段");
    console.log(results);
    let err = "";
    if (results){
      results.forEach(element => {
        if (element){
          err = element;
        }
      });
    }
    if (this.acState == "" || this.acState == "0"){
      this.showErrAlert(err);
      return;
    }
    if (this.ctState == "" || this.ctState == "0"){
      this.showErrAlert(err);
      return;
    }

    if (this.acState == "2" || this.ctState == "2"){
      this.showTipDialog();
      return;
    }
    console.log("所有任务成功。");
    this.succeeded();
  }

  succeeded(){
    this.device.push("refreshCards","",success=>{
      this.db.saveString("false", AppServiceProvider.getInstance().userinfo.username+"_"+AppGlobal.commInfo.deviceId + "_acaState");
      this.navCtrl.pop();
      if (this.navParams.data.callback) {
        this.navParams.data.callback("false");
      }
    });
  }
}
