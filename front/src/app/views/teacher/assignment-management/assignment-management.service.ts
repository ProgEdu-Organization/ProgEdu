import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';


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
  constructor(private http: HttpClient) { }
  getAllAssignments(): Observable<any> {
    return this.http.get<any>(this.ALL_ASSIGNMENT_API);
  }

  editAssignment(): Observable<any> {
    const form = new FormData();
    return this.http.post<any>(this.ALL_ASSIGNMENT_API, form, editAssignmentOptions);
  }

  deleteAssignment(assignmentName: string): Observable<any> {
    const form = new FormData();
    form.append('del_Hw_Name', assignmentName);
    return this.http.post<any>(this.DELETE_ASSIGNMENT_API, form, deleteAssignmentOptions);
  }
}
