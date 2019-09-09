import { Component, OnInit, NgModule } from '@angular/core';
import { allStudentDatas } from '../../../containers/default-layout/_nav';
import { DashboardService } from './dashboard.service';
import { controllers } from 'chart.js';

@Component({
  templateUrl: 'dashboard.component.html'
})

export class DashboardComponent implements OnInit {
  public allStudentDatas = allStudentDatas;
  public data: Array<any> = new Array<any>();
  public tableHead: Array<any> = new Array<any>();
  public allStudentCommitRecord: JSON;
  public search;
  constructor(private dashboardService: DashboardService) { }
  async ngOnInit() {
    await this.getAllAssignments();
    await this.getAllStudent();
  }

  async getAllAssignments() {
    this.dashboardService.getAllAssignments().subscribe(response => {
      this.tableHead = response.allAssignments;
    });
  }

  async getAllStudent() {
    // clear student array
    this.dashboardService.getAllStudentCommitRecord().subscribe(response => {
      this.allStudentCommitRecord = response.allUsersCommitRecord;
      if (this.allStudentCommitRecord[0] === undefined) {
        this.tableHead.length = 0;
      }
    });
  }
}
