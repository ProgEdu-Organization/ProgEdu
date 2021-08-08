import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';
import { AssignmentAPI } from '../../../api/AssignmentAPI';
import { CommitRecordAPI } from '../../../api/CommitRecordAPI';
import { PeerReviewAPI } from '../../../api/PeerReviewAPI';
import { PublicAPI } from '../../../api/PublicAPI';

@Injectable({
  providedIn: 'root'
})
export class ReviewAssignmentChooseService {

  COMMIT_RECORD_DETAIL = CommitRecordAPI.getCommitRecord;
  ASSIGNMENT_API = AssignmentAPI.getAssignmentDescription;
  GITLAB_URL_API = CommitRecordAPI.getGitLabURL;
  FEEDBACK_API = CommitRecordAPI.getFeedback;
  SCREENSHOT_API = PublicAPI.getScreenshotURL;
  REVIEW_FEEDBACK_API = PeerReviewAPI.getReviewedRecordDetail;
  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getCommitDetail(assignmentName: string, username: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName);
    return this.addJwtTokenHttpClient.get(this.COMMIT_RECORD_DETAIL, { params });
  }

  getFeedback(assignmentName: string, username: string, commitNumber: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName)
      .set('number', commitNumber);
    return this.addJwtTokenHttpClient.get(this.FEEDBACK_API, { params });
  }

  getAssignment(assignmentName: string): Observable<any> {
    const params = new HttpParams()
      .set('assignmentName', assignmentName);
    return this.addJwtTokenHttpClient.get(this.ASSIGNMENT_API, { params });
  }

  getGitAssignmentURL(assignmentName: string, username: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName);
    return this.addJwtTokenHttpClient.get(this.GITLAB_URL_API, { params });
  }

  getScreenshotUrls(username: string, assignmentName: string, commitNumber: number): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName)
      .set('commitNumber', commitNumber.toString());
    return this.addJwtTokenHttpClient.get(this.SCREENSHOT_API, { params });
  }

  getReviewFeedback(assignmentName: string, username: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName);
    return this.addJwtTokenHttpClient.get(this.REVIEW_FEEDBACK_API, { params });
  }
}
