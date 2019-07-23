import { Component, OnInit } from '@angular/core';
import { StudentManagementService } from './student-management.service';
@Component({
  selector: 'app-student-management',
  templateUrl: './student-management.component.html'
})
export class StudentManagementComponent implements OnInit {

  public users: Array<any> = new Array<any>();
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
