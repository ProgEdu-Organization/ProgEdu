import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';
import { UserAPI } from '../../../api/UserAPI';
import { GroupsAPI } from '../../../api/GroupsAPI';
import { ScoreAPI } from '../../../api/ScoreAPI';

const editGroupOptions = ({
  headers: new HttpHeaders(
  )
});

@Injectable({
  providedIn: 'root'
})
export class EditScoreManagementService {
  ALL_USERS_SCORE_API = ScoreAPI.getAllUserScore;
  EDIT_USERS_SCORE_API = ScoreAPI.editUsersScore;
  constructor(private http: HttpClient, private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getAllUsersScore(assignmentName: string): Observable<any> {
    const params = new HttpParams()
      .set('assignmentName',assignmentName);
    return this.addJwtTokenHttpClient.get(this.ALL_USERS_SCORE_API, { params });
  }

  editUserScore(assignmentName: string, userName: string, score: string) {
    const params = new HttpParams()
      .append('assignmentName', assignmentName)
      .append('userName', userName)
      .append('score', score.toString());

    return this.addJwtTokenHttpClient.post(this.EDIT_USERS_SCORE_API, params);
  }
  
}