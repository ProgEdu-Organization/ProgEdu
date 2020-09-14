import { Category, Assessment } from './Category';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class ReviewMetricsManagementService {

  GET_ALL_CATEGORY_API = environment.SERVER_URL + '/webapi/categoryMetrics/category';
  GET_METRICS_API = environment.SERVER_URL + '/webapi/categoryMetrics/metrics';
  CREATE_CATEGORY_API = environment.SERVER_URL + '/webapi/categoryMetrics/category/create?';
  EDIT_CATEGORY_API = environment.SERVER_URL + '/webapi/categoryMetrics/category/edit?';
  DELETE_CATEGORY_API = environment.SERVER_URL + '/webapi/categoryMetrics/category/delete?';
  CREATE_METRICS_API = environment.SERVER_URL + '/webapi/categoryMetrics/metrics/create?';
  EDIT_METRICS_API = environment.SERVER_URL + '/webapi/categoryMetrics/metrics/edit?';
  DELETE_METRICS_API = environment.SERVER_URL + '/webapi/categoryMetrics/metrics/delete?';

  constructor(private http: HttpClient) { }

  getAllCategory(): Observable<any> {
    return this.http.get(this.GET_ALL_CATEGORY_API);
  }
  getMetrics(category: Category): Observable<any> {
    const params = new HttpParams()
    .set('category', category.id.toString());
    return this.http.get(this.GET_METRICS_API, { params } );
  }
  createCategory(category: Category): Observable<any> {
    const params = new HttpParams()
    .set('name', category.name)
    .set('metrics', category.metrics);
    return this.http.post(this.CREATE_CATEGORY_API + params.toString(), null );
  }
  editCategory(category: Category): Observable<any> {
    const params = new HttpParams()
    .set('id', category.id.toString())
    .set('metrics', category.metrics);
    return this.http.put(this.EDIT_CATEGORY_API + params.toString(), null);
  }
  deleteCategory(category: Category): Observable<any> {
    const params = new HttpParams()
    .set('id', category.id.toString());
    return this.http.delete(this.DELETE_CATEGORY_API + params.toString());
  }
  createMetrics(category: Category, assessment: Assessment): Observable<any> {
    const params = new HttpParams()
    .set('category', category.id.toString())
    .set('mode', assessment.mode.toString())
    .set('description', assessment.description)
    .set('link', assessment.link)
    .set('metrics', assessment.metrics);
    return this.http.post(this.CREATE_METRICS_API + params.toString(), null);
  }
  editMetrics(assessment: Assessment): Observable<any> {
    const params = new HttpParams()
    .set('id', assessment.id.toString())
    .set('mode', assessment.mode.toString())
    .set('description', assessment.description)
    .set('link', assessment.link);
    return this.http.put(this.EDIT_METRICS_API + params.toString(), null);
  }
  deleteMetrics(assessment: Assessment): Observable<any> {
    const params = new HttpParams()
    .set('id', assessment.id.toString());
    return this.http.delete(this.DELETE_METRICS_API + params.toString());
  }


}
