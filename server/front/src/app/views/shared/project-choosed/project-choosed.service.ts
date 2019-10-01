import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProjectChoosedService {
  GITLAB_URL_API = environment.SERVER_URL + '/webapi/commits/gitLab';

  constructor(private http: HttpClient) { }

  getCommitResult(groupName: string, projectName: string): Observable<any> {
    const GROUP_COMMITS_RESULT_API: string = environment.SERVER_URL + `/webapi/groups/${groupName}/projects/${projectName}/commits`;
    return this.http.get<any>(GROUP_COMMITS_RESULT_API);
  }

  getGroupMembers(groupName): Observable<any> {
    const GROUP_MEMBER_API: string = environment.SERVER_URL + `/webapi/groups/${groupName}`;
    return this.http.get<any>(GROUP_MEMBER_API);
  }

  getFeedback(groupName: string, projectName: string, commitNumber: string): Observable<any> {
    const GROUP_FEEDBACK_API: string = environment.SERVER_URL + `/webapi/groups/${groupName}/projects/${projectName}/feedback/${commitNumber}`;
    return this.http.get<any>(GROUP_FEEDBACK_API);
  }

  getProjectUrl(groupName, projectName): Observable<any> {
    const params = new HttpParams()
      .set('username', groupName)
      .set('assignmentName', projectName);
    return this.http.get<any>(this.GITLAB_URL_API, { params });
  }

}
