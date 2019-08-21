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
  public OneStudentForm: FormGroup;
  public MutipleStudentFile: File;
  public SERVER_URL = environment.SERVER_URL;
  constructor(private studentService: StudentManagementService, private fb: FormBuilder) { }

  async ngOnInit() {
    await this.getAllUserData();
    this.OneStudentForm = this.fb.group({
      name: ['', Validators.pattern('^[a-zA-Z0-9-_]{5,20}')],
      username: ['', Validators.pattern('^[a-zA-Z0-9-_]{5,20}')],
      password: ['', Validators.pattern('^[a-zA-Z0-9-_]{5,20}')],
      email: ['', Validators.pattern('^[a-zA-Z0-9-_]{5,20}')],
      isDisplayed: [true],
      rememberMe: [true]
    });
  }
  async getAllUserData() {
    this.studentService.getAllUserData().subscribe(response => {
      this.users = response.Users;
    });
  }

  public addOneStudent() {
    console.log('add one student' + environment.SERVER_URL);

    this.studentService.addOneStudent(this.OneStudentForm).subscribe(
      (response) => {
        console.log('Sul');
      },
      error => {
        $('#confirmPassword-invalid-feedbacks').show().text(error.error);
        console.log(error.error);
      });
  }

  changeFileLister(e: { target: { files: File[]; }; }) {
    console.log(e.target.files[0]);
    this.MutipleStudentFile = e.target.files[0];
  }
  public addMutipleStudent() {

    this.studentService.addMutipleStudent(this.MutipleStudentFile).subscribe(
      (response) => {
        console.log('Sul');
      },
      error => {
        console.log(error.error);
      });
  }

}
