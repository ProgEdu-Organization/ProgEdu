import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReviewStudashboardService {

  ALL_COMMIT_API = 'http://140.134.26.66:22000/webapi/peerReview/record/oneUser';
  ALL_ASSIGNMENT_API = 'http://140.134.26.66:22000/webapi/assignment/peerReview/allAssignment';
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
