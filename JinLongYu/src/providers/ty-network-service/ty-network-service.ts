import { AppServiceProvider, AppGlobal } from './../app-service/app-service';
import { Injectable, NgZone } from '@angular/core';
import { LoadingController, Events } from 'ionic-angular';
import { HttpClient, HttpRequest, HttpEventType, HttpHeaders } from '@angular/common/http';
import { Md5 } from '../../../node_modules/ts-md5/dist/md5';
declare var cordova;
/*
  Generated class for the TyNetworkServiceProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class TyNetworkServiceProvider {

  constructor(
    public loadingCtrl: LoadingController,
    public zone?:NgZone,
    public events?: Events,
    public http?:HttpClient,) {

  }
  encode(params) {
    var str = '';
    if (params) {
        for (var key in params) {
            if (params.hasOwnProperty(key)) {
                var value = params[key];
                //str += encodeURIComponent(key) + '=' + encodeURIComponent(value) + '&';
                str += key + '=' + value + '&';
            }
        }
        //str = '?' + str.substring(0, str.length - 1);
    }
    return str;
  }
  userLogin(url, params, success,failed, loader: boolean = false){
    let loading = this.loadingCtrl.create();
    if (loader) {
        loading.present();
    }
    cordova.plugins.TYNative.userLogin(params,msg => {
      //成功
      this.zone.runGuarded(()=>{
        if (loader) {
          loading.dismiss();
        }
        success(msg);
      });
      
    },(msg) => {
      //失败
      this.zone.runGuarded(()=>{
        if (loader) {
          loading.dismiss();
        }
        failed(msg);
      });
     
    });
    
  }
  httpPost(url, params, success,failed, loader: boolean = false) {
    let loadingConfig = {
      spinner: 'ios',
      content: '加载中'
    };
    let loading = this.loadingCtrl.create(loadingConfig);
    console.log("post:"+url+params);
    params["deviceId"] = AppGlobal.commInfo.deviceId;
    params["msgId"] = new Date().getTime()+"";

    // let sortParams = {};
    // let sortKeys = Object.keys(params).sort();
    // for (let index = 0; index < sortKeys.length; index++) {
    //   const key = sortKeys[index];
    //   sortParams[key] = params[key];
    // }
    //把签名放在底座去做，避免和服务端签名不一致
    //let sign = this.sign(sortParams);
    //sortParams['sign'] = sign;

    if (loader) {
        loading.present();
    }

    let finalUrl = AppGlobal.domain+url;
    //add some process for demo
    if(AppGlobal.Config.comm.mode == 0){
      if (url == AppGlobal.API.applyNxp){
        finalUrl = AppGlobal.Config.comm['prod'].host + AppGlobal.interfacePrefix + url;
      }
      if (url == AppGlobal.API.falseLogin){
        url = AppGlobal.API.login;
        finalUrl = AppGlobal.Config.comm['prod'].host + AppGlobal.interfacePrefix + url;
      }
    }
    //for demo end

    let mParams = {
      "postData":JSON.stringify(params),
      "url":(finalUrl+"")
    };

    cordova.plugins.TYNative.post(mParams,resp=>{
      this.zone.runGuarded(()=>{
        if (loader) {
          loading.dismiss();
        }
        let info = JSON.parse(resp);
        if (info.returnCode == AppGlobal.returnCode.succeed) {
          success(info);
        } else {
          if ('INVALIDTOKEN' == info.returnCode) {
            this.events.publish("INVALIDTOKEN");
            //failed(info.returnDes);
          }
          else {
            failed(info.returnDes);
          }
        }
      });
    },error=>{
      this.zone.runGuarded(()=>{
        if (loader) {
          loading.dismiss();
        }
        failed(error);
      });
    });
  }
  httpGet(url, params, success,failed, loader: boolean = false) {
    let loading = this.loadingCtrl.create();
    if (loader) {
        loading.present();
    }
    let mParams = {
      "requestActionName":params.ACTION_NAME,
      "sessionID":AppServiceProvider.getInstance().userinfo.SESSIONID,
      "userID":AppServiceProvider.getInstance().userinfo.USERID,
      "actionInfoStr":JSON.stringify(params),
      "url":(AppGlobal.domain+url+"")
    };
    cordova.plugins.TYNative.get(mParams,msg=>{
    
      this.zone.runGuarded(()=>{
        if (loader) {
          loading.dismiss();
        }
        success(msg);
      });
    },error=>{
      this.zone.runGuarded(()=>{
        if (loader) {
          loading.dismiss();
        }
        failed(error);
      });
    });
  } 


  isFirst:boolean = true;
  count:number = 0;
  uploadFile(arr, success, failed, progress, complete) {    
    if (this.isFirst) {
      this.count = arr.length;
      this.isFirst = false;
    }

    let base64 = arr.pop();
    let token = AppServiceProvider.getInstance().userinfo.token;
    this.uploadFilePost(base64, token, (response) => {
      success(base64, response);
      if (arr.length > 0) {
        //继续发送
        this.uploadFile(arr, success, failed, progress, complete);
      } else {
        //发送完成
        this.isFirst = true;
        complete();
      }
    }, error => {
      this.isFirst = true;
      failed(error);
    }, uploadProgress => {
      let myProgress = uploadProgress;
      if (this.count > 1) {
        let subProgress = uploadProgress / this.count;
        myProgress = 100 * (this.count - arr.length - 1) / this.count + subProgress;
      }
      myProgress = parseInt(0 + myProgress);
      progress(myProgress);
    });
  }

  uploadFilePost(base64DataUrl, token, success, failed, progress?) {
    let params = { "fileContent": base64DataUrl };
    console.log("post:" + AppGlobal.domain +  AppGlobal.API.base64PicUpload + "-->" + JSON.stringify(params));
    
    let sortParams = {};
    let sortKeys = Object.keys(params).sort();
    for (let index = 0; index < sortKeys.length; index++) {
      const key = sortKeys[index];
      sortParams[key] = params[key];
    }

    let sign = this.sign(sortParams);
    sortParams['sign'] = sign;
    
    let httpHeader = new HttpHeaders({ "Authorization": token });
    this.http.request(new HttpRequest(
      'POST',
      AppGlobal.domain +  AppGlobal.API.base64PicUpload,
      sortParams,
      {
        headers: httpHeader,
        reportProgress: true
      })).subscribe(event => {

        if (event.type === HttpEventType.DownloadProgress) {
          // {
          // loaded:11, // Number of bytes uploaded or downloaded.
          // total :11 // Total number of bytes to upload or download
          // }
          console.log('loaded:' + event.loaded + '  total:' + event.total);
        }

        if (event.type === HttpEventType.UploadProgress) {
          // {
          // loaded:11, // Number of bytes uploaded or downloaded.
          // total :11 // Total number of bytes to upload or download
          // }
          console.log('loaded:' + event.loaded + '  total:' + event.total);
          if (event.total && progress) {
            let uploadProgress = event.loaded / event.total;
            console.log('progress:' + uploadProgress);
            uploadProgress = parseInt('0' + uploadProgress * 100);
            progress(uploadProgress);
          }
        }

        if (event.type === HttpEventType.Response) {
          console.log(event.body);

          let res = event.body
          //成功
          this.zone.runGuarded(() => {

            if (success != null) {
              console.log("result:" + JSON.stringify(res));
              let re = res as any;
              let code = re.returnCode;
              if ("SUCCESS" == code) {
                let data = re.data;
                success(data);
              } else {
                let des = re.returnDes;
                if (code == "INVALIDTOKEN") {
                  des = "会话过期，请重新登录!";
                  this.events.publish("INVALIDTOKEN");
                }
                //failed(des);
              }
            }
          });
        }
      }, error => {
        this.zone.runGuarded(() => {

          if (failed != null) {
            failed(error);
          }
        });
      })

  }

  sign(params) {
    var msg = this.encode(params);
    var str = msg + "key=" + AppGlobal.Config.comm.appKey;
    console.log("before md5-->"+str);
    return Md5.hashStr(str).toString().toUpperCase();
  }

}
