import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {

  transform(items: Array<any>, ...args: any): any {
    items = items || [];
    if (args != null && args != '') {
      var result = items.filter(item => item.userName.includes(args));
      return this.sortByUserName(result);
    }
    console.log(items.sort((a, b) => a.userName - b.userName))
    return this.sortByUserName(items);
  }

  sortByUserName(items: Array<any>): Array<any> {
    return items.sort(function (a, b) {
      return a.userName > b.userName ? 1 : -1;
    });
  }
}
