import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { JwtService } from './jwt.service';

@Injectable({
  providedIn: 'root'
})
export class AddJwtTokenHttpClient {
  // headers = new Headers();

  jwtToken: string;

  constructor(private  http: HttpClient, private jwtService: JwtService) { 
  }

  ngOnInit() {
  }

  get(url, options?) {
    let jwtToken = this.jwtService.getToken();
    const header = { Authorization: 'Bearer ' + jwtToken };

    if(options == undefined) {

      return this.http.get<any>(url, {headers: header});

    } else if( options.params == undefined) {

        options.headers = header;
        return this.http.get<any>(url, options);

    } else {

        options.headers = header;
        return this.http.get<any>(url, options);

    }

  };

  post(url, body, options?) {
    let jwtToken = this.jwtService.getToken();
    const header = { Authorization: 'Bearer ' + jwtToken };

    if(options == undefined) {

        return this.http.post<any>(url, body, {headers: header});

    } else if( options.params == undefined) {
        options.headers = header;
        return this.http.post<any>(url, body, options);

    } else {
        options.headers = header;
        return this.http.post<any>(url, body, options);
    }
  }

  put(url, body, options?) {
    let jwtToken = this.jwtService.getToken();
    const header = { Authorization: 'Bearer ' + jwtToken };

    if(options == undefined) {

        return this.http.put<any>(url, body, {headers: header});

    } else if( options.params == undefined) {
        options.headers = header;
        return this.http.put<any>(url, body, options);

    } else {
        options.headers = header;
        return this.http.put<any>(url, body, options);
    }
  }

  delete(url, options?) {
    let jwtToken = this.jwtService.getToken();
    const header = { Authorization: 'Bearer ' + jwtToken };
    if(options == undefined) {

        return this.http.delete<any>(url,  {headers: header});

    } else if( options.params == undefined) {
        options.headers = header;
        return this.http.delete<any>(url, options);

    } else {
        options.headers = header;
        return this.http.delete<any>(url, options);
    }

  }

}
