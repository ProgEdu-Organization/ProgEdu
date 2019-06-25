import { Component, OnDestroy, Inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { navItems } from './_nav';
import { HttpService } from '../../services/http.service'
import { using } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  templateUrl: './default-layout.component.html'
})
export class DefaultLayoutComponent implements OnDestroy {
  public navData: Array<any> = new Array<any>();
  public navItems = navItems;
  public sidebarMinimized = true;
  private changes: MutationObserver;
  public element: HTMLElement;
  public navDataisload: boolean = false;

  constructor(@Inject(DOCUMENT) _document?: any,
    private httpService?: HttpService) {

    this.changes = new MutationObserver((mutations) => {
      this.sidebarMinimized = _document.body.classList.contains('sidebar-minimized');
    });
    this.element = _document.body;
    this.changes.observe(<Element>this.element, {
      attributes: true,
      attributeFilter: ['class']
    });
  }
  ngOnInit() {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.updateNavData();
  }

  async updateNavData() {
    const navURL = "http://140.134.26.77:8080/ProgEdu/webapi/commits/all";
    //clear student array
    this.navItems[1].children.length = 0;

    let response = await this.httpService.getData(navURL);
    this.navData = await response.result.sort(function (a, b) {
      return a.name > b.name ? 1 : - 1;
    });
    // add the data to the navItem
    for (let i of this.navData) {
      let data = {
        name: i.userName,
        url: '/base/cards',
        icon: 'icon-puzzle'
      }
      this.navItems[1].children.push(data);
    }
    this.navDataisload = true;
  }

  ngOnDestroy(): void {
    this.changes.disconnect();
  }
}
