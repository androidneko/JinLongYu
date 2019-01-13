import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ViewController } from 'ionic-angular';

/**
 * Generated class for the ShowPicPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-show-pic',
  templateUrl: 'show-pic.html',
})
export class ShowPicPage {

  picUrl:string = "";
  constructor(public navCtrl: NavController, public navParams: NavParams,public viewCtrl:ViewController) {
    this.picUrl = navParams.data.picUrl;
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad ShowPicPage');
  }

  close(){
    this.viewCtrl.dismiss();
  }
}
