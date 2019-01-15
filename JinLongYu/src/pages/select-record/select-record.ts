import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';

/**
 * Generated class for the SelectRecordPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-select-record',
  templateUrl: 'select-record.html',
})
export class SelectRecordPage {

  records:any = [];

  constructor(public navCtrl: NavController, public navParams: NavParams) {
    if (navParams.data.timeList){
      this.records = navParams.data.timeList;
    }
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad SelectRecordPage');
  }

  select(item){
    if (this.navParams.data.callback){
      this.navParams.data.callback(item);
      this.navCtrl.pop();
    }
  }
}
