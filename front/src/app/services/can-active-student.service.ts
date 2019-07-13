import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { LoginAuthService } from './login-auth.service';

@Injectable({
  providedIn: 'root'
})
export class CanActiveStudentService implements CanActivate {
  constructor(public router: Router, private loginAuth: LoginAuthService) { }
  canActivate(): boolean {
    if (this.loginAuth.isLoginByTeacher()) {
      return true;
    }
    return false;
  }
}
