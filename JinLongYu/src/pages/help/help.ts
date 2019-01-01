import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';

/**
 * Generated class for the HelpPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-help',
  templateUrl: 'help.html',
})
export class HelpPage {

  tag1='ios-arrow-down';
  tag2='ios-arrow-down';
  tag3='ios-arrow-down';
  tag4='ios-arrow-down';
  tag5='ios-arrow-down';
  
  constructor(public navCtrl: NavController, public navParams: NavParams) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad HelpPage');
  }

  tag1Click(){
    if(this.tag1=='ios-arrow-down'){
      this.tag1='ios-arrow-up';
    }else{
      this.tag1='ios-arrow-down';
    }
  }

  tag2Click(){
    if(this.tag2=='ios-arrow-down'){
      this.tag2='ios-arrow-up';
    }else{
      this.tag2='ios-arrow-down';
    }
  }

  tag3Click(){
    if(this.tag3=='ios-arrow-down'){
      this.tag3='ios-arrow-up';
    }else{
      this.tag3='ios-arrow-down';
    }
  }

  tag4Click(){
    if(this.tag4=='ios-arrow-down'){
      this.tag4='ios-arrow-up';
    }else{
      this.tag4='ios-arrow-down';
    }
  }

  tag5Click(){
    if(this.tag5=='ios-arrow-down'){
      this.tag5='ios-arrow-up';
    }else{
      this.tag5='ios-arrow-down';
    }
  }
}
