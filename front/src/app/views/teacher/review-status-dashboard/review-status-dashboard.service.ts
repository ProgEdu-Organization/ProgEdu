import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';


@Injectable({
  providedIn: 'root'
})
export class ReviewStatusDashboardService {

  ALL_COMMIT_API = environment.SERVER_URL + '/webapi/peerReview/status/allUsers';
  ALL_ASSIGNMENT_API = environment.SERVER_URL + '/webapi/assignment/peerReview/allAssignment';
  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getAllStudentCommitRecord(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.ALL_COMMIT_API);
  }

  getAllAssignments(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.ALL_ASSIGNMENT_API);
  }

}
