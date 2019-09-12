import { Component, OnInit } from '@angular/core';
import { StudentManagementService } from './student-management.service';
import { environment } from '../../../../environments/environment';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import * as $ from 'jquery';
@Component({
  selector: 'app-student-management',
  templateUrl: './student-management.component.html'
})
export class StudentManagementComponent implements OnInit {
  public users: Array<any> = new Array<any>();
  public oneStudentForm: FormGroup;
  public mutipleStudentFile: File;
  public SERVER_URL = environment.SERVER_URL;
  public isCollapsed = true;
  public addOneStudentErrorMsg = '';
  public addOneStudentSuccessful: boolean = false;
  public addMutipleStudentErrorMsg = '';
  public addMutipleStudentSuccessful: boolean = false;
  constructor(private studentService: StudentManagementService, private fb: FormBuilder) { }

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
    this.studentService.getAllUserData().subscribe(response => {
      this.users = response.Users;
    });
  }

  public addOneStudent() {

    if (this.oneStudentForm.dirty && this.oneStudentForm.valid) {
      this.studentService.addOneStudent(this.oneStudentForm).subscribe(
        (response) => {
          this.getAllUser();
          this.addOneStudentSuccessful = true;
        },
        error => {
          this.addOneStudentErrorMsg = error.error;
        });
    }
  }

  changeFileLister(e: { target: { files: File[]; }; }) {
    this.mutipleStudentFile = e.target.files[0];
  }
  public addMutipleStudent() {
    if (this.mutipleStudentFile != null) {
      this.studentService.addMutipleStudent(this.mutipleStudentFile).subscribe(
        (response) => {
          this.getAllUser();
          this.addMutipleStudentSuccessful = true;
        },
        error => {
          this.addMutipleStudentErrorMsg = error.error;
        });
    }
  }

  public updateDisplay(username: string) {
    this.studentService.updateDisplay(username).subscribe(
      response => {
        this.getAllUser();
      },
      error => { }
    );
  }

}
