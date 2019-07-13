import { Injectable, SystemJsNgModuleLoader } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { LoginAuthService } from './login-auth.service';
import { HttpService } from './http.service';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CanActiveTeacherService implements CanActivate {
  constructor(public router: Router, private loginAuth: LoginAuthService,
    private http: HttpService) { }

  canActivate(): Promise<boolean> | boolean {
    return new Promise((resolve) => {
      this.loginAuth.isLoginByTeacher().then((response) => {
        console.log('TEST' + response.isLogin);
        if (response.isLogin === true) {
          resolve(true);
        } else {
          localStorage.removeItem('token');
          console.log('not authenticated');
          this.router.navigate(['login']);
          resolve(false);
        }
      });
    });
  }
}
