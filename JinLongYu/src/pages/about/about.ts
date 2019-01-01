import { Component, ViewChild, ElementRef } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController } from 'ionic-angular';
import { DeviceIntefaceServiceProvider } from '../../providers/device-inteface-service/device-inteface-service';
import { PhotoLibrary } from '@ionic-native/photo-library';
import { BasePage } from '../base/base';

@IonicPage()
@Component({
  selector: 'page-about',
  templateUrl: 'about.html'
})
export class AboutPage extends BasePage{
  @ViewChild('testImg') testImg:ElementRef;
  version:String = '1.0.0.0';
  constructor(public navCtrl: NavController, public device:DeviceIntefaceServiceProvider,private photoLibrary:PhotoLibrary, public navParams: NavParams,public toastCtrl?: ToastController) {
    super(navCtrl,navParams,device,toastCtrl);
  }

  ionViewDidLoad(){
    this.device.push("getVersion", "" ,msg =>{
      console.log("push success");
     this.version=msg;
    },err => {
      console.log("push failed");
    });
  }

  telClick(){
    console.log("tel click");
    this.device.push("telephone", "4000273330" ,msg =>{
      console.log("push success");
    },err => {
      console.log("push failed");
    });
  }

  checkUpdate(){
    this.device.push("checkUpdate", "true" ,msg =>{
      if (msg){
        this.toast(msg);
      }
    },err => {
      this.toast(err);
    },true);
  }

}
