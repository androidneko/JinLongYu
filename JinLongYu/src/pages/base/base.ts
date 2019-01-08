import { NavController, NavParams, ToastController, NavOptions } from 'ionic-angular';
import { Page, TransitionDoneFn } from 'ionic-angular/navigation/nav-util';

/**
 * Generated class for the BasePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */


export class BasePage {
  constructor(
    public navCtrl: NavController, 
    public navParams: NavParams,
    public toastCtrl?: ToastController
    ) {
    
  }
  
  toast(info){
    if(this.toastCtrl!=null)
    this.toastCtrl.create({
      message:  info,
      duration: 3000,
      position: 'middle',
      showCloseButton:false,
      closeButtonText:"关闭"
    }).present();
  }

  toastLong(info){
    if(this.toastCtrl!=null)
    this.toastCtrl.create({
      message:  info,
      duration: 3500,
      position: 'middle',
      showCloseButton:false,
      closeButtonText:"关闭"
    }).present();
  }

  toastShort(info){
    if(this.toastCtrl!=null)
    this.toastCtrl.create({
      message:  info,
      duration: 1500,
      position: 'middle',
      showCloseButton:false,
      closeButtonText:"关闭"
    }).present();
  }

  // trace(src:Page | string,des:Page | string){
  //   let srcPage = "";
  //   let desPage = "";
  //   if (typeof src === 'string'){
  //     srcPage = src;
  //   }else {
  //     srcPage = src.name;
  //   }
  //   if (typeof des === 'string'){
  //     desPage = des;
  //   }else {
  //     desPage = des.name;
  //   }
  //   this.device.push("trace",{url:AppGlobal.traceUrl,src:srcPage,des:desPage});
  // }

  setRoot(last:string,pageOrViewCtrl: Page | string, params?: any, opts?: NavOptions, done?: TransitionDoneFn): Promise<any>{
    //this.trace(last,pageOrViewCtrl);
    return this.navCtrl.setRoot(pageOrViewCtrl,params,opts,done);
  }

  push(page: Page | string, params?: any, opts?: NavOptions, done?: TransitionDoneFn): Promise<any>{
    let last = this.navCtrl.last();
    if (last && last.name){
      //this.trace(last.name,page);
    }
    return this.navCtrl.push(page,params,opts,done);
  }
}
