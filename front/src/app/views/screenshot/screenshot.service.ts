import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ScreenshotService {
  SCREENSHOt_API = environment.SERVER_URL + '/ProgEdu/webapi/commits/screenshot/getScreenshotURL';
  constructor(private http: HttpClient) { }

  getScreenshotUrls(username: string, assignmentName: string, commitNumber: number): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('assignmentName', assignmentName)
      .set('commitNumber', commitNumber.toString());
    return this.http.get<any>(this.SCREENSHOt_API, { params });
  }
}
