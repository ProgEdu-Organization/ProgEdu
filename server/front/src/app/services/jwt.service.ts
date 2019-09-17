import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';


const helper = new JwtHelperService();

@Injectable({
  providedIn: 'root'
})
export class JwtService {

  public getDecodedToken() {
    return helper.decodeToken(this.getToken());
  }

  public getExpirationDate() {
    return helper.getTokenExpirationDate(this.getToken());
  }

  public isTokenExpired() {
    return helper.isTokenExpired(this.getToken());
  }

  public getToken() {
    return localStorage.getItem('token');
  }

  public removeToken() {
    localStorage.removeItem('token');
  }

  public setToken(token) {
    return localStorage.setItem('token', token);
  }

}
