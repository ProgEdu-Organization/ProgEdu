import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FormGroup } from '@angular/forms';
import { environment } from '../../../../environments/environment';

const createProjectOptions = ({
  headers: new HttpHeaders(
  )
});
@Injectable({
  providedIn: 'root'
})

export class CreateGroupService {
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
    console.log(params.get('member'));

    return this.http.post<any>(this.CREATE_PROJECT, params, createProjectOptions);
  }

  getAllGroup(): Observable<any> {
    return this.http.get<any>(this.GET_GROUP_MEMBER_API);
  }
}
