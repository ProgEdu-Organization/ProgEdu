import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';
@Injectable({
  providedIn: 'root'
})
export class ReviewStatusStudashboardService {

  ALL_COMMIT_API = environment.SERVER_URL + '/webapi/peerReview/status/oneUser';
  ALL_ASSIGNMENT_API = environment.SERVER_URL + '/webapi/assignment/peerReview/allAssignment';
  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getStudentCommitRecord(username: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username);
    return this.addJwtTokenHttpClient.get(this.ALL_COMMIT_API, { params });
  }

  getAllAssignments(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.ALL_ASSIGNMENT_API);
  }
}
