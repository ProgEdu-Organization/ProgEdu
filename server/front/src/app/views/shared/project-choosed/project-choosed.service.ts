import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProjectChoosedService {
  GROUP_COMMITS = environment.SERVER_URL + '/webapi/groups/commits';
  constructor(private http: HttpClient) { }

  getGroupCommits(): Observable<any> {
    return this.http.get(this.GROUP_COMMITS);
  }

}
