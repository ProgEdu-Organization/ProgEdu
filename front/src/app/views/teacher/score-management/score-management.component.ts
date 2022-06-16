import { Component, OnInit, ViewChild } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { ScoreManagementService } from './score-management.service';
import { HttpErrorResponse } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { assignmentMethodEnum } from './assignmentMethodEnum.enum';
import * as $ from 'jquery';
@Component({
  selector: 'app-score-management',
  templateUrl: './score-management.component.html'
})
export class ScoreManagementComponent implements OnInit {
  @ViewChild('deleteModal', { static: true }) public deleteModal: ModalDirective;
  public users: Array<any> = new Array<any>();
  public assignmentTable: Array<any> = new Array<any>();
  public avgScoreTable: Array<any> = new Array<any>();
  public scoreForm: FormGroup;
  public multipleScoreFile: File;
  public NEW_SERVER_URL = environment.NEW_SERVER_URL;
  public isCollapsed = true;
  public isDeleteProgress = false;
  public selectedAssignment = "";
  public addOneScoreErrorMsg = '';
  public addOneScoreSuccessful: boolean = false;
  public addMultipleScoreErrorMsg = '';
  public addMultipleScoreSuccessful: boolean = false;
  public progressbar : boolean = false;

  errorResponse: HttpErrorResponse;
  errorTitle: string;

  constructor(private scoreService: ScoreManagementService, private fb: FormBuilder) { }

  async ngOnInit() {
    await this.getAllAssignments();
    await this.getAllAvgScore();
    this.scoreForm = this.fb.group({
      method: [assignmentMethodEnum['Assignment']],
      assignmentName: [''],
      examName: [undefined, [Validators.required, Validators.pattern('^[a-zA-Z0-9-_]{3,10}')]],
    });
    this.onChange();
  }

  onChange() {
    const assignmentName = 'assignmentName';

    this.scoreForm.get(assignmentName).valueChanges.subscribe(
      () => {
        this.scoreForm.get(assignmentName).valid ? this.showIsValidById(assignmentName) : this.hideIsInvalidById(assignmentName);
      });

  }

  async getAllAvgScore() {
    this.scoreService.getAllAvgScore().subscribe(response => {
      this.avgScoreTable = response;
    })
  }

  async getAllAssignments() {
    this.scoreService.getAllAssignments().subscribe(response => {
      this.assignmentTable = response.allAssignments;
    })
  }


  showIsValidById(id: string) {
    $('#' + id).addClass('is-valid');
    $('#' + id).removeClass('is-invalid');
  }

  hideIsInvalidById(id: string) {
    $('#' + id).removeClass('is-valid');
    $('#' + id).addClass('is-invalid');
  }

  changeFileListener(e: { target: { files: File[]; }; }) {
    this.addMultipleScoreSuccessful = false;
    this.addMultipleScoreErrorMsg = '';
    this.multipleScoreFile = e.target.files[0];
  }

  checkForm() {
    if(!this.multipleScoreFile) {
      return true;
    }
    if(this.scoreForm.get('method').value == assignmentMethodEnum['Assignment']) {
      if(this.scoreForm.get('assignmentName').value == "") {
        return true;
      }
    } else {
      if(this.scoreForm.get('examName').invalid) {
        return true;
      }
    }
    return false;
  }

  setSelectedAssignment(assignmentName: string) {
    this.selectedAssignment = assignmentName;
  }

  getType(type: string) {
    if(type == "EXAM") return "Exam"
    else return "Assignment"
  }

  deleteScores(assignmentName: string) {
    this.isDeleteProgress = true;

    this.scoreService.deleteAllScores(assignmentName).subscribe(
      response => {
        this.deleteModal.hide();
        this.getAllAvgScore();
        this.isDeleteProgress = false;
      },
      error => {
        this.errorTitle = 'Delete Score Error';
        this.deleteModal.hide();
        this.errorResponse = error;
      });
  }

  public uploadMultipleScore() {
    console.log(this.scoreForm.value.method);
    this.progressbar = true;
    if(this.multipleScoreFile != null) {
      if (this.scoreForm.value.method == assignmentMethodEnum['Assignment']) {
        this.scoreService.addMultipleAssignmentScore(this.scoreForm, this.multipleScoreFile).subscribe(
          (response) => {
            this.progressbar = false;
            this.getAllAvgScore();
            this.addMultipleScoreSuccessful = true;
            this.addMultipleScoreErrorMsg = '';
          },
          error => {
            this.addMultipleScoreErrorMsg = error.error;
          }
        )
      } else {
        this.scoreService.addMultipleExamScore(this.scoreForm, this.multipleScoreFile).subscribe(
          (response) => {
            this.progressbar = false;
            this.getAllAvgScore();
            this.addMultipleScoreSuccessful = true;
            this.addMultipleScoreErrorMsg = '';
          },
          error => {
            this.addMultipleScoreErrorMsg = error.error;
          }
        )
      }
    }
  }

}