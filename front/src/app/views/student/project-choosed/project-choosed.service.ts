import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProjectChoosedService {
  COMMIT_RECORD_DETAIL = environment.SERVER_URL + '/ProgEdu/webapi/commits/commitRecords';
  ASSIGNMENT_API = environment.SERVER_URL + '/ProgEdu/webapi/assignment/getAssignment';
  GITLAB_URL_API = environment.SERVER_URL + '/ProgEdu/webapi/commits/gitLab';
  FEEDBACK_API = environment.SERVER_URL + '/ProgEdu/webapi/commits/feedback';

  constructor(private http: HttpClient) { }

  getCommitDetail(assignmentName: string, username: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName);
    return this.http.get<any>(this.COMMIT_RECORD_DETAIL, { params });
  }

  getFeedback(assignmentName: string, username: string, commitNumber: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName)
      .set('number', commitNumber);
    return this.http.get<any>(this.FEEDBACK_API, { params });
  }

  getAssignment(assignmentName: string): Observable<any> {
    const params = new HttpParams()
      .set('assignmentName', assignmentName);
    return this.http.get<any>(this.ASSIGNMENT_API, { params });
  }

  getGitAssignmentURL(assignmentName: string, username: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName);
    return this.http.get<any>(this.GITLAB_URL_API, { params });
  }

}
