import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { FormGroup, FormArray } from '@angular/forms';
import { AddJwtTokenHttpClient } from '../../../services/add-jwt-token.service';

import { AssignmentAPI } from '../../../api/AssignmentAPI';


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

  ALL_ASSIGNMENT_API = AssignmentAPI.getAllAssignments;
  DELETE_ASSIGNMENT_API = AssignmentAPI.deleteAssignment;
  EDIT_ASSIGNMENT_API = AssignmentAPI.editAssignment;
  ASSIGNMENT_ORDER = AssignmentAPI.getAssignmentOrder;

  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }
  
  getAllAssignments(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.ALL_ASSIGNMENT_API);
  }

  editAssignment(assignment: FormGroup): Observable<any> {
    const form = new FormData();
    const reviewTime = new FormArray([]);
    let round = 0;

    (<FormArray>assignment.get('reviewTime')).controls.forEach(element => {
      let action = new FormGroup({});
      let reviewAction = new FormGroup({});
      let roundGroup = new FormGroup({});
      element.get('startTime').setValue(new Date(element.get('startTime').value).toUTCString());
      element.get('endTime').setValue(new Date(element.get('endTime').value).toUTCString());
      action.addControl('startTime', element.get('startTime'));
      action.addControl('endTime', element.get('endTime'));
      roundGroup.addControl('Do', action);
      element.get('reviewStartTime').setValue(new Date(element.get('reviewStartTime').value).toUTCString());
      element.get('reviewEndTime').setValue(new Date(element.get('reviewEndTime').value).toUTCString());
      reviewAction.addControl('startTime', element.get('reviewStartTime'));
      reviewAction.addControl('endTime', element.get('reviewEndTime'));
      roundGroup.addControl('Review', reviewAction);
      reviewTime.insert(round, roundGroup);
      round++;
    })

    form.append('assignmentName', assignment.get('name').value);
    //form.append('releaseTime', new Date(assignment.get('releaseTime').value).toUTCString());
    //form.append('deadline', new Date(assignment.get('deadline').value).toUTCString());
    form.append('assessmentTimes', JSON.stringify(reviewTime.value));
    form.append('readMe', assignment.get('description').value);
    form.append('order', assignment.get('order').value);

    return this.addJwtTokenHttpClient.post(this.EDIT_ASSIGNMENT_API, form, editAssignmentOptions);
  }

  deleteAssignment(assignmentName: string): Observable<any> {
    const form = new FormData();
    form.append('assignmentName', assignmentName);
    return this.addJwtTokenHttpClient.post(this.DELETE_ASSIGNMENT_API, form, deleteAssignmentOptions);
  }

  getAssignmentOrder(assignmentName: string): Observable<any> {
    const params = new HttpParams()
      .set('fileName',assignmentName);
    return this.addJwtTokenHttpClient.get(this.ASSIGNMENT_ORDER, { params });
  }

}
