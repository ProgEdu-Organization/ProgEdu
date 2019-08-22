import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { LoginAuthService } from './login-auth.service';
import { JwtService } from './jwt.service';

@Injectable({
  providedIn: 'root'
})
export class CanActiveTeacherService implements CanActivate {
  constructor(public router: Router, private loginAuth: LoginAuthService, private jwtService: JwtService) { }

  canActivate(): Promise<boolean> | boolean {
    return new Promise((resolve) => {
      this.loginAuth.isLoginByTeacher().subscribe((response) => {
        console.log(response);
        if (response.isLogin && response.isTeacher) {
          console.log('canActive teacher');
          resolve(true);
        } else {
          this.jwtService.removeToken();
          console.log('not authenticated');
          this.router.navigate(['login']);
          resolve(false);
        }
      });
    });
  }
}
