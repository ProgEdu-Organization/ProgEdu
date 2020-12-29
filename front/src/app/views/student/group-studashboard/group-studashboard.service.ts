import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GroupStudashboardService {

  constructor(private http: HttpClient) { }

  getAllCommits(username: string): Observable<any> {
    const COMMITS_API = environment.SERVER_URL + `/webapi/groups/${username}/commits`;
    return this.http.get(COMMITS_API);
  }
}
