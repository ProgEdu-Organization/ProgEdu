import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FormGroup } from '@angular/forms';
import { environment } from '../../../../environments/environment';

const addOneStudentOptions = ({
  headers: new HttpHeaders({
    'Content-Type': 'application/x-www-form-urlencoded',
  })
});

const addMultipleStudentOptions = ({
  headers: new HttpHeaders(
  )
});

const updateDisplayOptions = ({
  headers: new HttpHeaders(
  )
});


@Injectable({
  providedIn: 'root'
})


export class StudentManagementService {
  GET_USERS_API = environment.SERVER_URL + '/ProgEdu/webapi/user/getUsers';
  ADD_ONE_USER_API = environment.SERVER_URL + '/ProgEdu/webapi/user/new';
  ADD_Multiple_USER_API = environment.SERVER_URL + '/ProgEdu/webapi/user/upload';
  DISPLAY_API = environment.SERVER_URL + '/ProgEdu/webapi/user/display';
  constructor(private http: HttpClient) { }

  getAllUserData(): Observable<any> {
    return this.http.get<any>(this.GET_USERS_API);

  }
  addOneStudent(student: FormGroup): Observable<any> {
    const params = new HttpParams()
      .append('name', student.value.name)
      .append('username', student.value.username)
      .append('password', student.value.password)
      .append('email', student.value.email)
      .append('role', student.value.role)
      .append('isDisplayed', student.value.isDisplayed);

    return this.http.post<any>(this.ADD_ONE_USER_API, params, addOneStudentOptions);
  }

  addMultipleStudent(file: File) {
    const frmData = new FormData();
    frmData.append('file', file);
    return this.http.post<any>(this.ADD_Multiple_USER_API, frmData, addMultipleStudentOptions);
  }

  updateDisplay(username: string) {
    const params = new HttpParams()
      .append('username', username);

    return this.http.post<any>(this.DISPLAY_API, params, updateDisplayOptions);
  }

}
