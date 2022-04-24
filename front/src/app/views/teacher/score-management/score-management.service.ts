import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FormGroup } from '@angular/forms';
import { environment } from '../../../../environments/environment';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';
import { UserAPI } from '../../../api/UserAPI';
import { AssignmentAPI } from '../../../api/AssignmentAPI';
import { ScoreAPI } from '../../../api/ScoreAPI';


const addMultipleScoreOptions = ({
  headers: new HttpHeaders(
  )
});

@Injectable({
  providedIn: 'root'
})


export class ScoreManagementService {
  GET_ASSIGNMENTS_API = AssignmentAPI.getAllAssignments;
  ADD_MULTIPLE_SCORE_API = ScoreAPI.addScoreByCsv;
  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  addMultipleAssignmentScore(assignment: FormGroup, file: File) {
    const formData = new FormData();
    formData.append('assignmentName', assignment.value.assignmentName);
    formData.append('file', file);
    return this.addJwtTokenHttpClient.post(this.ADD_MULTIPLE_SCORE_API, formData, addMultipleScoreOptions);
  }

}
