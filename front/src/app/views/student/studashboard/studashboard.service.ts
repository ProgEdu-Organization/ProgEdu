import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StudashboardService {
  ALL_COMMIT_API = '/ProgEdu/webapi/commits/all';
  constructor(private http: HttpClient) { }

  getAllStudentData(): Observable<any> {
    return this.http.get<any>(this.ALL_COMMIT_API);
  }
}
