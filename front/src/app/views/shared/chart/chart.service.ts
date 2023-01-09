import {Injectable} from '@angular/core';
import {HttpClient, HttpParams, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../../environments/environment';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';

import {ChartAPI} from '../../../api/ChartAPI';
import {UserAPI} from '../../../api/UserAPI';
import {CommitRecordAPI} from '../../../api/CommitRecordAPI';
import {AssignmentAPI} from '../../../api/AssignmentAPI';
import {PeerReviewAPI} from '../../../api/PeerReviewAPI';
import {ScoreAPI} from '../../../api/ScoreAPI';
import {Category} from '../../teacher/review-metrics-management/Category';
import {CategoryMetricsAPI} from '../../../api/CategoryMetricsAPI';

@Injectable({
  providedIn: 'root'
})
export class ChartService {
  ALL_COMMIT_RECORD = ChartAPI.getAllCommitRecord;
  ALL_USER = UserAPI.getUsers;
  PART_COMMIT_RECORD = CommitRecordAPI.getPartCommitRecord;
  COMMIT_RECORDS = CommitRecordAPI.getCommitRecord;
  PEER_REVIEW_ALL_ASSIGNMENT = AssignmentAPI.getAllPeerReviewAssignment;
  PEER_REVIEW_STATUS_ROUND_ALL_USER = PeerReviewAPI.getAllReviewRoundStatus;
  ALL_USERS_SCORE_API = ScoreAPI.getAllUserScore;
  ALL_AVERAGE_SCORE_API = ScoreAPI.getAvgScores;
  REVIEW_FEEDBACK_API = PeerReviewAPI.getReviewedRecordDetail;
  REVIEW_DETAIL_PAGE_API = PeerReviewAPI.getReviewPageDetail;
  GET_ALL_CATEGORY_API = CategoryMetricsAPI.getCategory;
  GET_METRICS_API = CategoryMetricsAPI.getMetrics;

  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) {
  }

  getAllCommits(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.ALL_COMMIT_RECORD);
  }

  // 叫API
  getAllUser(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.ALL_USER);
  }

  getPartCommitDetail(username: string, assignmentName: string, currentPage: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName)
      .set('currentPage', currentPage);
    return this.addJwtTokenHttpClient.get(this.PART_COMMIT_RECORD, { params });
  }

  getCommitRecord(username: string, assignmentName: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName);
    return this.addJwtTokenHttpClient.get(this.COMMIT_RECORDS, {params});
  }

  getAllPeerReviewAssignment(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.PEER_REVIEW_ALL_ASSIGNMENT);
  }

  getPeerReviewStatusRoundAllUser(assignmentName: string): Observable<any> {
    const params = new HttpParams()
      .set('assignmentName', assignmentName);
    return this.addJwtTokenHttpClient.get(this.PEER_REVIEW_STATUS_ROUND_ALL_USER, {params});
  }
  getAllUserScore(assignmentName: string): Observable<any> {
    const params = new HttpParams().set('assignmentName', assignmentName);
    return this.addJwtTokenHttpClient.get(this.ALL_USERS_SCORE_API, {params});
  }
  getAllAvgScore(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.ALL_AVERAGE_SCORE_API);
  }
  getReviewFeedback(assignmentName: string, username: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName);
    return this.addJwtTokenHttpClient.get(this.REVIEW_FEEDBACK_API, { params });
  }
  getReviewPageDetail(username: string, assignmentName: string, reviewId: string, round: string) {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName)
      .set('reviewId', reviewId)
      .set('round', round);
    return this.addJwtTokenHttpClient.get(this.REVIEW_DETAIL_PAGE_API, { params });
  }
  getAllCategory(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.GET_ALL_CATEGORY_API);
  }
  getMetrics(category: Category): Observable<any> {
    const params = new HttpParams().
    set('category', category.id.toString());
    return this.addJwtTokenHttpClient.get(this.GET_METRICS_API , { params });
  }
}
