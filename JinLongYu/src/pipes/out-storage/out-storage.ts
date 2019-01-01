import { Pipe, PipeTransform } from '@angular/core';

/**
 * Generated class for the OutStoragePipe pipe.
 *
 * See https://angular.io/api/core/Pipe for more info on Angular Pipes.
 */
@Pipe({
  name: 'outStorage',
})
export class OutStoragePipe implements PipeTransform {
  /**
   * Takes a value and makes it lowercase.
   */
  transform(value: string, ...args) {
    let payStatus = String(value);
    if (payStatus == "01") {
      return "已出库";
    }else if(payStatus=="02"){
      return "部分出库"
    }
    else{
      return "未出库";
    }
  }
}
