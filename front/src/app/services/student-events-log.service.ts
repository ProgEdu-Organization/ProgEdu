import { environment } from './../../environments/environment.prod';
import { User } from './../models/user';
import { JwtService } from './jwt.service';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StudentEvent } from './student-event';

@Injectable({
  providedIn: 'root'
})
export class StudentEventsService {

  private ADD_STUDENT_LOGIN_EVENT_API = environment.SERVER_URL + '/webapi/student_events/logStudentEvent';
  private username = '';

  constructor(private http: HttpClient, private jwtService?: JwtService) {
  }

  createReviewRecord(event: StudentEvent) {
  }
}
