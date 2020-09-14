import { environment } from './../../environments/environment';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class StudentEventsService {

  private ADD_STUDENT_LOGIN_EVENT_API = 'http://140.134.26.63:23000/webapi/student_events/logStudentEvent';
  private ip = '';

  constructor(private http: HttpClient) {
    this.getIPAddress().subscribe((res: any) => {
      this.ip = res.ip;
    });
   }

  createReviewRecord(event: any): Observable<any> {
    const formData = new FormData();

    formData.append('username', event.username);
    formData.append('page', event.page);
    formData.append('name', event.event );
    formData.append('event',  '{}');
    formData.append('ip', this.ip);
    return this.http.post<any>(this.ADD_STUDENT_LOGIN_EVENT_API, formData  );
  }

  private getIPAddress() {
    return this.http.get('http://ip.jsontest.com/');
  }
}
