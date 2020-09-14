import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewStatusAssignmentChooseService {

  ALL_STATUS_DETAIL_API = 'http://140.134.26.66:22000/webapi/peerReview/status/detail';
  REVIEW_METRICS_API = 'http://140.134.26.66:22000/webapi/peerReview/metrics';
  REVIEW_STATUS_DETAIL_PAGE_API = 'http://140.134.26.66:22000/webapi/peerReview/status/detail/page';
  CREATE_REVIEW_RECORD_API = 'http://140.134.26.62:8080/webapi/peerReview/create';
  constructor(private http: HttpClient) { }

  getReviewDetail(username: string, assignmentName: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName);
    return this.http.get<any>(this.ALL_STATUS_DETAIL_API, { params });
  }

  getReviewMetrics(assignmentName: string): Observable<any> {
    const params = new HttpParams()
      .set('assignmentName', assignmentName);
    return this.http.get<any>(this.REVIEW_METRICS_API, { params });
  }

  getReviewStatusPageDetail(assignmentName: string, username: string, userId: string, page: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName)
      .set('userId', userId)
      .set('page', page);
    return this.http.get<any>(this.REVIEW_STATUS_DETAIL_PAGE_API, { params }  );
  }

  createReviewRecord(username: string, reviewedName: string , assignmentName: string, reviewRecord: any): Observable<any> {
    const formData = new FormData();
    formData.append('username', username);
    formData.append('reviewedName', reviewedName);
    formData.append('assignmentName', assignmentName);
    formData.append('time', reviewRecord.allReviewRecord[0].time);
    formData.append('reviewRecord',  JSON.stringify(reviewRecord).toString());
    return this.http.post<any>(this.CREATE_REVIEW_RECORD_API, formData  );
  }

}
