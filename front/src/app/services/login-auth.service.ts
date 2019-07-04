import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions } from '@angular/http';
import { HttpService } from './http.service';
@Injectable({
  providedIn: 'root'
})
export class LoginAuthService {

  constructor(private http: HttpService) { }
  // 是否登录
  public isLogin(): boolean {
    const token = localStorage.getItem('token');

    return !this.isTokenExpired(token);
  }

  private isTokenExpired(token: string): boolean {

    return false;
  }
}
