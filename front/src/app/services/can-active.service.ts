import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { LoginAuthService } from './login-auth.service';

@Injectable({
  providedIn: 'root'
})
export class CanActiveService implements CanActivate {
  constructor(public router: Router, private loginAuth: LoginAuthService) { }

  canActivate(): boolean {
    if (!this.loginAuth.isLogin()) {
      this.router.navigate(['login']);
      return false;
    }
    return true;
  }
}
