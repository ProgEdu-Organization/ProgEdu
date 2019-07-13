import { Injectable, RootRenderer, SystemJsNgModuleLoader } from '@angular/core';
import { HttpService } from './http.service';
import { HttpParams } from '@angular/common/http';
import { JwtModule } from '@auth0/angular-jwt';
import { HttpClientModule } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LoginAuthService {

  SERVER_URL: string = 'http://140.134.26.77:8080/ProgEdu/LoginAuth';
  TOKEN_API_URL: string = 'http://140.134.26.77:8080/ProgEdu/webapi/auth/login';
  constructor(private http: HttpService) { }
  // 是否登录
  public async Login(username, password) {
    let params = new HttpParams();
    params = params.append('username', username);
    params = params.append('password', password);
    return await this.http.postData(this.SERVER_URL, params);
  }

  private tokenGetter() {
    return localStorage.getItem('token');
  }

  public async isLoginByTeacher() {
    const token = this.tokenGetter();
    console.log('token is:' + token);
    let params = new HttpParams();
    params = params.append('token', token);
    return await this.http.postData(this.TOKEN_API_URL, params);
  }

  public async  isLoginByStudent() {
    const token = this.tokenGetter();
    console.log('token is:' + token);
    let params = new HttpParams();
    params = params.append('token', token);
    return await this.http.postData(this.TOKEN_API_URL, params);
  }

  private isTokenExpired(token: string): boolean {
    return true;
  }
}
