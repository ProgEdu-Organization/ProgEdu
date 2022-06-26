import {Injectable} from '@angular/core';
import {HttpClient, HttpParams, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../../environments/environment';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';

import {ChartAPI} from '../../../api/ChartAPI';
import {UserAPI} from '../../../api/UserAPI';
import {CommitRecordAPI} from '../../../api/CommitRecordAPI';
import {AssignmentAPI} from '../../../api/AssignmentAPI';

@Injectable({
  providedIn: 'root'
})
export class ChartService {
  ALL_COMMIT_RECORD = ChartAPI.getAllCommitRecord;
  ALL_USER = UserAPI.getUsers;
  PART_COMMIT_RECORD = CommitRecordAPI.getPartCommitRecord;
  COMMIT_RECORDS = CommitRecordAPI.getCommitRecord;
  PEER_REVIEW_ALL_ASSIGNMENT = AssignmentAPI.getAllPeerReviewAssignment;

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
}
