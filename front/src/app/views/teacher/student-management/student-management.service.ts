import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FormGroup } from '@angular/forms';
import { environment } from '../../../../environments/environment';

const options = ({
  headers: new HttpHeaders({
    'Content-Type': 'application/x-www-form-urlencoded',
  })
});
@Injectable({
  providedIn: 'root'
})


export class StudentManagementService {
  GET_USERS_API = environment.SERVER_URL + '/ProgEdu/webapi/user/getUsers';
  ADD_ONE_USER_API = environment.SERVER_URL + '/ProgEdu/webapi/user/new';
  constructor(private http: HttpClient) { }

  getAllUserData(): Observable<any> {
    console.log('test: ' + this.GET_USERS_API);
    return this.http.get<any>(this.GET_USERS_API);

  }
  addOneStudent(student: FormGroup) {
    let params = new HttpParams();

    params = params.append('name', student.value.name);
    params = params.append('username', student.value.username);
    params = params.append('password', student.value.password);
    params = params.append('email', student.value.email);
    params = params.append('isDisplayed', student.value.isDisplayed);

    return this.http.post<any>(this.ADD_ONE_USER_API, params, options);
  }
}
