import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AddJwtTokenHttpClient } from '../../../services/add-jwt-token.service';

import { ScoreAPI } from '../../../api/ScoreAPI';
import {UserAPI} from '../../../api/UserAPI';

const editGroupOptions = ({
  headers: new HttpHeaders(
  )
});

@Injectable({
  providedIn: 'root'
})

export class StudentChartService {
  ALL_USERS_SCORE_API = ScoreAPI.getAllUserScore;
  ALL_AVERAGE_SCORE_API = ScoreAPI.getAvgScores;
  ALL_USER = UserAPI.getUsers;
  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

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