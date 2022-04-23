import { Component, OnInit } from '@angular/core';
import { ScoreManagementService } from './score-management.service';
import { environment } from '../../../../environments/environment';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import * as $ from 'jquery';
@Component({
  selector: 'app-student-management',
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
      type: ['', Validators.required],
      assignmentName: ['', Validators.pattern('^[a-zA-Z0-9-_]{4,20}')],
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

  public uploadMultipleScore() {

  }

}