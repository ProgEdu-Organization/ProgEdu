import { Component, OnDestroy, Inject, OnInit } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { navItems } from './_nav';
import { HttpService } from '../../services/http.service';
import { JwtService } from '../../services/jwt.service';
import { User } from '../../models/user';

@Component({
  selector: 'app-dashboard',
  templateUrl: './default-layout.component.html'
})
export class DefaultLayoutComponent implements OnDestroy, OnInit {
  public navData: Array<any> = new Array<any>();
  public navItems = navItems;
  public sidebarMinimized = true;
  private changes: MutationObserver;
  public element: HTMLElement;
  public navDataisload: boolean = false;
  public user: User;
  public isTeacher: boolean = false;

  status: { isOpen: boolean } = { isOpen: false };
  disabled: boolean = false;
  isDropup: boolean = true;
  autoClose: boolean = false;

  constructor(@Inject(DOCUMENT) _document?: any,
    private httpService?: HttpService, private jwtService?: JwtService) {
    this.user = new User(jwtService);
    if (this.user.getUserRole() === 'teacher') {
      this.isTeacher = true;
    }
    console.log('isTeacher: ' + this.isTeacher);

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
    // Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    // Add 'implements OnInit' to the class.
    this.updateNavData();
  }

  async updateNavData() {
    const navURL = 'http://140.134.26.77:8080/ProgEdu/webapi/commits/all';
    // clear student array
    this.navItems[1].children.length = 0;

    const response = await this.httpService.getData(navURL);
    this.navData = await response.result.sort(function (a, b) {
      return a.name > b.name ? 1 : - 1;
    });
    // add the data to the navItem
    for (const i of this.navData) {
      const data = {
        name: i.userName,
        url: '/base/cards',
        icon: 'icon-puzzle'
      };
      this.navItems[1].children.push(data);
    }
    this.navDataisload = true;
  }

  logout() {
    this.jwtService.removeToken();
  }

  onHidden(): void {
    console.log('Dropdown is hidden');
  }
  onShown(): void {
    console.log('Dropdown is shown');
  }
  isOpenChange(): void {
    console.log('Dropdown state is changed');
  }

  toggleDropdown($event: MouseEvent): void {
    $event.preventDefault();
    $event.stopPropagation();
    this.status.isOpen = !this.status.isOpen;
  }

  change(value: boolean): void {
    this.status.isOpen = value;
    console.log(window.location.href);
  }


  ngOnDestroy(): void {
    this.changes.disconnect();
    this.status.isOpen = false;
  }
}
