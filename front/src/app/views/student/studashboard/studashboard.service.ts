import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class StudashboardService {
  ALL_COMMIT_API = environment.SERVER_URL + '/ProgEdu/webapi/commits/oneUser';
  ALL_ASSIGNMENT_API = environment.SERVER_URL + '/ProgEdu/webapi/assignment/getAllAssignments';
  constructor(private http: HttpClient) { }

  getStudentCommitRecord(username: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username);
    return this.http.get<any>(this.ALL_COMMIT_API, { params });
  }

  getAllAssignments(): Observable<any> {
    return this.http.get<any>(this.ALL_ASSIGNMENT_API);
  }
}
