import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class AssignmentManagementService {

  ALL_PROJECT_API = environment.SERVER_URL + '/ProgEdu/webapi/project/getAllProjects';
  constructor(private http: HttpClient) { }
  getAllProjects(): Observable<any> {
    return this.http.get<any>(this.ALL_PROJECT_API);
  }
}
