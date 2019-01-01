import { Pipe, PipeTransform } from '@angular/core';

/**
 * Generated class for the TextDatePipe pipe.
 *
 * See https://angular.io/api/core/Pipe for more info on Angular Pipes.
 */
@Pipe({
  name: 'textDate',
})
export class TextDatePipe implements PipeTransform {
  /**
   * Takes a value and makes it lowercase.
   */

  //转为时间 并返回时间搓

  transform(value: string, ...args) {
    var beginDate:any = new Date();
    var endDate:any = new Date();

    if (value == '今天') {
      //
      return{
        beginTime: this.dateFormat(beginDate, "yyyy-MM-dd"),
        endTime: this.dateFormat(endDate, "yyyy-MM-dd")
      };
    }
    if (value == "本周") {
      var day = endDate.getDay();
      var oneDayLong = 24 * 60 * 60 * 1000;
      var MondayTime = endDate - (day - 1) * oneDayLong;
      beginDate = MondayTime;
      return {
        beginTime: this.dateFormat(beginDate, "yyyy-MM-dd"),
        endTime: this.dateFormat(endDate, "yyyy-MM-dd")
      };
    }
    if (value == "本月") {
      //本月时间
      beginDate = (new Date((new Date()).setMonth((new Date().getMonth() - 0)))).setDate(1);
      return {
        beginTime: this.dateFormat(beginDate, "yyyy-MM-dd"),
        endTime: this.dateFormat(endDate, "yyyy-MM-dd")
      };
    }
    if (value == "本季度") {

      beginDate = (new Date((new Date()).setMonth((new Date().getMonth() - 2)))).setDate(1);
      return {
        beginTime: this.dateFormat(beginDate, "yyyy-MM-dd"),
        endTime: this.dateFormat(endDate, "yyyy-MM-dd")
      };
    }
    if (value == "最近半年") {
      //最近半年
      beginDate = (new Date((new Date()).setMonth((new Date().getMonth() - 5)))).setDate(1);
      return {
        beginTime: this.dateFormat(beginDate, "yyyy-MM-dd"),
        endTime: this.dateFormat(endDate, "yyyy-MM-dd")
      };
    }
    if (value == "全部") {
      return {
        beginTime: "",
        endTime: ""
      };;
    }
    return {
      beginTime: "",
      endTime: ""
    };;

  }
  dateFormat(value: any, format: string): any {
    let Dates = new Date(value);
    let year: number = Dates.getFullYear();
    let month: any = (Dates.getMonth() + 1) < 10 ? '0' + (Dates.getMonth() + 1) : (Dates.getMonth() + 1);
    let day: any = Dates.getDate() < 10 ? '0' + Dates.getDate() : Dates.getDate();
    let h = Dates.getHours();
    let m = Dates.getMinutes();
    let s = Dates.getHours();
    return format.replace("yyyy", this.pad(year, 4) + "").replace("MM", this.pad(month, 2) + "").replace("dd", this.pad(day, 2) + "").replace("HH", this.pad(h, 2) + "").replace("mm", this.pad(m, 2) + "").replace("ss", this.pad(s, 2) + "");
  }
  pad(num, n) {
    let y = '00000000000000000000000000000' + num; //爱几个0就几个，自己够用就行
    return y.substr(y.length - n);
  }
}
