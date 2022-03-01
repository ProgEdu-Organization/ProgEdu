import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';

@Injectable({
  providedIn: 'root'
})
export class ReviewCommitRecordService {

  REVIEW_DETAIL_PAGE_API = environment.NEW_SERVER_URL + '/peerReview/record/detail/page'; // Todo 沒用到

  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getReviewPageDetail(assignmentName: string, username: string, reviewId: string, round: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName)
      .set('reviewId', reviewId)
      .set('round', round);
    return this.addJwtTokenHttpClient.get(this.REVIEW_DETAIL_PAGE_API, { params }  );
  }

}
