import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { JwtService } from './jwt.service';


@Injectable({
  providedIn: 'root'
})
export class AddJwtTokenHttpClient {
  constructor(private  http: HttpClient) { }

  get(url, options?) {
    const jwtService = new JwtService();

    if(options == undefined) {

        const params = new HttpParams().set('token', jwtService.getToken());
        return this.http.get<any>(url, {params});

    } else if( options.params == undefined) {

        options.params = new HttpParams().set('token', jwtService.getToken());
        return this.http.get<any>(url, options);

    } else {
        options.params = options.params.append('token', jwtService.getToken());
        return this.http.get<any>(url, options);
    }
    
  };

  post(url, body, options?) {

    const jwtService = new JwtService();

    if(options == undefined) {

        const params = new HttpParams().set('token', jwtService.getToken());
        return this.http.post<any>(url, body, {params});

    } else if( options.params == undefined) {
        options.params = new HttpParams().set('token', jwtService.getToken());
        return this.http.post<any>(url, body, options);

    } else {
        options.params = options.params.append('token', jwtService.getToken());
        return this.http.post<any>(url, body, options);
    }
  }

  put(url, body, options?) {

    const jwtService = new JwtService();

    if(options == undefined) {

        const params = new HttpParams().set('token', jwtService.getToken());
        return this.http.put<any>(url, body, {params});

    } else if( options.params == undefined) {
        options.params = new HttpParams().set('token', jwtService.getToken());
        return this.http.put<any>(url, body, options);

    } else {
        options.params = options.params.append('token', jwtService.getToken());
        return this.http.put<any>(url, body, options);
    }
  }

  delete(url, options?) {

    const jwtService = new JwtService();

    if(options == undefined) {

        const params = new HttpParams().set('token', jwtService.getToken());
        return this.http.delete<any>(url, {params});

    } else if( options.params == undefined) {

        options.params = new HttpParams().set('token', jwtService.getToken());
        return this.http.delete<any>(url, options);

    } else {
        options.params = options.params.append('token', jwtService.getToken());
        return this.http.delete<any>(url, options);
    }

  }

}
