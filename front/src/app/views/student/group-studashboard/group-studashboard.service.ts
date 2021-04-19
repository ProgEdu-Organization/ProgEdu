import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';

@Injectable({
  providedIn: 'root'
})
export class GroupStudashboardService {

  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getAllCommits(username: string): Observable<any> {
    const COMMITS_API = environment.SERVER_URL + `/webapi/groups/${username}/commits`;
    return this.addJwtTokenHttpClient.get(COMMITS_API);
  }
}
