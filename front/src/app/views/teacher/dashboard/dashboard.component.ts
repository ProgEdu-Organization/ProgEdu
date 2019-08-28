import { Component, OnInit, NgModule } from '@angular/core';
import { allStudentDatas } from '../../../containers/default-layout/_nav';
import { DashboardService } from './dashboard.service';

@Component({
  templateUrl: 'dashboard.component.html'
})

export class DashboardComponent implements OnInit {
  public allStudentDatas = allStudentDatas;
  public data: Array<any> = new Array<any>();
  public tableHead: Array<any> = new Array<any>();
  public tableData: Array<any> = new Array<any>();
  public studentData: JSON;
  public search;
  constructor(private dashboardService: DashboardService) { }
  async ngOnInit() {
    await this.getAllAssignment();
    await this.getAllStudent();
  }

  async getAllAssignment() {
    this.dashboardService.getAllProjectName().subscribe(response => {
      this.tableHead = response.allAssignments;
    });
  }

  async getAllStudent() {
    // clear student array
    this.dashboardService.getAllStudent().subscribe(response => {
      console.log('test');
      console.log(response);
      this.studentData = response.allUsersCommitRecord;
      if (this.studentData[0] === undefined) {
        this.tableHead.length = 0;
      }
    });
  }
}
