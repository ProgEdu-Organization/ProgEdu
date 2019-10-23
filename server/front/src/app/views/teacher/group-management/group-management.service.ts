import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

const createProjectOptions = ({
  headers: new HttpHeaders(
  )
});
@Injectable({
  providedIn: 'root'
})

export class GroupManagementService {
  GET_USERS_API = environment.SERVER_URL + '/webapi/user/getUsers';
  CREATE_PROJECT = environment.SERVER_URL + '/webapi/groups/create';
  GET_GROUP_MEMBER_API = environment.SERVER_URL + '/webapi/groups';
  constructor(private http: HttpClient) { }

  getAllUserData(): Observable<any> {
    return this.http.get<any>(this.GET_USERS_API);

  }

  createProject(name: string, projectName: string, projectType: string, leader: string, members: Array<string>): Observable<any> {

    let params = new HttpParams()
      .append('name', name)
      .append('projectName', projectName)
      .append('projectType', projectType)
      .append('leader', leader);
    for (const member of members) {
      params = params.append('member', member);
    }

    return this.http.post<any>(this.CREATE_PROJECT, params, createProjectOptions);
  }

  getAllGroup(): Observable<any> {
    return this.http.get<any>(this.GET_GROUP_MEMBER_API);
  }

  deleteGroup(groupName: string): Observable<any> {
    const DELETE_GROUP_API = environment.SERVER_URL + `/webapi/groups/${groupName}`;
    return this.http.delete<any>(DELETE_GROUP_API);
  }
}
