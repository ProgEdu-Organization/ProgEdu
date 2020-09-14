import { User } from './../models/user';
import { JwtService } from './jwt.service';
import { environment } from './../../environments/environment';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StudentEvent } from './emit-student-event';

@Injectable({
  providedIn: 'root'
})
export class StudentEventsService {

  private ADD_STUDENT_LOGIN_EVENT_API = 'http://140.134.26.63:23000/webapi/student_events/logStudentEvent';
  private username = '';
  private ip;

  constructor(private http: HttpClient, private jwtService?: JwtService) {
  }

  createReviewRecord(event: StudentEvent) {
    const formData = new FormData();
    if (this.username === '') {
      this.username = new User(this.jwtService).getUsername();
    }
    formData.append('username', this.username);
    formData.append('page', event.page);
    formData.append('name', event.name);
    formData.append('event', event.event);
    this.getIPAddress().subscribe(
      (res) => {
        formData.append('ip', res.ip);
        return this.http.post<any>(this.ADD_STUDENT_LOGIN_EVENT_API, formData).subscribe();
      },
      (error) => {
        formData.append('ip', 'unknown');
        return this.http.post<any>(this.ADD_STUDENT_LOGIN_EVENT_API, formData).subscribe();
      }
    );
  }

  getIPAddress(): Observable<any> {
    return this.http.get('http://ip.jsontest.com/');
  }
}
