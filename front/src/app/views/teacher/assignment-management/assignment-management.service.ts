import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
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

  ALL_ASSIGNMENT_API = environment.SERVER_URL + '/webapi/assignment/getAllAssignments';
  DELETE_ASSIGNMENT_API = environment.SERVER_URL + '/webapi/assignment/delete';
  EDIT_ASSIGNMENT_API = environment.SERVER_URL + '/webapi/assignment/edit';
  ASSIGNMENT_ORDER = environment.SERVER_URL + '/webapi/assignment/getAssignmentOrder'
  constructor(private http: HttpClient) { }
  getAllAssignments(): Observable<any> {
    return this.http.get<any>(this.ALL_ASSIGNMENT_API);
  }

  editAssignment(assignment: FormGroup): Observable<any> {
    const form = new FormData();
    form.append('assignmentName', assignment.get('name').value);
    form.append('releaseTime', new Date(assignment.get('releaseTime').value).toUTCString());
    form.append('deadline', new Date(assignment.get('deadline').value).toUTCString());
    form.append('readMe', assignment.get('description').value);
    form.append('file', assignment.get('file').value);
    form.append('order', assignment.get('order').value);

    return this.http.post<any>(this.EDIT_ASSIGNMENT_API, form, editAssignmentOptions);
  }

  deleteAssignment(assignmentName: string): Observable<any> {
    const form = new FormData();
    form.append('assignmentName', assignmentName);
    return this.http.post<any>(this.DELETE_ASSIGNMENT_API, form, deleteAssignmentOptions);
  }

  getAssignmentOrder(assignmentName: string): Observable<any> {
    const params = new HttpParams()
      .set('fileName',assignmentName);
    return this.http.get<any>(this.ASSIGNMENT_ORDER, { params });
  }

}
