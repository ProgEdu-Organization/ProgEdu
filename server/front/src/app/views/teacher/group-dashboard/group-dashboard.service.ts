import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GroupDashboardService {
  private COMMITS_API = environment.SERVER_URL + '/webapi/groups/commits';
  constructor(private http: HttpClient) { }

  getAllCommits(): Observable<any> {
    return this.http.get(this.COMMITS_API);
  }
}
