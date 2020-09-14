import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReviewCommitRecordService {

  REVIEW_DETAIL_PAGE_API = 'http://140.134.26.66:22000/webapi/peerReview/record/detail/page';

  constructor(private http: HttpClient) { }

  getReviewPageDetail(assignmentName: string, username: string, reviewId: string, page: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName)
      .set('reviewId', reviewId)
      .set('page', page);
    return this.http.get<any>(this.REVIEW_DETAIL_PAGE_API, { params }  );
  }

}
