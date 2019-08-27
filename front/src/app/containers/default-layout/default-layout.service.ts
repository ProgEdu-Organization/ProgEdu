import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DefaultLayoutService {
  ALL_COMMIT_API = environment.SERVER_URL + '/ProgEdu/webapi/commits/allUsers';
  MODIFY_API = environment.SERVER_URL + '/ProgEdu/webapi/commits/allUsers';
  constructor(private http: HttpClient) { }

  getNavData(): Observable<any> {
    return this.http.get<any>(this.ALL_COMMIT_API);
  }

  modifySecret(): Observable<any> {
    return this.http.get<any>(this.MODIFY_API);
  }
}
