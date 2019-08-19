import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StudentManagementService {
  USER_API = 'ProgEdu/webapi/user/getUsers';
  constructor(private http: HttpClient) { }

  getAllUserData(): Observable<any> {
    return this.http.get<any>(this.USER_API);
  }
}
