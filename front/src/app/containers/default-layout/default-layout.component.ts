import { Component, OnDestroy, Inject, OnInit, ViewChild } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { navItems } from './_nav';
import { JwtService } from '../../services/jwt.service';
import { User } from '../../models/user';
import { DefaultLayoutService } from './default-layout.service';
import { Router } from '@angular/router';
import { ModalDirective } from 'ngx-bootstrap/modal';
import * as $ from 'jquery';

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
  public isAdmin: boolean = false;

  dashboard: string;
  status: { isOpen: boolean } = { isOpen: false };
  disabled: boolean = false;
  isDropup: boolean = true;
  autoClose: boolean = false;
  @ViewChild('dangerModal', { static: false }) public dangerModal: ModalDirective;

  constructor(@Inject(DOCUMENT) _document?: any, private defaultLayoutService?: DefaultLayoutService,
    private jwtService?: JwtService, private router?: Router) {
    console.log('isTeacher: ' + this.isAdmin);
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
    this.user = new User(this.jwtService);
    if (this.user.getIsAdmin()) {
      this.dashboard = '/dashboard';
      this.isAdmin = true;
    } else {
      this.dashboard = '/studashboard';
      this.isAdmin = false;
    }

    this.updateNavData();
    // console.log($('a[.navbar]').attr('href', 'studashboard'))
  }

  async updateNavData() {
    // clear student array
    this.navItems[2].children.length = 0;
    this.defaultLayoutService.getNavData().subscribe(response => {
      console.log('test' + JSON.stringify(response));
      this.navData = response.result.sort(function (a, b) {
        return a.name > b.name ? 1 : - 1;
      });
      // add the data to the navItem
      for (const i of this.navData) {
        const data = {
          name: i.userName,
          url: '/base/cards',
          icon: 'icon-puzzle'
        };
        this.navItems[2].children.push(data);
      }
      this.navDataisload = true;
    });
  }

  changeToDashboard() {
    this.router.navigate([this.dashboard]);
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

  modifySecret() {
    this.dangerModal.show();
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
