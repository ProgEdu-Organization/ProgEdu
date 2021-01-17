import { Category, Assessment } from './../review-metrics-management/Category';
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
  GET_ALL_CATEGORY_API = environment.SERVER_URL + '/webapi/categoryMetrics/category';
  GET_METRICS_API = environment.SERVER_URL + '/webapi/categoryMetrics/metrics';
  CREATE_REVIEW_ASSIGNMENT_API = environment.SERVER_URL + '/webapi/assignment/peerReview/create';
  constructor(private http: HttpClient) { }

  createAssignment(assignment: FormGroup): Observable<any> {

    const formData = new FormData();

    formData.append('assignmentName', assignment.value.name);
    formData.append('releaseTime', new Date(assignment.value.releaseTime).toUTCString());
    formData.append('deadline', new Date(assignment.value.deadline).toUTCString());
    formData.append('readMe', assignment.value.description);
    formData.append('fileRadio', assignment.value.type);
    formData.append('file', assignment.value.file);
    //formData.append('order',assignment.value.assOrder);

    return this.http.post(this.CREATE_ASSIGNMENT_API, formData, createAssigmentOptions);
  }

  modifyOrder(assignment: FormGroup): Observable<any> {

    const formData = new FormData();
    
    formData.append('assignmentName', assignment.value.name);
    formData.append('fileRadio', assignment.value.type);
    formData.append('order',assignment.value.assOrder);
    
    return this.http.post(this.MODIFY_ORDER_API, formData, createAssigmentOptions);
  }

  getAllCategory(): Observable<any> {
    return this.http.get(this.GET_ALL_CATEGORY_API);
  }
  getMetrics(category: Category): Observable<any> {
    const params = new HttpParams().
    set('category', category.id.toString());
    return this.http.get(this.GET_METRICS_API , { params });
  }

  createPeerReviewAssignment(assigememt: FormGroup, metrics: number[]): Observable<any> {
    const formData = new FormData();

    formData.append('assignmentName', assigememt.value.name);
    formData.append('releaseTime', new Date(assigememt.value.releaseTime).toUTCString());
    formData.append('deadline', new Date(assigememt.value.deadline).toUTCString());
    formData.append('readMe', assigememt.value.description);
    formData.append('fileRadio', assigememt.value.type);
    formData.append('file', assigememt.value.file);
    formData.append('amount', assigememt.value.commitRecordCount);
    formData.append('reviewStartTime', new Date(assigememt.value.reviewReleaseTime).toUTCString());
    formData.append('reviewEndTime', new Date(assigememt.value.reviewDeadline).toUTCString());
    formData.append('metrics', metrics.toString());

    return this.http.post( this.CREATE_REVIEW_ASSIGNMENT_API, formData, createAssigmentOptions);
  }
}
