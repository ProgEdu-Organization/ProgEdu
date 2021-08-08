import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';

import { CommitRecordAPI } from '../../../api/CommitRecordAPI';

import { GroupCommitRecordAPI } from '../../../api/GroupCommitRecordAPI';

import { GroupsAPI } from '../../../api/GroupsAPI';
import { PublicAPI } from '../../../api/PublicAPI';

@Injectable({
  providedIn: 'root'
})
export class ProjectChoosedService {
  GITLAB_URL_API = CommitRecordAPI.getGitLabURL;
  SCREENSHOT_API = PublicAPI.getScreenshotURLofGroup;

  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getCommitResult(groupName: string, projectName: string): Observable<any> {
    const GROUP_COMMITS_RESULT_API: string = GroupCommitRecordAPI.getCommitRecord(groupName, projectName);
    return this.addJwtTokenHttpClient.get(GROUP_COMMITS_RESULT_API);
  }

  getPartCommitResult(groupName: string, projectName: string, currentPage: string): Observable<any> {
    const GROUP_PART_COMMITS_RESULT_API: string = GroupCommitRecordAPI.getPartCommitRecord(groupName, projectName, currentPage);
    return this.addJwtTokenHttpClient.get(GROUP_PART_COMMITS_RESULT_API);
  }

  getGroup(groupName): Observable<any> {
    const GROUP_MEMBER_API: string = GroupsAPI.getGroup(groupName);
    return this.addJwtTokenHttpClient.get(GROUP_MEMBER_API);
  }

  getFeedback(groupName: string, projectName: string, commitNumber: string): Observable<any> {
    const GROUP_FEEDBACK_API: string = GroupCommitRecordAPI.getFeedback(groupName, projectName, commitNumber);
    return this.addJwtTokenHttpClient.get(GROUP_FEEDBACK_API);
  }

  getProjectUrl(groupName, projectName): Observable<any> {
    const params = new HttpParams()
      .set('username', groupName)
      .set('assignmentName', projectName);
    return this.addJwtTokenHttpClient.get(this.GITLAB_URL_API, { params });
  }

  getScreenshotUrls(groupName: string, projectName: string, commitNumber: number): Observable<any> {
    const params = new HttpParams()
      .set('groupName', groupName)
      .set('projectName', projectName)
      .set('commitNumber', commitNumber.toString());
    return this.addJwtTokenHttpClient.get(this.SCREENSHOT_API, { params });
  }

}
