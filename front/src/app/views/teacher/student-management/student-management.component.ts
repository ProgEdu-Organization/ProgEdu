import { Component, OnInit } from '@angular/core';
import { StudentManagementService } from './student-management.service';
import { environment } from '../../../../environments/environment';
@Component({
  selector: 'app-student-management',
  templateUrl: './student-management.component.html'
})
export class StudentManagementComponent implements OnInit {
  private SERVER_URL;
  public users: Array<any> = new Array<any>();
  constructor(private studentService: StudentManagementService) { }
  async ngOnInit() {
    await this.getAllUserData();
    this.SERVER_URL = environment.SERVER_URL;
  }
  async getAllUserData() {
    this.studentService.getAllUserData().subscribe(response => {
      this.users = response.results;
    });
  }
}
