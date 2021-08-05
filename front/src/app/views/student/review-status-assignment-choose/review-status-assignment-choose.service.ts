import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';
import { PeerReviewAPI } from '../../../api/PeerReviewAPI';

@Injectable({
  providedIn: 'root'
})
export class ReviewStatusAssignmentChooseService {

  ALL_STATUS_DETAIL_API = PeerReviewAPI.getReviewStatusDetail;
  REVIEW_METRICS_API = PeerReviewAPI.getReviewMetrics;
  REVIEW_STATUS_DETAIL_PAGE_API = environment.SERVER_URL + '/webapi/peerReview/status/detail/page';
  CREATE_REVIEW_RECORD_API = environment.SERVER_URL + '/webapi/peerReview/create';
  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getReviewDetail(username: string, assignmentName: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName);
    return this.addJwtTokenHttpClient.get(this.ALL_STATUS_DETAIL_API, { params });
  }

  getReviewMetrics(assignmentName: string): Observable<any> {
    const params = new HttpParams()
      .set('assignmentName', assignmentName);
    return this.addJwtTokenHttpClient.get(this.REVIEW_METRICS_API, { params });
  }

  getReviewStatusPageDetail(assignmentName: string, username: string, userId: string, page: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName)
      .set('userId', userId)
      .set('page', page);
    return this.addJwtTokenHttpClient.get(this.REVIEW_STATUS_DETAIL_PAGE_API, { params }  );
  }

  createReviewRecord(username: string, reviewedName: string , assignmentName: string, reviewRecord: any): Observable<any> {
    const formData = new FormData();
    formData.append('username', username);
    formData.append('reviewedName', reviewedName);
    formData.append('assignmentName', assignmentName);
    formData.append('reviewRecord',  JSON.stringify(reviewRecord).toString());
    return this.addJwtTokenHttpClient.post(this.CREATE_REVIEW_RECORD_API, formData );
  }

}
