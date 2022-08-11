import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AddJwtTokenHttpClient } from '../../../services/add-jwt-token.service';

import { ScoreAPI } from '../../../api/ScoreAPI';
import { UserAPI } from '../../../api/UserAPI';
import { ChartAPI } from '../../../api/ChartAPI';
import { PeerReviewAPI } from '../../../api/PeerReviewAPI';

const editGroupOptions = ({
  headers: new HttpHeaders(
  )
});

@Injectable({
  providedIn: 'root'
})

export class StudentChartService {
  ALL_COMMIT_RECORD = ChartAPI.getAllCommitRecord;
  PEER_REVIEW_RECORD_ONE_USER = PeerReviewAPI.getOneUserReviewedRecord;
  PEER_REVIEW_STATUS_ROUND_ALL_USER = PeerReviewAPI.getAllReviewRoundStatus;
  ALL_USERS_SCORE_API = ScoreAPI.getAllUserScore;
  ALL_AVERAGE_SCORE_API = ScoreAPI.getAvgScores;
  ALL_USER = UserAPI.getUsers;
  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getAllCommits(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.ALL_COMMIT_RECORD);
  }

  getPeerReviewStatusRoundAllUser(assignmentName: string): Observable<any> {
    const params = new HttpParams()
      .set('assignmentName', assignmentName);
    return this.addJwtTokenHttpClient.get(this.PEER_REVIEW_STATUS_ROUND_ALL_USER, {params});
  }

  getOneUserPeerReviewCommitRecord(username: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username);
    return this.addJwtTokenHttpClient.get(this.PEER_REVIEW_RECORD_ONE_USER, {params});
  }

  getAllUsersScore(assignmentName: string): Observable<any> {
    const params = new HttpParams()
      .set('assignmentName',assignmentName);
    return this.addJwtTokenHttpClient.get(this.ALL_USERS_SCORE_API, { params });
  }

  getAllAvgScore(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.ALL_AVERAGE_SCORE_API);
  }

  getAllUser(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.ALL_USER);
  }

}