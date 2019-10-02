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

  constructor(private http: HttpClient) { }

  getGroup(groupName: string): Observable<any> {
    const GET_GROUP_API = environment.SERVER_URL + `/webapi/groups/${groupName}`;
    return this.http.get<any>(GET_GROUP_API);
  }

  editGroupLeader(groupName: string, leader: string) {
    const EDIT_LEADER_API = environment.SERVER_URL + `/webapi/groups/${groupName}/members/${leader[0]}`;
    return this.http.put(EDIT_LEADER_API, editGroupOptions);
  }
}
