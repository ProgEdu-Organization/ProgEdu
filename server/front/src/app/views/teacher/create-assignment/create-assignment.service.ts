import { Category, Assessment } from './../review-metrics-management/Category';
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
  CREATE_ASSIGNMENT_API = environment.SERVER_URL + 'webapi/assignment/create';
  GET_ALL_CATEGORY_API = `http://140.134.26.66:22000/webapi/categoryMetrics/category`;
  constructor(private http: HttpClient) { }

  createAssignment(assigememt: FormGroup): Observable<any> {

    const formData = new FormData();

    formData.append('assignmentName', assigememt.value.name);
    formData.append('releaseTime', new Date(assigememt.value.releaseTime).toUTCString());
    formData.append('deadline', new Date(assigememt.value.deadline).toUTCString());
    formData.append('readMe', assigememt.value.description);
    formData.append('fileRadio', assigememt.value.type);
    formData.append('file', assigememt.value.file);

    return this.http.post(this.CREATE_ASSIGNMENT_API, formData, createAssigmentOptions);
  }
  getAllCategory(): Observable<any> {
    return this.http.get(this.GET_ALL_CATEGORY_API);
  }
  getMetrics(category: Category): Observable<any> {
    const GET_METRICS_API = `http://140.134.26.66:22000/webapi/categoryMetrics/metrics?category=${category.id.toString()}`;
    return this.http.get(GET_METRICS_API);
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

    return this.http.post('http://140.134.26.62:8080/webapi/assignment/peerReview/create', formData, createAssigmentOptions);
  }
}
