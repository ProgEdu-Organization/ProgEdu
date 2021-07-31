import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';
import { AssignmentAPI } from '../../../api/AssignmentAPI';
import { CommitRecordAPI } from '../../../api/CommitRecordAPI';

@Injectable({
  providedIn: 'root'
})
export class StudashboardService {
  ALL_COMMIT_API = CommitRecordAPI.getAutoAssessment;
  ALL_ASSIGNMENT_API = AssignmentAPI.getAllAutoAssessment;
  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getStudentCommitRecord(username: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username);
    return this.addJwtTokenHttpClient.get(this.ALL_COMMIT_API, { params });
  }

  getAllAssignments(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.ALL_ASSIGNMENT_API);
  }

}
