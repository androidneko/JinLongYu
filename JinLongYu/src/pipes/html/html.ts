import { DomSanitizer } from '@angular/platform-browser';
import { Pipe, PipeTransform } from '@angular/core';

/**
 * Generated class for the HtmlPipe pipe.
 *
 * See https://angular.io/api/core/Pipe for more info on Angular Pipes.
 */
@Pipe({
  name: 'html',
})
export class HtmlPipe implements PipeTransform {
  /**
   * Takes a value and makes it lowercase.
   */
  constructor (private sanitizer:DomSanitizer){

  }
  transform(style) {
    return this.sanitizer.bypassSecurityTrustHtml(style);
  }
}
