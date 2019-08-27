import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
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
  CREATE_ASSIGNMENT_API = environment.SERVER_URL + '/ProgEdu/webapi/assignment/create';
  constructor(private http: HttpClient) { }

  createAssignment(assigememt: FormGroup): Observable<any> {

    const formData = new FormData();

    formData.append('assignmentName', assigememt.value.name);
    formData.append('releaseTime', new Date(assigememt.value.releaseTime).toUTCString());
    formData.append('deadline', new Date(assigememt.value.deadline).toUTCString());
    formData.append('readMe', assigememt.value.readMe);
    formData.append('fileRadio', assigememt.value.type);
    formData.append('file', assigememt.value.file);

    return this.http.post(this.CREATE_ASSIGNMENT_API, formData, createAssigmentOptions);
  }
}
