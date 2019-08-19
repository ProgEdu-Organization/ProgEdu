import { Component, OnInit } from '@angular/core';
import { StudentManagementService } from './student-management.service';
import { environment } from '../../../../environments/environment';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
@Component({
  selector: 'app-student-management',
  templateUrl: './student-management.component.html'
})
export class StudentManagementComponent implements OnInit {
  public users: Array<any> = new Array<any>();
  public addOneStudentForm: FormGroup;
  public SERVER_URL = environment.SERVER_URL;
  constructor(private studentService: StudentManagementService, private fb: FormBuilder) { }
  async ngOnInit() {
    await this.getAllUserData();
    this.addOneStudentForm = this.fb.group({
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
      this.users = response.results;
    });
  }

  public addOneStudent() {
    console.log(this.addOneStudentForm.value);
  }

}
