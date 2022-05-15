import { Component, OnInit, ViewChild } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { EditScoreManagementService } from './edit-score-management.service';
import { ActivatedRoute } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-edit-score-management',
  templateUrl: './edit-score-management.component.html',
  //styleUrls: ['./edit-score-management.component.scss']
})

export class EditScoreManagementComponent implements OnInit {
  @ViewChild('editModal', { static: true }) public editModal: ModalDirective;
  public assignmentName: string;
  public assignmentScoreTable: Array<any> = new Array<any>();
  public assignmentUserId: string;
  public editScoreForm: FormGroup;
  public oldScore: number;

  errorResponse: HttpErrorResponse;
  errorTitle: string;
  
  constructor(private route: ActivatedRoute, private editScoreManagementService: EditScoreManagementService,private fb: FormBuilder) { }

  async ngOnInit() {
    this.assignmentName = this.route.snapshot.queryParamMap.get('assignmentName');
    await this.getAllUsersScore();
    this.editScoreForm = this.fb.group({
      score: [0, [Validators.pattern('^[0-9]{1,3}'), Validators.required]]
    });
  }

  async getAllUsersScore() {
    this.editScoreManagementService.getAllUsersScore(this.assignmentName).subscribe(response => {
      this.assignmentScoreTable = response;
    });
  }
  
  setUserId(assignmentUserId: string) {
    this.assignmentUserId = assignmentUserId;
  }

  setEditScore(score: number) {
    this.oldScore = score;
    this.editScoreForm.get('score').setValue(score);
  }

  checkForm() {
    if(this.editScoreForm.invalid || this.editScoreForm.get('score').value == this.oldScore) {
      return true;
    }
    return false;
  }

  editScore() {
    const newScore = this.editScoreForm.get('score').value;
    this.editScoreManagementService.editUserScore(this.assignmentName, this.assignmentUserId, newScore).subscribe(
      response => {
        this.editModal.hide();
        this.getAllUsersScore();
      },
      error => {
        this.errorTitle = 'Edit Assignment Error';
        this.editModal.hide();
        this.errorResponse = error;
      });
  }

}