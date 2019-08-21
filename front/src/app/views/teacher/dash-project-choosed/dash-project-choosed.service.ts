import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class DashProjectChoosedService {
  ALL_PROJECT_API = environment.SERVER_URL + '/ProgEdu/webapi/jenkins/buildDetail?num=1&proName=WEB-HW5&userName=D0350510';
  constructor(private http: HttpClient) { }

  getCommitData(): Observable<any> {
    return this.http.get<any>(this.ALL_PROJECT_API);
  }
}
