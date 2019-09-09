import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class DashProjectChoosedService {
  COMMIT_RECORD_DETAIL = environment.SERVER_URL + '/ProgEdu/webapi/commits/commitRecords';
  ASSIGNMENT_API = environment.SERVER_URL + '/ProgEdu/webapi/assignment/getAssignment';
  FEEDBACK_API = environment.SERVER_URL + '/ProgEdu/webapi/commits/feedback';
  SCREENSHOt_API = environment.SERVER_URL + '/ProgEdu/webapi/commits/screenshot/getScreenshotURL';
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
    return this.http.get<any>(this.ASSIGNMENT_API, { params });
  }

  getFeedback(assignmentName: string, username: string, commitNumber: number): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName)
      .set('number', commitNumber.toString());
    return this.http.get<any>(this.FEEDBACK_API, { params });
  }

  getScreenshotUrls(username: string, assignmentName: string, commitNumber: number): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName)
      .set('commitNumber', commitNumber.toString());
    return this.http.get<any>(this.SCREENSHOt_API, { params });
  }
}
