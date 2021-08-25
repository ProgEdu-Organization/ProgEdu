import { Injectable, RootRenderer, SystemJsNgModuleLoader, ɵConsole } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { JwtService } from './jwt.service';
import { environment } from '../../environments/environment';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import {AddJwtTokenHttpClient} from './add-jwt-token.service';

import { LoginAPI } from '../api/LoginApi';


@Injectable({
  providedIn: 'root'
})
export class LoginAuthService {

  LOGIN_URL: string = LoginAPI.login;
  // AUTH_URL: string = LoginAPI.checkLogin; // Todo 待刪

  constructor(private http: HttpClient, private jwtService: JwtService, 
              private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  // 是否登录
  public Login(username, password): Observable<any> {
    let params = new HttpParams();
    params = params.append('username', username);
    params = params.append('password', password);

    const options = ({
      headers: new HttpHeaders({
        'Content-Type': 'application/x-www-form-urlencoded',
      })
    });

    return this.http.post<any>(this.LOGIN_URL, params, options);
  }

  public isLoginByTeacher(): boolean {

    let jwtInfo = this.jwtService.getDecodedToken();
    if (jwtInfo.authorities.includes("ROLE_TEACHER")) {
      return true;
    } else {
      return false;
    }
  }

  public isLoginByStudent(): boolean {

    let jwtInfo = this.jwtService.getDecodedToken();
    if (jwtInfo.authorities.includes("ROLE_STUDENT")) {
      return true;
    } else {
      return false;
    }
  }

  public logout() {
    return this.jwtService.removeToken();
  }

}
