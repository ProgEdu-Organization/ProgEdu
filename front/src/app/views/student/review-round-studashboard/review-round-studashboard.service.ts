import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';
import { AssignmentAPI } from '../../../api/AssignmentAPI';
import { PeerReviewAPI } from '../../../api/PeerReviewAPI';

@Injectable({
  providedIn: 'root'
})

export class ReviewRoundStudashboardService {

  ALL_COMMIT_API = PeerReviewAPI.getReviewStatus;
  ALL_ASSIGNMENT_API = AssignmentAPI.getAllPeerReviewAssignment;
  ALL_STATUS_DETAIL_API = PeerReviewAPI.getReviewStatusDetail;
  ALL_REVIEW_ROUND_API = PeerReviewAPI.getOneUserReviewedRoundStatus;
  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getStudentCommitRecord(username: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username);
    return this.addJwtTokenHttpClient.get(this.ALL_COMMIT_API, { params });
  }

  getAllAssignments(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.ALL_ASSIGNMENT_API);
  }

  getReviewDetail(username: string, assignmentName: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName);
    return this.addJwtTokenHttpClient.get(this.ALL_STATUS_DETAIL_API, { params });
  }

  getReviewRoundStatus(username: string, assignmentName: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName);
      return this.addJwtTokenHttpClient.get(this.ALL_REVIEW_ROUND_API, { params });
  }
}