import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

const editGroupOptions = ({
  headers: new HttpHeaders(
  )
});

@Injectable({
  providedIn: 'root'
})
export class EditGroupManagementService {
  GET_USERS_API = environment.SERVER_URL + '/webapi/user/getUsers';
  constructor(private http: HttpClient) { }

  getGroup(groupName: string): Observable<any> {
    const GET_GROUP_API = environment.SERVER_URL + `/webapi/groups/${groupName}`;
    return this.http.get<any>(GET_GROUP_API);
  }

  editGroupLeader(groupName: string, leader: string) {
    const EDIT_LEADER_API = environment.SERVER_URL + `/webapi/groups/${groupName}/members/${leader[0]}`;
    return this.http.put(EDIT_LEADER_API, editGroupOptions);
  }

  deleteGroupMember(groupName: string, member: string) {
    const EDIT_LEADER_API = environment.SERVER_URL + `/webapi/groups/${groupName}/members/${member[0]}`;
    return this.http.delete(EDIT_LEADER_API);
  }

  getAllUser() {
    return this.http.get<any>(this.GET_USERS_API);
  }

  addGroupMemeber(groupName: string, username: string) {
    const ADD_MEMBER_API = environment.SERVER_URL + `/webapi/groups/${groupName}/members`;
    const params = new HttpParams()
      .append('members', username);

    return this.http.post(ADD_MEMBER_API, params);
  }
}
