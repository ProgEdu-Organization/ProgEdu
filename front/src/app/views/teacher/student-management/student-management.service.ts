import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FormGroup } from '@angular/forms';
import { environment } from '../../../../environments/environment';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';
import { UserAPI } from '../../../api/UserAPI';

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
  GET_USERS_API = UserAPI.getUsers;
  ADD_ONE_USER_API = UserAPI.addOneUser;
  ADD_MULTIPLE_USER_API = UserAPI.addUserByCsv;
  DISPLAY_API = environment.SERVER_URL + '/webapi/user/display';// todo 這沒用到
  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getAllUserData(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.GET_USERS_API);

  }
  addOneStudent(student: FormGroup): Observable<any> {
    const params = new HttpParams()
      .append('name', student.value.name)
      .append('username', student.value.username)
      .append('password', student.value.password)
      .append('email', student.value.email)
      .append('role', student.value.role)
      .append('isDisplayed', student.value.isDisplayed);

    return this.addJwtTokenHttpClient.post(this.ADD_ONE_USER_API, params, addOneStudentOptions);
  }

  addMultipleStudent(file: File) {
    const frmData = new FormData();
    frmData.append('file', file);
    return this.addJwtTokenHttpClient.post(this.ADD_MULTIPLE_USER_API, frmData, addMultipleStudentOptions);
  }

  updateDisplay(username: string) {
    const params = new HttpParams()
      .append('username', username);

    return this.addJwtTokenHttpClient.post(this.DISPLAY_API, params, updateDisplayOptions);
  }

}
