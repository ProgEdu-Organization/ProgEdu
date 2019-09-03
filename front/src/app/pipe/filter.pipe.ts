import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {

  transform(items: Array<any>, args: string): any {
    items = items || [];
    if (args !== undefined && items.length !== 0) {
      const result = items.filter(item => item.username.includes(args));
      return this.sortByUserName(result);
    }
    // console.log(items.sort((a, b) => a.useruame - b.useruame));
    return this.sortByUserName(items);
  }

  sortByUserName(items: Array<any>): Array<any> {
    return items.sort(function (a, b) {
      return a.username > b.username ? 1 : -1;
    });
  }
}
