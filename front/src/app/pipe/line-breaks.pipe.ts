import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser'

@Pipe({
  name: 'lineBreaks'
})
export class LineBreaksPipe implements PipeTransform {

  constructor(private sanitized: DomSanitizer) { }
  transform(message: any, ...args: any[]): any {
    return message.replace(/ /g, String.fromCharCode(160)).replace('\n', '<br />');
  }

}
