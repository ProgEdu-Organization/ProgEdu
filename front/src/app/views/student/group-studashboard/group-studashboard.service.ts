import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';

import { GroupCommitRecordAPI } from '../../../api/GroupCommitRecordAPI'

@Injectable({
  providedIn: 'root'
})
export class GroupStudashboardService {

  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getAllCommits(username: string): Observable<any> {
    const COMMITS_API = GroupCommitRecordAPI.getCommitRecordByUsername(username);
    return this.addJwtTokenHttpClient.get(COMMITS_API);
  }
}
