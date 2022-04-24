import { Component, OnInit } from '@angular/core';
import { ScoreManagementService } from './score-management.service';
import { environment } from '../../../../environments/environment';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { assignmentMethodEnum } from './assignmentMethodEnum.enum';
import * as $ from 'jquery';
@Component({
  selector: 'app-score-management',
  templateUrl: './score-management.component.html'
})
export class ScoreManagementComponent implements OnInit {
  public users: Array<any> = new Array<any>();
  public scoreForm: FormGroup;
  public multipleScoreFile: File;
  public NEW_SERVER_URL = environment.NEW_SERVER_URL;
  public isCollapsed = true;
  public addOneScoreErrorMsg = '';
  public addOneScoreSuccessful: boolean = false;
  public addMultipleScoreErrorMsg = '';
  public addMultipleScoreSuccessful: boolean = false;
  public progressbar : boolean = false;
  constructor(private scoreService: ScoreManagementService, private fb: FormBuilder) { }

  async ngOnInit() {
    await this.getAllScore();
    this.scoreForm = this.fb.group({
      method: [assignmentMethodEnum['Assignment']],
      assignmentName: [undefined, [Validators.required, Validators.pattern('^[a-zA-Z0-9-_]{3,10}')]],
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

  showIsValidById(id: string) {
    $('#' + id).addClass('is-valid');
    $('#' + id).removeClass('is-invalid');
  }

  hideIsInvalidById(id: string) {
    $('#' + id).removeClass('is-valid');
    $('#' + id).addClass('is-invalid');
  }

  async getAllScore() {
    
  }

  changeFileListener(e: { target: { files: File[]; }; }) {
    this.addMultipleScoreSuccessful = false;
    this.addMultipleScoreErrorMsg = '';
    this.multipleScoreFile = e.target.files[0];
  }

  public uploadMultipleScore() {
    this.progressbar = true;
    if(this.multipleScoreFile != null) {
      this.scoreService.addMultipleScore(this.scoreForm, this.multipleScoreFile).subscribe(
        (response) => {
          this.progressbar = false;
          this.getAllScore();
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