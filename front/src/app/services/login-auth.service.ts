import { Injectable, RootRenderer, SystemJsNgModuleLoader } from '@angular/core';
import { HttpService } from './http.service';
import { HttpParams } from '@angular/common/http';
import { JwtService } from './jwt.service';

@Injectable({
  providedIn: 'root'
})
export class LoginAuthService {

  SERVER_URL: string = 'http://140.134.26.77:8080/ProgEdu/LoginAuth';
  TOKEN_API_URL: string = 'http://140.134.26.77:8080/ProgEdu/webapi/auth/login';
  constructor(private http: HttpService, private jwtService: JwtService) { }
  // 是否登录
  public async Login(username, password) {
    let params = new HttpParams();
    params = params.append('username', username);
    params = params.append('password', password);
    return await this.http.postData(this.SERVER_URL, params);
  }



  public async isLoginByTeacher() {
    const token = this.jwtService.getToken();
    let params = new HttpParams();
    params = params.append('token', token);
    return await this.http.postData(this.TOKEN_API_URL, params);
  }

  public async  isLoginByStudent() {
    const token = this.jwtService.getToken();
    let params = new HttpParams();
    params = params.append('token', token);
    return await this.http.postData(this.TOKEN_API_URL, params);
  }

  private isTokenExpired(token: string): boolean {
    return this.jwtService.isTokenExpired();
  }

  public logout() {
    return this.jwtService.removeToken();
  }

}
