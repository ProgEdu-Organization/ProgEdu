import { Component, OnInit } from '@angular/core';
import { StudentManagementService } from './student-management.service';
import { environment } from '../../../../environments/environment';
@Component({
  selector: 'app-student-management',
  templateUrl: './student-management.component.html'
})
export class StudentManagementComponent implements OnInit {
  public users: Array<any> = new Array<any>();
  public SERVER_URL = environment.SERVER_URL;
  constructor(private studentService: StudentManagementService) { }
  async ngOnInit() {
    await this.getAllUserData();
  }
  async getAllUserData() {
    this.studentService.getAllUserData().subscribe(response => {
      this.users = response.results;
    });
  }
}
