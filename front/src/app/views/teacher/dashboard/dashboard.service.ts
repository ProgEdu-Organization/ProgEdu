import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})


export class DashboardService {
  ALL_COMMIT_API = environment.SERVER_URL + '/ProgEdu/webapi/commits/all';
  constructor(private http: HttpClient) { }

  getAllStudentData(): Observable<any> {
    return this.http.get<any>(this.ALL_COMMIT_API);
  }

}
