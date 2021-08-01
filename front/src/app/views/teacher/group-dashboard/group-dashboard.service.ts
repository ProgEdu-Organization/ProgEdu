import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';
import { GroupCommitRecordAPI } from '../../../api/GroupCommitRecordAPI';

@Injectable({
  providedIn: 'root'
})
export class GroupDashboardService {
  private COMMITS_API = GroupCommitRecordAPI.getAllGroupCommitRecord;
  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getAllCommits(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.COMMITS_API);
  }
}
