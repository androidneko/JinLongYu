// import { AppGlobal } from './../app-service/app-service';
// import { TyNetworkServiceProvider } from './../ty-network-service/ty-network-service';
import { LoadingController, Events } from 'ionic-angular';
import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { AppGlobal, AppServiceProvider } from '../app-service/app-service';
import { Md5 } from '../../../node_modules/ts-md5/dist/md5';

/*
  Generated class for the WebTyNetworkServiceProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class WebTyNetworkServiceProvider {

  constructor(public http: Http, public loadingCtrl: LoadingController, public events?: Events) {

  }

  encode(params) {
    var str = '';
    if (params) {
      for (var key in params) {
        if (params.hasOwnProperty(key)) {
          var value = params[key];
          str += encodeURIComponent(key) + '=' + encodeURIComponent(value) + '&';
        }
      }
      str = '?' + str.substring(0, str.length - 1);
    }
    return str;
  }

  userLogin(url, params, success, failed, loader: boolean = false) {
    let loading = this.loadingCtrl.create();
    if (loader) {
      loading.present();
    }
    this.http.get("assets/data/userinfo.json")
      .toPromise()
      .then(res => {
        if (loader) {
          loading.dismiss();
        }
        let obj = res.json();
        let str = JSON.stringify(obj);
        success(str);
      })
      .catch(error => {
        if (loader) {
          loading.dismiss();
        }
        console.log(error);
        failed(error.message);
      });
  }

  httpPost(url, params, success, failed, loader: boolean = false) {
    let loading = this.loadingCtrl.create();
    if (loader) {
      loading.present();
    }
    console.log("request url:" + AppGlobal.domain + url);
    console.log("request data:" + JSON.stringify(params));
    params["deviceId"] = AppGlobal.commInfo.deviceId;
    params["msgId"] = new Date().getTime() + "";

    // let sortParams = {};
    // let sortKeys = Object.keys(params).sort();
    // for (let index = 0; index < sortKeys.length; index++) {
    //   const key = sortKeys[index];
    //   sortParams[key] = params[key];
    // }
    // let sign = this.sign(sortParams);
    // sortParams['sign'] = sign;

    this.http.post(AppGlobal.domain + url, JSON.stringify(params),
      {
        headers: new Headers(
          {
            "content-type": "application/json;charset=UTF-8",
            "APPID": AppGlobal.commInfo.appId,
            "Authorization": AppServiceProvider.getInstance().userinfo.token,
            "ENCODEMETHOD": "",
            "SIGNMETHOD": ""
          }
        )
      }
    )
      .toPromise()
      .then(res => {

        setTimeout(() => {
          if (loader) {
            loading.dismiss();
          }
          let m = res.json();
          console.log("response data:" + JSON.stringify(m));
          //let info = JSON.parse(m);
          if (m.code == AppGlobal.returnCode.succeed) {
            success(m);
          } else {
            if ('INVALIDTOKEN' == m.code) {
              this.events.publish("INVALIDTOKEN");
              //failed(info.returnDes);
            }
            else {
              failed(m.message);
            }
          }
        }, 1000);

      })
      .catch(error => {

        setTimeout(() => {
          if (loader) {
            loading.dismiss();
          }
          console.log(error);
          failed(error.message);
        }, 1000);

      });

  }

  httpGet(url, params, success, failed, loader: boolean = false) {
    let loading = this.loadingCtrl.create();
    if (loader) {
      loading.present();
    }
    let actioname = params.ACTION_NAME.replace("|", "");
    let murl = "assets/data/" + actioname + '.json';
    this.http.get(murl)
      .toPromise()
      .then(res => {

        setTimeout(() => {
          if (loader) {
            loading.dismiss();
          }

          let m = res.text();
          var reg = /("([^\\\"]*(\\.)?)*")|('([^\\\']*(\\.)?)*')|(\/{2,}.*?(\r|\n))|(\/\*(\n|.)*?\*\/)/g;
          let str = m.replace(reg, function (word) { // 去除注释后的文本  
            return /^\/{2,}/.test(word) || /^\/\*/.test(word) ? "" : word;
          });
          success(str);
        }, 1000);

      })
      .catch(error => {

        setTimeout(() => {
          if (loader) {
            loading.dismiss();
          }
          console.log(error);
          failed(error.message);
        }, 1000);

      });
  }

  sign(params) {
    var msg = this.encode(params);
    var str = msg + "key=" + AppGlobal.Config.comm.appKey;
    return Md5.hashStr(str).toString().toUpperCase();
  }
}
