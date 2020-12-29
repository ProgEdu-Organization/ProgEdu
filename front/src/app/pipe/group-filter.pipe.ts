import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'groupFilter'
})
export class GroupFilterPipe implements PipeTransform {

  transform(items: Array<any>, args: string): any {
    items = items || [];
    if (args !== undefined && items.length !== 0) {
      const result = items.filter(item => item.groupName.includes(args));
      return this.sortByGroupName(result);
    }
    // console.log(items.sort((a, b) => a.useruame - b.useruame));
    return this.sortByGroupName(items);
  }

  sortByGroupName(items: Array<any>): Array<any> {
    return items.sort(function (a, b) {
      return a.name > b.name ? 1 : -1;
    });
  }

}
