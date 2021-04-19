import { Injectable, RootRenderer, SystemJsNgModuleLoader, ɵConsole } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { JwtService } from './jwt.service';
import { environment } from '../../environments/environment';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginAuthService {

  LOGIN_URL: string = environment.SERVER_URL + '/LoginAuth';
  AUTH_URL: string = environment.SERVER_URL + '/webapi/auth/login';
  constructor(private http: HttpClient, private jwtService: JwtService) { }
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

  public isLoginByTeacher(): Observable<any> {

    const token = this.jwtService.getToken();
    const params = new HttpParams().set('token', token);

    const options = ({
      headers: new HttpHeaders({
        'Content-Type': 'application/x-www-form-urlencoded',
      }),
      params: params
    });


    return this.http.post(this.AUTH_URL, null, options);
  }

  public isLoginByStudent(): Observable<any> {

    const token = this.jwtService.getToken();
    const params = new HttpParams().set('token', token);

    const options = ({
      headers: new HttpHeaders({
        'Content-Type': 'application/x-www-form-urlencoded',
      }),
      params: params
    });

    return this.http.post(this.AUTH_URL, null,  options);
  }

  public logout() {
    return this.jwtService.removeToken();
  }

}
