import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { JwtService } from '../../../services/jwt.service';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';

import { AssignmentAPI } from '../../../api/AssignmentAPI';


@Injectable({
  providedIn: 'root'
})


export class DashboardService {
  ALL_COMMIT_API = environment.SERVER_URL + '/webapi/commits/allUsers';
  ALL_ASSIGNMENT_API = AssignmentAPI.getAllAssignments;
  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  getAllStudentCommitRecord(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.ALL_COMMIT_API);
  }

  getAllAssignments(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.ALL_ASSIGNMENT_API);
  }

}
