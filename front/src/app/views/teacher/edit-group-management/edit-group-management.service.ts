import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';
import { UserAPI } from '../../../api/UserAPI';
import { GroupsAPI } from '../../../api/GroupsAPI';

const editGroupOptions = ({
  headers: new HttpHeaders(
  )
});

@Injectable({
  providedIn: 'root'
})
export class EditGroupManagementService {
  GET_USERS_API = UserAPI.getUsers;
  constructor(private http: HttpClient, private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getGroup(groupName: string): Observable<any> {
    const GET_GROUP_API = GroupsAPI.getGroup(groupName);;
    return this.addJwtTokenHttpClient.get(GET_GROUP_API);
  }

  editGroupLeader(groupName: string, leader: string) {
    const EDIT_LEADER_API = GroupsAPI.updateLeader(groupName, leader); // Todo 用陣列不好
    return this.addJwtTokenHttpClient.put(EDIT_LEADER_API, "");
  }

  deleteGroupMember(groupName: string, member: string) {
    const EDIT_LEADER_API = GroupsAPI.removeMember(groupName, member); // Todo 用陣列不好
    return this.addJwtTokenHttpClient.delete(EDIT_LEADER_API);
  }

  getAllUser() {
    return this.addJwtTokenHttpClient.get(this.GET_USERS_API);
  }

  addGroupMemeber(groupName: string, username: string) {
    const ADD_MEMBER_API = GroupsAPI.addMembers(groupName);
    const params = new HttpParams()
      .append('members', username);

    return this.addJwtTokenHttpClient.post(ADD_MEMBER_API, params);
  }
}
