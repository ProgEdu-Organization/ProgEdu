import { Category, Assessment } from './Category';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { param } from 'jquery';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';

import { CategoryMetricsAPI } from '../../../api/CategoryMetricsAPI';

@Injectable({
  providedIn: 'root'
})
export class ReviewMetricsManagementService {

  GET_ALL_CATEGORY_API = CategoryMetricsAPI.getCategory;
  GET_METRICS_API = CategoryMetricsAPI.getMetrics + "?";
  CREATE_CATEGORY_API = CategoryMetricsAPI.createCategory + "?";
  EDIT_CATEGORY_API = CategoryMetricsAPI.editCategory + "?";
  DELETE_CATEGORY_API = CategoryMetricsAPI.deleteCategory + "?";
  CREATE_METRICS_API = CategoryMetricsAPI.createMetrics + "?";
  EDIT_METRICS_API = CategoryMetricsAPI.editMetrics + "?";
  DELETE_METRICS_API = CategoryMetricsAPI.deleteMetrics + "?";

  constructor(private http: HttpClient, private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getAllCategory(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.GET_ALL_CATEGORY_API);
  }
  getMetrics(category: Category): Observable<any> {
    const params = new HttpParams()
    .set('category', category.id.toString());
    return this.addJwtTokenHttpClient.get(this.GET_METRICS_API + params.toString() );
  }
  createCategory(category: Category): Observable<any> {
    const params = new HttpParams()
    .set('name', category.name)
    .set('metrics', category.metrics);
    return this.addJwtTokenHttpClient.post(this.CREATE_CATEGORY_API + params.toString(), null );
  }
  editCategory(category: Category): Observable<any> {
    const params = new HttpParams()
    .set('id', category.id.toString())
    .set('metrics', category.metrics);
    return this.addJwtTokenHttpClient.put(this.EDIT_CATEGORY_API + params.toString(), null);
  }
  deleteCategory(category: Category): Observable<any> {
    const params = new HttpParams()
    .set('id', category.id.toString());
    return this.addJwtTokenHttpClient.delete(this.DELETE_CATEGORY_API + params.toString());
  }
  createMetrics(category: Category, assessment: Assessment): Observable<any> {
    const params = new HttpParams()
    .set('category', category.id.toString())
    .set('mode', assessment.mode.toString())
    .set('description', assessment.description)
    .set('link', assessment.link)
    .set('metrics', assessment.metrics);
    return this.addJwtTokenHttpClient.post(this.CREATE_METRICS_API + params.toString(), null);
  }
  editMetrics(assessment: Assessment): Observable<any> {
    const params = new HttpParams()
    .set('metrics', assessment.metrics.toString())
    .set('id', assessment.id.toString())
    .set('mode', assessment.mode.toString())
    .set('description', assessment.description)
    .set('link', assessment.link);
    return this.addJwtTokenHttpClient.put(this.EDIT_METRICS_API + params.toString(), null);
  }
  deleteMetrics(assessment: Assessment): Observable<any> {
    const params = new HttpParams()
    .set('id', assessment.id.toString());
    return this.addJwtTokenHttpClient.delete(this.DELETE_METRICS_API + params.toString());
  }


}
