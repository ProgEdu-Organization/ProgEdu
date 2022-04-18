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
  public oneStudentForm: FormGroup;
  public multipleStudentFile: File;
  public NEW_SERVER_URL = environment.NEW_SERVER_URL;
  public isCollapsed = true;
  public addOneStudentErrorMsg = '';
  public addOneStudentSuccessful: boolean = false;
  public addMultipleStudentErrorMsg = '';
  public addMultipleStudentSuccessful: boolean = false;
  public progressbar : boolean = false;
  constructor(private scoreService: ScoreManagementService, private fb: FormBuilder) { }

  async ngOnInit() {
    await this.getAllUser();
    this.oneStudentForm = this.fb.group({
      role: ['', Validators.required],
      name: ['', Validators.required],
      username: ['', Validators.pattern('^[a-zA-Z0-9-_]{4,20}')],
      password: ['', Validators.pattern('^[a-zA-Z0-9-_]{8,20}')],
      email: ['', [Validators.required, Validators.email]],
      isDisplayed: [true],
      rememberMe: [true]
    });
    this.onChange();
  }

  onChange() {
    const name = 'name';
    const username = 'username';
    const password = 'password';
    const email = 'email';

    this.oneStudentForm.get(name).valueChanges.subscribe(
      () => {
        this.oneStudentForm.get(name).valid ? this.showIsValidById(name) : this.hideIsInvalidById(name);
      });

    this.oneStudentForm.get(username).valueChanges.subscribe(
      () => {
        this.oneStudentForm.get(username).valid ? this.showIsValidById(username) : this.hideIsInvalidById(username);
      });

    this.oneStudentForm.get(password).valueChanges.subscribe(
      () => {
        this.oneStudentForm.get(password).valid ? this.showIsValidById(password) : this.hideIsInvalidById(password);
      });

    this.oneStudentForm.get(email).valueChanges.subscribe(
      () => {
        this.oneStudentForm.get(email).valid ? this.showIsValidById(email) : this.hideIsInvalidById(email);
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

  async getAllUser() {
    
  }

  public uploadMultipleScore() {

  }

}