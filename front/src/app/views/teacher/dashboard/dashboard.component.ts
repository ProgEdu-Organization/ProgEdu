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
  public studentDatas: JSON;
  public search;
  constructor(private dashboardService: DashboardService) { }
  async ngOnInit() {
    // Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    // Add 'implements OnInit' to the class.
    await this.getAllStudentData();
  }

  async getAllStudentData() {
    // clear student array
    this.dashboardService.getAllStudentData().subscribe(response => {
      console.log(response);
      this.studentDatas = response.allUsersCommitRecord;
      if (this.studentDatas[0] === undefined) {
        this.tableHead.length = 0;
      } else {
        this.tableHead = this.studentDatas[0].commits;
      }
    });
  }
}
