import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ViewController } from 'ionic-angular';

/**
 * Generated class for the UserProtocolPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-user-protocol',
  templateUrl: 'user-protocol.html',
})
export class UserProtocolPage {

  constructor(public navCtrl: NavController, public navParams: NavParams,public viewCtrl:ViewController,) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad UserProtocholPage');
  }

  close(){
    this.viewCtrl.dismiss();
  }
}
