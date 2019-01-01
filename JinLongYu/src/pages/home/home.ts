import { AppServiceProvider } from './../../providers/app-service/app-service';
import { Component } from '@angular/core';
import { NavController, Events, ToastController, AlertController, NavParams} from 'ionic-angular';
import { BasePage } from '../base/base';
import { DeviceIntefaceServiceProvider } from '../../providers/device-inteface-service/device-inteface-service';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage extends BasePage{
  
  constructor(
    public events:Events,
    public navCtrl: NavController, 
    public toastCtrl: ToastController, 
    public alert:AlertController,
    public device: DeviceIntefaceServiceProvider,
    public navParams: NavParams) {
    super(navCtrl, navParams,device, toastCtrl);
  }
  
  ionViewDidLoad(){

  }

}
