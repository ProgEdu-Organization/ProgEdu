import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DashProjectChoosedService {
  ALL_PROJECT_API = 'ProgEdu/webapi/jenkins/buildDetail?num=1&proName=WEB-HW5&userName=D0350510';
  constructor(private http: HttpClient) { }

  getCommitData(): Observable<any> {
    return this.http.get<any>(this.ALL_PROJECT_API);
  }
}
