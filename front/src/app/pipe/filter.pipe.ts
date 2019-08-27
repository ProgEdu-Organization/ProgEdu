import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {

  transform(items: Array<any>, ...args: any): any {
    items = items || [];
    if (args != null && args.length === 0) {
      const result = items.filter(item => item.useruame.includes(args));
      return this.sortByUserName(result);
    }
    console.log(items.sort((a, b) => a.useruame - b.useruame));
    return this.sortByUserName(items);
  }

  sortByUserName(items: Array<any>): Array<any> {
    return items.sort(function (a, b) {
      return a.useruame > b.useruame ? 1 : -1;
    });
  }
}
