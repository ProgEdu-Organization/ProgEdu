import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { LoginAuthService } from './login-auth.service';
import { JwtService } from './jwt.service';

@Injectable({
  providedIn: 'root'
})
export class CanActiveStudentService implements CanActivate {
  constructor(public router: Router, private loginAuth: LoginAuthService, private jwtService: JwtService) { }

  canActivate(): Promise<boolean> | boolean {
    return new Promise((resolve, reject) => {

      if (this.loginAuth.isLoginByStudent()) {
        resolve(true);
      } else {
        this.jwtService.removeToken();
        this.router.navigate(['login']);
        resolve(false);
      }
    });
  }
}
