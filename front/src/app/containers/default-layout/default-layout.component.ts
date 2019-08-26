import { Component, OnDestroy, Inject, OnInit, ViewChild } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { navItems } from './_nav';
import { JwtService } from '../../services/jwt.service';
import { User } from '../../models/user';
import { DefaultLayoutService } from './default-layout.service';
import { Router } from '@angular/router';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import * as $ from 'jquery';
import { ɵangular_packages_platform_browser_platform_browser_d } from '@angular/platform-browser';

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

  public modifySecretForm: FormGroup;

  dashboard: string;
  status: { isOpen: boolean } = { isOpen: false };
  disabled: boolean = false;
  isDropup: boolean = true;
  autoClose: boolean = false;
  @ViewChild('modifySecretModal', { static: false }) public modifySecretModal: ModalDirective;

  constructor(@Inject(DOCUMENT) _document?: any, private defaultLayoutService?: DefaultLayoutService,
    private fb?: FormBuilder,
    private jwtService?: JwtService, private router?: Router) {
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
    if (this.user.isTeacher) {
      this.dashboard = '/dashboard';
      this.isTeacher = true;
    } else {
      this.dashboard = '/studashboard';
    }
    /* Modify Secret Area*/
    this.modifySecretForm = this.fb.group({
      username: [this.user.getUsername(), Validators.pattern('^[a-zA-Z0-9-_]{5,20}')],
      password: ['', Validators.pattern('^[a-zA-Z0-9-_]{5,20}')],
      confirmPassword: ['', Validators.pattern('^[a-zA-Z0-9-_]{5,20}')],
      rememberMe: [true]
    });

    this.updateNavData();
    // console.log($('a[.navbar]').attr('href', 'studashboard'))
  }

  async updateNavData() {
    // clear student array
    this.navItems[2].children.length = 0;
    this.defaultLayoutService.getNavData().subscribe(response => {
      console.log('test' + JSON.stringify(response));
      this.navData = response.allUsersCommitRecord.sort(function (a, b) {
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

  confirmInputOnChange() {
    const password = this.modifySecretForm.value.password;
    const confirmPassword = this.modifySecretForm.value.confirmPassword;

    if (password.length >= 8) {
      $('#password').addClass('is-valid').removeClass('is-invalid');
      $('#password-invalid-feedbacks').hide();
    } else {
      $('#password').addClass('is-invalid').removeClass('is-valid');
      $('#password-invalid-feedbacks').show();
    }
    if (confirmPassword !== '') {
      if (password === confirmPassword) {
        $('#confirmPassword').addClass('is-valid').removeClass('is-invalid');
        $('#confirmPassword-invalid-feedbacks').hide();
      } else {
        $('#confirmPassword').addClass('is-invalid').removeClass('is-valid');
        $('#confirmPassword-invalid-feedbacks').show();
      }
    }
  }

  modifySecret() {
    console.log('modifySecret');
  }

  toggleDropdown($event: MouseEvent): void {
    $event.preventDefault();
    $event.stopPropagation();
    this.status.isOpen = !this.status.isOpen;
  }

  change(value: boolean): void {
    this.status.isOpen = value;
  }


  ngOnDestroy(): void {
    this.changes.disconnect();
    this.status.isOpen = false;
  }
}
