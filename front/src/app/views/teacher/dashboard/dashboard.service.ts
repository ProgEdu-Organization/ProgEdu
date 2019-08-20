import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})


export class DashboardService {
  ALL_COMMIT_API = 'webapi/commits/all';
  constructor(private http: HttpClient) { }

  getAllStudentData(): Observable<any> {
    return this.http.get<any>(this.ALL_COMMIT_API);
  }

}
