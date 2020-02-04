import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class ChartService {
  ALL_COMMIT_RECORD = environment.SERVER_URL + '/webapi/chart/allCommitRecord';
  constructor(private http: HttpClient) { }

  getAllCommits(): Observable<any> {
    return this.http.get<any>(this.ALL_COMMIT_RECORD);
  }

}
