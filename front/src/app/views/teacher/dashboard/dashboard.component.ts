import { Component, OnInit, NgModule } from '@angular/core';
import { DateFormatter } from 'ngx-bootstrap';
import { allStudentDatas } from '../../../containers/default-layout/_nav';
import { DashboardService } from './dashboard.service';

@Component({
  templateUrl: 'dashboard.component.html'
})

export class DashboardComponent implements OnInit {
  public allStudentDatas = allStudentDatas;
  public data: Array<any> = new Array<any>();
  public assignmentTable: Array<any> = new Array<any>();
  public allStudentCommitRecord: JSON;
  public search;
  constructor(private dashboardService: DashboardService) { }
  async ngOnInit() {
    await this.getAllAssignments();
    await this.getAllStudent();
  }

  async getAllAssignments() {
    this.dashboardService.getAllAssignments().subscribe(response => {
      this.assignmentTable = response.allAssignments;
    });
  }

  async getAllStudent() {
    console.log("franky-test   getAllStudent()");
    // clear student array
    this.dashboardService.getAllStudentCommitRecord().subscribe(response => {

      

      this.allStudentCommitRecord = response.allUsersCommitRecord;
      if (this.allStudentCommitRecord[0] === undefined) {
        this.assignmentTable.length = 0;
      }

    });
  }

  isNA(commit: any) {
    if (JSON.stringify(commit.commitRecord) !== '{}') {
      return false;
    }
    return true;
  }

  getStatusString(index: number) {
    let createTime = this.assignmentTable[index].createTime;
    let assessmentTimes = this.assignmentTable[index].assessmentTimes;
    const now_time = Date.now() - (new Date().getTimezoneOffset() * 60 * 1000);
    
    if (now_time > Date.parse(assessmentTimes[assessmentTimes.length - 1].endTime)) {
      return "已結束"
    } else {
      if (now_time < Date.parse(assessmentTimes[0].startTime)) {
        return "未開放";
      } else {
        return "進行中";
      }
    }
  }

  getStatus(index: number) {
    let createTime = this.assignmentTable[index].createTime;
    let assessmentTimes = this.assignmentTable[index].assessmentTimes;
    const now_time = Date.now() - (new Date().getTimezoneOffset() * 60 * 1000);
    
    if (now_time > Date.parse(assessmentTimes[assessmentTimes.length - 1].endTime)) {
      return "end"
    } else {
      if (now_time < Date.parse(assessmentTimes[0].startTime)) {
        return "not_allow";
      } else {
        return "ongoing";
      }
    }
  }

}
