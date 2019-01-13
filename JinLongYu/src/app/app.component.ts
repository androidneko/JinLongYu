/**
 *　　　　　　　 ┏┓       ┏┓+ +
 *　　　　　　　┏┛┻━━━━━━━┛┻┓ + +
 *　　　　　　　┃　　　　　　 ┃
 *　　　　　　　┃　　　━　　　┃ ++ + + +
 *　　　　　　 █████━█████  ┃+
 *　　　　　　　┃　　　　　　 ┃ +
 *　　　　　　　┃　　　┻　　　┃
 *　　　　　　　┃　　　　　　 ┃ + +
 *　　　　　　　┗━━┓　　　 ┏━┛
 *               ┃　　  ┃
 *　　　　　　　　　┃　　  ┃ + + + +
 *　　　　　　　　　┃　　　┃　Code is far away from     bug with the animal protecting
 *　　　　　　　　　┃　　　┃ + 　　　　         神兽保佑,代码无bug
 *　　　　　　　　　┃　　　┃
 *　　　　　　　　　┃　　　┃　　+
 *　　　　　　　　　┃　 　 ┗━━━┓ + +
 *　　　　　　　　　┃ 　　　　　┣┓
 *　　　　　　　　　┃ 　　　　　┏┛
 *　　　　　　　　　┗┓┓┏━━━┳┓┏┛ + + + +
 *　　　　　　　　　 ┃┫┫　 ┃┫┫
 *　　　　　　　　　 ┗┻┛　 ┗┻┛+ + + +
 */
import { DbServiceProvider } from './../providers/db-service/db-service';
import { Component, ViewChild } from '@angular/core';
import { Platform, Keyboard, ToastController, IonicApp, Nav, App, Events } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { AppServiceProvider, AppGlobal } from '../providers/app-service/app-service';
import { DeviceIntefaceServiceProvider } from './../providers/device-inteface-service/device-inteface-service';

// import { TabsPage } from '../pages/tabs/tabs';

@Component({
  templateUrl: 'app.html'
})
export class MyApp {
  // rootPage:any = TabsPage;
  @ViewChild(Nav) nav: Nav;
  rootPages: Array<string> = ["LoginPage", "HomePage"];
  backButtonPressed: boolean = false;
  rootPage: string = "";
  constructor(
    public app: App,
    public platform: Platform,
    public ionicApp: IonicApp,
    public events: Events,
    public statusBar: StatusBar,
    public keyboard: Keyboard,
    public toastCtrl: ToastController,
    public splashScreen: SplashScreen,
    public db: DbServiceProvider,
    public device: DeviceIntefaceServiceProvider) {
    platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      if (platform.is("ios")) {
        statusBar.styleDefault();
      }
      console.log("platform has been ready...");
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      if (this.platform.is("android")) {
        this.statusBar.backgroundColorByHexString("#00aeff");
        this.registerBackButtonAction();
      }
      this.eventsregisterTokenErrEvent();
      //数据初始化,装置配置信息
      this.initData();
    });
  }

  //更新登录状态，重新登陆
  eventsregisterTokenErrEvent() {
    this.events.subscribe('INVALIDTOKEN', () => {
      this.toastCtrl.create({
        message: '您的登录状态已失效，请重新登录',
        duration: 2000,
        position: 'bottom',
        cssClass: 'text-align: center'
      }).present();
      AppServiceProvider.getInstance().userinfo.token = "";
      AppServiceProvider.getInstance().userinfo.loginName = "";
      AppServiceProvider.getInstance().userinfo.avatar = "";
      //用户信息整体保存，整体更新，整体读取
      //用户信息更新保存场景:登录、申请成功，修改用户头像（首页和我的用户信息页面）
      this.db.saveString(JSON.stringify(AppServiceProvider.getInstance().userinfo),
        AppServiceProvider.getInstance().userinfo.username + "_userinfo");
      this.db.saveString("", AppServiceProvider.getInstance().userinfo.username + "_token", () => {
        this.nav.setRoot("LoginPage");
      });
    });
  }

  //数据初始化
  initData() {
    this.configSettingData();
  }
  //初始化配置信以后服务器地址可能重同一个配置文件和特殊化设置
  configSettingData() {
    return this.getConfig()
      //.then(() => { return this.getCommInfo() })
      .then(() => { return this.getUsername() })
      .then((username) => { return this.getUserInfo(username) })
      .then(() => {
        this.launchPage("HomePage");
      }, err => {
        this.launchPage("LoginPage");
        //this.launchPage("RootPage");
      })
  }

  checkUpdate(){
    this.device.push("checkUpdate", "false" ,msg =>{},err => {},false);
  }

  getConfig() {
    return new Promise((resolve, reject) => {
      this.device.push("config", "", (msg) => {
        AppGlobal.Config = JSON.parse(msg);
        AppGlobal.host = AppGlobal.Config.comm[AppGlobal.Config.comm.env].host;
        AppGlobal.domain = AppGlobal.host + AppGlobal.interfacePrefix;
        AppGlobal.picturePrefix = AppGlobal.host + "/profile/";
        resolve();
      }, (err) => {
        AppGlobal.host = AppGlobal.Config.comm[AppGlobal.Config.comm.env].host;
        AppGlobal.domain = AppGlobal.host + AppGlobal.interfacePrefix;
        AppGlobal.picturePrefix = AppGlobal.host + "/profile/";
        resolve();
      });
    });

  }

  getCommInfo() {
    //检查更新
    //this.checkUpdate();
    
    return new Promise((resolve, reject) => {
      this.device.push("commInfo", "", msg => {
        if (msg) {
          console.log("commInfo-->" + JSON.stringify(msg));
          AppGlobal.commInfo.deviceId = msg.deviceId;
          AppGlobal.commInfo.appVersion = msg.appVersion;
          AppGlobal.commInfo.model = msg.model;
          AppGlobal.commInfo.brand = msg.brand;
          AppGlobal.commInfo.manufacturer = msg.manufacturer;
          AppGlobal.commInfo.osVersion = msg.osVersion;
          AppGlobal.commInfo.networkType = msg.networkType;
          AppGlobal.commInfo.ipAddress = msg.ipAddress;
        }
        resolve();
      }, err => { resolve(); });
    });
  }

  //客户版自动登录需先读取用户保存的首页菜单，客户经理版则直接读用户信息
  clientAutoLogin() {
    this.getUsername()
      .then((username) => { return this.getUserInfo(username); })
      .then(() => {
        this.launchPage("RootPage");
      }, err => {
        this.launchPage("LoginPage");
      })
  }

  //为了解决此处过深嵌套回调，难以阅读，使用promise链式结构改善
  tryToLogin() {
    return this.getUsername()
      .then(username => { return this.getUserInfo(username); })
      .then(token => {
        AppServiceProvider.getInstance().userinfo.token = token
      });
  }

  getUsername() {
    return new Promise((resolve, reject) => {
      this.db.getString("username", (msg) => {
        console.log("username-->" + msg);
        AppServiceProvider.getInstance().userinfo.loginName = msg;
        resolve(msg);
      },
        (err) => {
          this.launchPage("LoginPage");
        });
    });
  }

  getUserInfo(username) {
    return new Promise((resolve, reject) => {
      this.db.getString(username + "_userinfo",
        (msg) => {
          console.log("userinfo-->" + msg);
          if (msg != null && msg != "" && msg.length > 0) {
            AppServiceProvider.getInstance().userinfo = JSON.parse(msg);
            //console.log("token-->"+msg);
            if (AppServiceProvider.getInstance().userinfo.token) {
              resolve();
            } else {
              this.launchPage("LoginPage");
              //this.launchPage("RootPage");
            }
          } else {
            this.launchPage("LoginPage");
          }
        },
        (err) => {
          this.launchPage("LoginPage");
        });
    });
  }

  launchPage(page: string) {
    this.splashScreen.hide();
    this.rootPage = page;
    //埋点统计
    //this.trace("splash",page);
    console.log("launchPage-->" + page);
  }

  getUserinfo(username) {
    return new Promise((resolve, reject) => {
      this.db.getString("username_" + username, (data) => {
        try {
          AppServiceProvider.getInstance().userinfo = JSON.parse(data);
          resolve();
        } catch (error) {
          this.launchPage("LoginPage");
        }
      }, (err) => { this.launchPage("LoginPage"); });
    });
  }

  //跳转引导页面
  goToGuild() {

  }

  registerBackButtonAction() {
    this.platform.registerBackButtonAction(() => {
      console.log("this.keyboard.isOpen():" + this.keyboard.isOpen());
      if (this.keyboard.isOpen()) {
        //按下返回键时，先关闭键盘
        this.keyboard.close();
        return;
      };

      //如果想点击返回按钮隐藏toast或loading或Overlay就把下面加上
      // this.ionicApp._toastPortal.gaetActive() || this.ionicApp._loadingPortal.getActive() || this.ionicApp._overlayPortal.getActive()
      //不写this.ionicApp._toastPortal.getActive()是因为连按2次退出
      let activePortal = this.ionicApp._modalPortal.getActive() || this.ionicApp._overlayPortal.getActive();
      let loadingPortal = this.ionicApp._loadingPortal.getActive();

      if (activePortal) {
        //其他的关闭
        activePortal.dismiss().catch(() => {
        });
        activePortal.onDidDismiss(() => {
        });
        return;
      }
      if (loadingPortal) {
        //loading的话，返回键无效
        return;
      }

      let activeVC = this.nav.getActive();
      let page = activeVC.instance;

      if (page.tabs) {
        let activeNav = page.tabs.getSelected();
        if (activeNav.canGoBack()) {
          return activeNav.pop();
        } else {
          return this.showExit();
        }
      }

      console.log("events go on ");
      if (this.rootPages.indexOf(this.nav.getActive().id) >= 0) {
        return this.showExit();
      } else {
        return this.nav.getActive().instance.navCtrl.pop();
      }
    }, 1);
  }

  //双击退出提示框
  showExit() {
    if (this.backButtonPressed) { //当触发标志为true时，即1秒内双击返回按键则退出APP
      this.platform.exitApp();
    } else {
      this.toastCtrl.create({
        message: '再按一次退出应用',
        duration: 1000,
        position: 'bottom',
        cssClass: 'text-align: center'
      }).present();
      this.backButtonPressed = true;
      setTimeout(() => this.backButtonPressed = false, 1000);//1秒内没有再次点击返回则将触发标志标记为false
    }
  }

  // trace(src:string,des:string){
  //   this.device.push("trace",{url:AppGlobal.traceUrl,src:src,des:des});
  // }
}
