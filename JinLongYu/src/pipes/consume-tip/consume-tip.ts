import { Pipe, PipeTransform } from '@angular/core';

/**
 * Generated class for the ConsumeTipPipe pipe.
 *
 * See https://angular.io/api/core/Pipe for more info on Angular Pipes.
 */
@Pipe({
  name: 'consumeTip',
})
export class ConsumeTipPipe implements PipeTransform {
  /**
   * Takes a value and makes it lowercase.
   */
  transform(value: string, ...args) {
    return value.toLowerCase();
  }
}
