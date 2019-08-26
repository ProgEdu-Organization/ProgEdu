import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class StudashboardService {
  ALL_COMMIT_API = environment.SERVER_URL + '/ProgEdu/webapi/commits/oneUser';
  constructor(private http: HttpClient) { }

  getStudentData(username: string): Observable<any> {
    console.log(this.ALL_COMMIT_API + `username = ${username}`);
    return this.http.get<any>(this.ALL_COMMIT_API + `?username=${username}`);
  }
}
