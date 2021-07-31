import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';

import { CommitRecordAPI } from '../../../api/CommitRecordAPI';

@Injectable({
  providedIn: 'root'
})
export class ProjectChoosedService {
  GITLAB_URL_API = CommitRecordAPI.getGitLabURL;
  SCREENSHOT_API = environment.SERVER_URL + '/publicApi/groups/commits/screenshot/getScreenshotURL';

  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getCommitResult(groupName: string, projectName: string): Observable<any> {
    const GROUP_COMMITS_RESULT_API: string = environment.SERVER_URL + `/webapi/groups/${groupName}/projects/${projectName}/commits`;
    return this.addJwtTokenHttpClient.get(GROUP_COMMITS_RESULT_API);
  }

  getPartCommitResult(groupName: string, projectName: string, currentPage: string): Observable<any> {
    const GROUP_PART_COMMITS_RESULT_API: string = environment.SERVER_URL + `/webapi/groups/${groupName}/projects/${projectName}/partCommits/${currentPage}`;
    return this.addJwtTokenHttpClient.get(GROUP_PART_COMMITS_RESULT_API);
  }

  getGroup(groupName): Observable<any> {
    const GROUP_MEMBER_API: string = environment.SERVER_URL + `/webapi/groups/${groupName}`;
    return this.addJwtTokenHttpClient.get(GROUP_MEMBER_API);
  }

  getFeedback(groupName: string, projectName: string, commitNumber: string): Observable<any> {
    const GROUP_FEEDBACK_API: string = environment.SERVER_URL + `/webapi/groups/${groupName}/projects/${projectName}/feedback/${commitNumber}`;
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
