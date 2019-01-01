import { Pipe, PipeTransform } from '@angular/core';

/**
 * Generated class for the StateTipPipe pipe.
 *
 * See https://angular.io/api/core/Pipe for more info on Angular Pipes.
 */
@Pipe({
  name: 'stateTip',
})
export class StateTipPipe implements PipeTransform {
  /**
   * Takes a value and makes it lowercase.
   */
  transform(value: string, ...args) {
    let state = value[0];
    if (state == "0"){
      return "正常";
    }
    if (state == "1"){
      return "迟到";
    }
    if (state == "2"){
      return "早退";
    }
    if (state == "3"){
      return "旷工";
    }
    return state;
  }
}
