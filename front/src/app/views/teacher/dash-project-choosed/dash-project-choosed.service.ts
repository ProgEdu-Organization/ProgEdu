import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class DashProjectChoosedService {
  COMMIT_RECORD_DETAIL = environment.SERVER_URL + '/ProgEdu/webapi/commits/commitRecords';
  ALL_ASSIGNMENT_API = environment.SERVER_URL + '/ProgEdu/webapi/assignment/getAssignment?assignmentName=HW4';
  constructor(private http: HttpClient) { }

  getCommitDetail(assignmentName, username): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName);
    return this.http.get<any>(this.COMMIT_RECORD_DETAIL, { params });
  }

  getAssignment(assignmentName: string): Observable<any> {
    const params = new HttpParams()
      .set('assignmentName', assignmentName);
    return this.http.get<any>(this.ALL_ASSIGNMENT_API, { params });
  }

  getFeedback(assignmentName: string, username: string, commitNumber): Observable<any> {
    const params = new HttpParams()
      .set('assignmentName', assignmentName)
      .set('username', username)
      .set('commitNumber', commitNumber);
    return this.http.get<any>(this.ALL_ASSIGNMENT_API, { params });
  }
}
