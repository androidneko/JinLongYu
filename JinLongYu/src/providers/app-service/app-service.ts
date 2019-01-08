import { Injectable } from '@angular/core';
import 'rxjs/add/operator/map';

/*
  Generated class for the AppServiceProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/

//全局信息
@Injectable()
export class AppGlobal {

    static cache: any = {
        slides: "_dress_slides",
        categories: "_dress_categories",
        products: "_dress_products"
    }

    static Config = {
        versionName: "1.0",
        versionCode: 0,
        comm: {
          debug: true,
          env: "prod",
          keyVer: "2",
          appId: "8a88a80f65b2e4b80165b2e7633b0000",
          appKey:"",
          mode:0,
          prod: {
            host: "https://iot.whty.com.cn",
            traceUrl: "http://10.8.15.33:8888/pre/collectMsg"
          },
          test: {
            host: "http://yfzx.whty.com.cn",
            traceUrl: "http://10.8.15.33:8888/pre/collectMsg"
          },
          dev: {
            host: "http://yfzx.whty.com.cn:3008",
            traceUrl: "http://10.8.15.33:8888/pre/collectMsg"
          },
          https:{
            host:"https://iot.whty.com.cn",
            traceUrl:"http://10.8.15.33:8888/pre/collectMsg"
          }
        },
        android: {
          os: "01",
        },
        ios: {
          os: "01",
          mode: "1"
        }
      };

    static commInfo = {
      hasHce:true,
      isRooted:false,
      appVersion:"",
      model:"",
      manufacturer:"",
      brand:"",
      osVersion:"",
      networkType:"",
      ipAddress:"",
      deviceId:"db5210fa-5afd-36d6-a586-353d0c48b6f0",
      appId:"8a88a80f65b2e4b80165b2e7633b0000",
      sign:""
    }

    static host = "";
    static interfacePrefix = "/hce-interface";
    static picturePrefix = AppGlobal.host + "/dev-hce/";
    static domain = AppGlobal.host + AppGlobal.interfacePrefix; //测试环境
    // static domain = "http://10.8.3.51:9090"; //

    static API = {
        test:"",
        login:'/login',//登录
        smsCode:'/message/sendSms',//短信验证码
        setPwd: '/users/pwd/set', //注册
        resetPassword: '/users/pwd/reset', //忘记密码
        modifyPassword: '/api/v1/account/modifyPwd', //修改密码
        feedback: '/api/appManage/saveFeedback', //编辑用户信息
        modifyPwd: '/users/pwd/modify', //获取用户信息
        userInfo: '/users/info', //获取任务列表列表
        base64PicUpload:'/file/upload/string',
        modifyUserInfo:'/users/info/modify',
        logout:"/users/logout",
        apply: '/card/app/active', 
        applyNxp: '/card/app/m2goAppActive', 
        acList:'/record/doorPage',
        ctList:'/record/roomPage',
        companyAndDept:'/users/queryCompanyDept',
        checkUpdate:'/api/appManage/queryVersion',
        bindJpush:'/message/bindJpush',
        applyConfirm:'/card/app/ackTransitBundle',
        falseLogin:'/api/falseLogin',
        prepSpleAcc:'/api/AcceptSample/Post',
        prepSpleMake:'/api/MadeSampleSearch/Post',
        prepSpleFlow:'/api/MadeSampleFlow/Post',
        prepSampleParams: "/api/MadeSampleParams/Post",
        unitInfo:'/api/UnitInfo/Post',
        appType:'/users/app/type',
        appInfo:'/users/app/appByType',
        flowPackList:'/api/PackageList/Post',
        flowLabList:'/api/Laboratory/Post',
        flowPackNew:'/api/CreatePackage/Post',
        flowSpleAccSearch:'/api/AcceptSubSample/Post',
        flowSpleAcc:'/api/AcceptSub/Post',
        flowPackFlow:'/api/PackageFlow/Post',
        flowPackInfo:'/api/PackageContent/Post',
        flowPackAddSple:'/api/AddPackageItem/Post',
        flowPacking:'/api/UpdatePackageStatu/Post',
        flowPackDel:'/api/PackageDelete/Post',
        flowPackDelSple:'/api/SubSampleDelete/Post',
        testProgressAcc:'/api/TestWaitAccept/Post',
        testPackAccInfo:'/api/TestWaitPackageItem/Post',
        testPackAccSearch:'/api/AcceptTestSample/Post',
        testPackAcc:'/api/UpdateAcceptStatus/Post',
        testProgress:'/api/WaitTestSamples/Post',
        testPackInfo:'/api/TestSampleSearch/Post',
        testFinish:'/api/TestFinish/Post',
        picPrefix:'/api/PrintSampleSearch/Post'
    };
    static DATA:any ={
      uploadDoc:':8080/pc/data/uploadDoc',//文件上传
    }
    static returnCode:any ={
        succeed:1//成功
    }

}

@Injectable()
export class AppServiceProvider {
  private static instance:AppServiceProvider = new AppServiceProvider();
  //当前用户信息
  public userinfo:any = {
    avatar:"",
    userName:"",
    sex:"",
    companyNo:"",
    loginName:"",
    company:"",
    deptUuid:"",
    phonenumber:"",
    token:"",
    pasFlag:""
  };

  constructor() {
    if (AppServiceProvider.instance) {
      throw new Error("错误: 请使用AppServiceProvider.getInstance() 代替使用new.");
    }
    AppServiceProvider.instance = this;
  }
    /**
     * 获取应用单例
     * 
     * @static
     * @returns {AppServiceProvider}
     */
    public static getInstance(): AppServiceProvider {
      return AppServiceProvider.instance;
  }

  public isEmojiCharacter(substring) {
    for (var i = 0; i < substring.length; i++) {
        var hs = substring.charCodeAt(i);
        if (0xd800 <= hs && hs <= 0xdbff) {
            if (substring.length > 1) {
                var ls = substring.charCodeAt(i + 1);
                var uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
                if (0x1d000 <= uc && uc <= 0x1f77f) {
                    return true;
                }
            }
        } else if (substring.length > 1) {
            var lss = substring.charCodeAt(i + 1);
            if (lss == 0x20e3) {
                return true;
            }
        } else {
            if (0x2100 <= hs && hs <= 0x27ff) {
                return true;
            } else if (0x2B05 <= hs && hs <= 0x2b07) {
                return true;
            } else if (0x2934 <= hs && hs <= 0x2935) {
                return true;
            } else if (0x3297 <= hs && hs <= 0x3299) {
                return true;
            } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d || hs == 0x3030
                || hs == 0x2b55 || hs == 0x2b1c || hs == 0x2b1b
                || hs == 0x2b50) {
                return true;
            }
        }
    }
  }
}
