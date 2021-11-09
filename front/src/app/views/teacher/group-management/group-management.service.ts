import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';
import { UserAPI } from '../../../api/UserAPI';
import { GroupsAPI } from '../../../api/GroupsAPI';

const createProjectOptions = ({
  headers: new HttpHeaders(
  )
});
@Injectable({
  providedIn: 'root'
})

export class GroupManagementService {
  GET_USERS_API = UserAPI.getUsers;
  CREATE_PROJECT = environment.SERVER_URL + '/webapi/groups/create'; // Todo 這好像根本沒用到
  GET_GROUP_MEMBER_API = GroupsAPI.getAllGroup;
  constructor(private http: HttpClient, private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getAllUserData(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.GET_USERS_API);
  }

  // Todo 以下根本沒用到
  createProject(name: string, projectName: string, projectType: string, leader: string, members: Array<string>): Observable<any> {

    let params = new HttpParams()
      .append('name', name)
      .append('projectName', projectName)
      .append('projectType', projectType)
      .append('leader', leader);
    for (const member of members) {
      params = params.append('member', member);
    }

    return this.addJwtTokenHttpClient.post(this.CREATE_PROJECT, params, createProjectOptions);
  }

  getAllGroup(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.GET_GROUP_MEMBER_API);
  }

  deleteGroup(groupName: string): Observable<any> {
    const DELETE_GROUP_API = GroupsAPI.removeGroup(groupName);
    return this.addJwtTokenHttpClient.delete(DELETE_GROUP_API);
  }
}
