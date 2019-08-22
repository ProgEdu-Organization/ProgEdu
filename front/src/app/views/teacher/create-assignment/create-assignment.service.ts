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

  createAssignment(assigememt: FormGroup) {
    const formData = new FormData();
    formData.append('assignmentName', assigememt.value.name);
    formData.append('releaseTime', assigememt.value.releaseTime);
    formData.append('deadline', assigememt.value.deadline);
    formData.append('readMe', assigememt.value.readMe);
    formData.append('fileRadio', assigememt.value.type);
    formData.append('file', assigememt.value.file);

    return this.http.post(this.CREATE_ASSIGNMENT_API, formData, createAssigmentOptions);
  }
}
