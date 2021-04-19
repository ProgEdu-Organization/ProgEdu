import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { JwtService } from './jwt.service';


@Injectable({
  providedIn: 'root'
})
export class AddJwtTokenHttpClient {
  constructor(private  http: HttpClient) { }

  get(url) {
    const jwtService = new JwtService();
    const params = new HttpParams().set('token', jwtService.getToken());
    return this.http.get<any>(url, {params});
  };
}
