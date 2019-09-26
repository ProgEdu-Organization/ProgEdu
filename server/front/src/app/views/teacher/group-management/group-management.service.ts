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

export class GroupManagementService {
  GET_USERS_API = environment.SERVER_URL + '/webapi/user/getUsers';
  CREATE_PROJECT = environment.SERVER_URL + '/webapi/groups/create';
  constructor(private http: HttpClient) { }

  getAllUserData(): Observable<any> {
    return this.http.get<any>(this.GET_USERS_API);

  }

  createProject(name: string, projectName: string, projectType: string, leader: string, members: Array<string>): Observable<any> {

    const formData = new FormData();

    formData.append('name', name);
    formData.append('projectName', projectName);
    formData.append('projectType', projectType);
    formData.append('leader', leader);
    for (const member of members) {
      formData.append('member', member);
    }
    console.log(formData.get('member'));

    return this.http.post<any>(this.CREATE_PROJECT, formData, createProjectOptions);

  }
}
