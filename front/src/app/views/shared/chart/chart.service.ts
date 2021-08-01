import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';

import { ChartAPI } from '../../../api/ChartAPI';

@Injectable({
  providedIn: 'root'
})
export class ChartService {
  ALL_COMMIT_RECORD = ChartAPI.getAllCommitRecord;
  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getAllCommits(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.ALL_COMMIT_RECORD);
  }

}
