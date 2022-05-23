import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FormGroup } from '@angular/forms';
import { environment } from '../../../../environments/environment';
import {AddJwtTokenHttpClient} from '../../../services/add-jwt-token.service';
import { UserAPI } from '../../../api/UserAPI';
import { AssignmentAPI } from '../../../api/AssignmentAPI';
import { ScoreAPI } from '../../../api/ScoreAPI';

const deleteAssignmentOptions = ({
  headers: new HttpHeaders({
  })
});

const addMultipleScoreOptions = ({
  headers: new HttpHeaders(
  )
});

@Injectable({
  providedIn: 'root'
})


export class ScoreManagementService {
  ALL_ASSIGNMENTS_API = AssignmentAPI.getAllAssignments;
  ALL_AVERAGE_SCORE_API = ScoreAPI.getAvgScores;
  ADD_ASSIGNMENT_SCORE_API = ScoreAPI.addAssignmentScoreByCsv;
  ADD_EXAM_SCORE_API = ScoreAPI.addExamScoreByCsv;
  DELETE_ALL_SCORE_API = ScoreAPI.deleteAllScores;
  constructor(private addJwtTokenHttpClient: AddJwtTokenHttpClient) { }

  addMultipleAssignmentScore(assignment: FormGroup, file: File) {
    const formData = new FormData();
    formData.append('assignmentName', assignment.value.assignmentName);
    formData.append('file', file);
    return this.addJwtTokenHttpClient.post(this.ADD_ASSIGNMENT_SCORE_API, formData, addMultipleScoreOptions);
  }

  addMultipleExamScore(assignment: FormGroup, file: File) {
    const formData = new FormData();
    formData.append('assignmentName', assignment.value.examName);
    formData.append('file', file);
    return this.addJwtTokenHttpClient.post(this.ADD_EXAM_SCORE_API, formData, addMultipleScoreOptions);
  }

  deleteAllScores(assignmentName: string): Observable<any> {
    const params = new HttpParams()
      .set('assignmentName',assignmentName);
    return this.addJwtTokenHttpClient.post(this.DELETE_ALL_SCORE_API, params);
  }

  getAllAssignments(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.ALL_ASSIGNMENTS_API);
  }

  getAllAvgScore(): Observable<any> {
    return this.addJwtTokenHttpClient.get(this.ALL_AVERAGE_SCORE_API);
  }

}
