import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})


export class DashboardService {
  ALL_COMMIT_API = environment.SERVER_URL + '/ProgEdu/webapi/commits/allUsers';
  ALL_ASSIGNMENT_API = environment.SERVER_URL + '/ProgEdu/webapi/assignment/getAllAssignments';
  constructor(private http: HttpClient) { }

  getAllStudentCommitRecord(): Observable<any> {
    return this.http.get<any>(this.ALL_COMMIT_API);
  }

  getAllAssignments(): Observable<any> {
    return this.http.get<any>(this.ALL_ASSIGNMENT_API);
  }

}
