import { Pipe, PipeTransform } from '@angular/core';

/**
 * Generated class for the DateFormatPipe pipe.
 *
 * See https://angular.io/api/core/Pipe for more info on Angular Pipes.
 */
@Pipe({
  name: 'dateFormat',
})
export class DateFormatPipe implements PipeTransform {
  /**
   * Takes a value and makes it lowercase.
   */
  transform(value: string, ...args) {
    let yyyyMM:string = value[0];
    if (yyyyMM){
      return yyyyMM.split('-')[0]+'年'+yyyyMM.split('-')[1]+'月';
    }
    return yyyyMM;
  }
}
