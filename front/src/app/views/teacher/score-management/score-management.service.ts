import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FormGroup } from '@angular/forms';
import { environment } from '../../../../environments/environment';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';
import { UserAPI } from '../../../api/UserAPI';
import { AssignmentAPI } from '../../../api/AssignmentAPI';


@Injectable({
  providedIn: 'root'
})


export class ScoreManagementService {
  GET_ASSIGNMENTS_API = AssignmentAPI.getAllAssignments;
  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

}
