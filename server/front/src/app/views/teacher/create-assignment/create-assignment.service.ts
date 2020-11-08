import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FormGroup } from '@angular/forms';
import { environment } from '../../../../environments/environment';

const createAssigmentOptions = ({
  headers: new HttpHeaders(
  )
});
@Injectable({
  providedIn: 'root'
})
export class CreateAssignmentService {
  CREATE_ASSIGNMENT_API = environment.SERVER_URL + '/webapi/assignment/create';
  MODIFY_ORDER_API = environment.SERVER_URL + '/webapi/assignment/order';
  GET_ORDER_API = environment.SERVER_URL + '/webapi/assignment/getAssignmentFile';
  constructor(private http: HttpClient) { }

  createAssignment(assignment: FormGroup): Observable<any> {

    const formData = new FormData();

    formData.append('assignmentName', assignment.value.name);
    formData.append('releaseTime', new Date(assignment.value.releaseTime).toUTCString());
    formData.append('deadline', new Date(assignment.value.deadline).toUTCString());
    formData.append('readMe', assignment.value.description);
    formData.append('fileRadio', assignment.value.type);
    formData.append('file', assignment.value.file);
    formData.append('order',assignment.value.assOrder);

    return this.http.post(this.CREATE_ASSIGNMENT_API, formData, createAssigmentOptions);
  }

  modifyOrder(assignment: FormGroup): Observable<any> {

    const formData = new FormData();
    
    formData.append('assignmentName', assignment.value.name);
    formData.append('fileRadio', assignment.value.type);
    formData.append('order',assignment.value.assOrder);
    
    return this.http.post(this.MODIFY_ORDER_API, formData, createAssigmentOptions);
  }

}
