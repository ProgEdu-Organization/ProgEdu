import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StudentManagementService {
  USER_API = environment.SERVER_URL + '/ProgEdu/webapi/user/getUsers';
  constructor(private http: HttpClient) { }

  getAllUserData(): Observable<any> {
    return this.http.get<any>(this.USER_API);
  }
}
