import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { FormGroup } from '@angular/forms';


const deleteAssignmentOptions = ({
  headers: new HttpHeaders({
  })
});


const editAssignmentOptions = ({
  headers: new HttpHeaders({
  })
});


@Injectable({
  providedIn: 'root'
})
export class AssignmentManagementService {

  ALL_ASSIGNMENT_API = environment.SERVER_URL + '/ProgEdu/webapi/assignment/getAllAssignments';
  DELETE_ASSIGNMENT_API = environment.SERVER_URL + '/ProgEdu/webapi/assignment/delete';
  EDIT_ASSIGNMENT_API = environment.SERVER_URL + '/ProgEdu/webapi/assignment/edit';
  constructor(private http: HttpClient) { }
  getAllAssignments(): Observable<any> {
    return this.http.get<any>(this.ALL_ASSIGNMENT_API);
  }

  editAssignment(assignment: FormGroup): Observable<any> {
    const form = new FormData();
    console.log('test' + assignment.get('name').value);
    form.append('assignmentName', assignment.get('name').value);
    form.append('releaseTime', new Date(assignment.get('releaseTime').value).toUTCString());
    form.append('deadline', new Date(assignment.get('deadline').value).toUTCString());
    form.append('readMe', assignment.get('readMe').value);
    form.append('file', assignment.get('file').value);

    return this.http.post<any>(this.EDIT_ASSIGNMENT_API, form, editAssignmentOptions);
  }

  deleteAssignment(assignmentName: string): Observable<any> {
    const form = new FormData();
    form.append('del_Hw_Name', assignmentName);
    return this.http.post<any>(this.DELETE_ASSIGNMENT_API, form, deleteAssignmentOptions);
  }

}
