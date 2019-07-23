import { Component, OnInit, NgModule } from '@angular/core';
import { allStudentDatas } from '../../../containers/default-layout/_nav'
import { DashboardService } from './dashboard.service';

@Component({
  templateUrl: 'dashboard.component.html'
})

export class DashboardComponent implements OnInit {
  public allStudentDatas = allStudentDatas;
  public data: Array<any> = new Array<any>();
  public tableHead: Array<any> = new Array<any>();
  public tableData: Array<any> = new Array<any>();
  private studentDatas: JSON;
  constructor(private dashboardService: DashboardService) { }
  async ngOnInit() {
    // Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    // Add 'implements OnInit' to the class.
    await this.getAllStudentData();
  }

  async getAllStudentData() {
    // clear student array
    this.dashboardService.getAllStudentData().subscribe(response => {
      this.studentDatas = response.result;
      this.tableHead = this.studentDatas[0].commits;
    });
  }
}
