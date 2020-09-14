import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class ReviewStatusDashboardService {

  ALL_COMMIT_API = 'http://140.134.26.66:22000' + '/webapi/peerReview/status/allUsers';
  ALL_ASSIGNMENT_API = 'http://140.134.26.66:22000/webapi/assignment/peerReview/allAssignment';
  constructor(private http: HttpClient) { }

  getAllStudentCommitRecord(): Observable<any> {
    return this.http.get<any>(this.ALL_COMMIT_API);
  }

  getAllAssignments(): Observable<any> {
    return this.http.get<any>(this.ALL_ASSIGNMENT_API);
  }

}
