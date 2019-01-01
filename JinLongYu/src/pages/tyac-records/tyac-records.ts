import { Component, ViewChild } from '@angular/core';
import { IonicPage, NavController, NavParams, DateTime, ToastController } from 'ionic-angular';
import { TyNetworkServiceProvider } from '../../providers/ty-network-service/ty-network-service';
import { DeviceIntefaceServiceProvider } from '../../providers/device-inteface-service/device-inteface-service';
import { BasePage } from '../base/base';
import { AppGlobal } from '../../providers/app-service/app-service';

/**
 * Generated class for the TyacRecordsPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-tyac-records',
  templateUrl: 'tyac-records.html',
})
export class TyacRecordsPage extends BasePage{
  @ViewChild('picker1') datePicker: DateTime;
  //accessCtrl,canteen
  recCategory: any = "accessCtrl";
  tabList = [{name:"accessCtrl",count:0},{name:"canteen",count:0}];
  tabIndex:number = 0;
  failed :boolean = false;

  acList:any[] = [
    // {state:"0",cardType:"0",week:"星期三",date:"2018-09-12",time:"13:30:26",zone:"公司正门"},
    // {state:"0",cardType:"1",week:"星期一",date:"2018-09-12",time:"13:30:26",zone:"公司正门"},
    // {state:"1",cardType:"1",week:"星期二",date:"2018-09-12",time:"13:30:26",zone:"公司正门"},
    // {state:"0",cardType:"0",week:"星期四",date:"2018-09-12",time:"13:30:26",zone:"公司正门"},
    // {state:"1",cardType:"1",week:"星期五",date:"2018-09-12",time:"13:30:26",zone:"公司正门"},
    // {state:"1",cardType:"0",week:"星期六",date:"2018-09-12",time:"13:30:26",zone:"公司正门"}
  ];
  ctList:any[] = [
    // {type:"0",week:"星期三",date:"2018-09-12",time:"13:30:26",name:"储值消费",detail:"-15.00元"},
    // {type:"0",week:"星期三",date:"2018-09-12",time:"13:30:26",name:"储值消费",detail:"-15.00元"},
    // {type:"1",week:"星期三",date:"2018-09-12",time:"13:30:26",name:"午餐消费",detail:"1次"},
    // {type:"0",week:"星期三",date:"2018-09-12",time:"13:30:26",name:"储值消费",detail:"-15.00元"},
    // {type:"1",week:"星期三",date:"2018-09-12",time:"13:30:26",name:"午餐消费",detail:"1次"},
    // {type:"1",week:"星期三",date:"2018-09-12",time:"13:30:26",name:"午餐消费",detail:"1次"},
    // {type:"1",week:"星期三",date:"2018-09-12",time:"13:30:26",name:"午餐消费",detail:"1次"}
  ];
  
  acInfo:any = {
    lateCount:0,
    earlyCount:0
  }

  ctInfo:any = {
    consumeAmount:0.00,
    consumeCount:0
  }

  date = "";
  format = "YYYY-MM";
  max = "";
  min = "";

  constructor(
    public toastCtrl: ToastController, 
    public navCtrl: NavController, 
    public navParams: NavParams, 
    public net: TyNetworkServiceProvider, 
    public device: DeviceIntefaceServiceProvider) {
    super(navCtrl, navParams,device, toastCtrl);
    this.initDateTimePicker();
  }


  ionViewDidLoad() {
    console.log('ionViewDidLoad TyacRecordsPage');
    this.segmentClick(0);
  }

  segmentClick(index:number) {
    this.tabIndex = index;
    this.recCategory = this.tabList[index].name;
    if (index == 0){
      this.getACRecords();
    }

    if (index == 1){
      this.getCantRecords();
    }
  }

  refresh(){
    if (this.tabIndex == 0){
      this.getACRecords();
    }

    if (this.tabIndex == 1){
      this.getCantRecords();
    }
  }

  getACRecords(){
    this.net.httpPost(AppGlobal.API.acList,{pageNo:1,pageSize:100,date:this.date},
      resp=>{
        //let info = JSON.parse(success);
        this.failed = false;
        this.resetAc();
        if (resp.data.doorRecordList && resp.data.doorRecordList.length > 0){
          this.acInfo = resp.data;
          this.acInfo.doorRecordList.forEach(element => {
            if (element.state != '3'){
              this.acList.push(element);
            }
          });
        }
      },
      err=>{
        this.toast(err);
        this.failed = true;
        this.resetAc();
      },
      true);
  }

  getCantRecords(){
    this.net.httpPost(AppGlobal.API.ctList,{pageNo:1,pageSize:100,date:this.date},
      resp=>{
        //let info = JSON.parse(success);
        this.failed = false;
        this.resetCt();
        if (resp.data.roomRecordList && resp.data.roomRecordList.length > 0){
          this.ctInfo = resp.data;
          this.ctInfo.roomRecordList.forEach(element => {
            if (element.state != '3'){
              this.ctList.push(element);
            }
          });
        }
      },
      err=>{
        this.toast(err);
        this.failed = true;
        this.resetCt();
      },
      true);
  }

  showPicker(){
    this.datePicker.open();
  }

  help(){
    this.navCtrl.push("HelpPage");
  }

  changeDate(event){
    console.log("changeDate---->"+event);
    this.date = event;
    if (this.tabIndex == 0){
      this.getACRecords();
    }
    if (this.tabIndex == 1){
      this.getCantRecords();
    }
  }

  resetAc(){
    this.acList = [];
    this.acInfo = {
      lateCount:0,
      earlyCount:0
    }
  }

  resetCt(){
    this.ctList = [];
    this.ctInfo = {
      consumeAmount:0.00,
      consumeCount:0
    }
  }

  initDateTimePicker(){
    let now = new Date();
    let year:number = now.getUTCFullYear();
    let month: any = (now.getMonth() + 1) < 10 ? '0' + (now.getMonth() + 1) : (now.getMonth() + 1);
    if (month == '01'){
      this.max = year+"-"+month;
      this.min = (year-1)+"-12";
    }else {
      this.max = year+"-"+month;
      let lastMonth: any = now.getMonth() < 10 ? '0' + now.getMonth() : now.getMonth();
      this.min = year+"-"+lastMonth;
    }
    this.date = this.max;
  }
}
