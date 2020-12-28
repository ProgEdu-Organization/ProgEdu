import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReviewDashboardService {

  ALL_COMMIT_API = environment.SERVER_URL + '/webapi/peerReview/record/allUsers';
  ALL_ASSIGNMENT_API = environment.SERVER_URL + '/webapi/assignment/peerReview/allAssignment';
  constructor(private http: HttpClient) { }

  getAllStudentCommitRecord(): Observable<any> {
    return this.http.get<any>(this.ALL_COMMIT_API);
  }

  getAllAssignments(): Observable<any> {
    return this.http.get<any>(this.ALL_ASSIGNMENT_API);
  }

}
