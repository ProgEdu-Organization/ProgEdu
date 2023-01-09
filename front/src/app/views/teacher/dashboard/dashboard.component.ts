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
  public allStudentCommitRecord: Array<any> = new Array<any>();
  public search;
  public readonly now_time = Date.now() - (new Date().getTimezoneOffset() * 60 * 1000);
  public examIndex: Array<any> = new Array<any>();
  constructor(private dashboardService: DashboardService) { }
  async ngOnInit() {
    await this.getAllAssignments();
    await this.getAllStudent();
  }

  async getAllAssignments() {
    this.dashboardService.getAllAssignments().subscribe(response => {
      this.assignmentTable = response.allAssignments;
      //remove exam data
      for(let i = 0; i < this.assignmentTable.length; i++) {
        if(this.assignmentTable[i].type == "EXAM") {
          this.examIndex.push(i);
          this.assignmentTable.splice(i, 1);
          i--;
        }
      }
    });
  }

  async getAllStudent() {
    // clear student array
    this.dashboardService.getAllStudentCommitRecord().subscribe(response => {

      this.allStudentCommitRecord = response.allUsersCommitRecord;
      if (this.allStudentCommitRecord[0] === undefined) {
        this.assignmentTable.length = 0;
      }

      for(let i = 0; i < this.allStudentCommitRecord.length; i++) {
        for(let j = 0; j < this.examIndex.length; j++) {
          this.allStudentCommitRecord[i].commitRecord.splice(this.examIndex[j], 1);
        }
      }

    });
  }

  isNA(commit: any) {
    if (JSON.stringify(commit.commitRecord) !== '{}') {
      return false;
    }
    return true;
  }

  isExam(index: number) {
    if(!this.assignmentTable[index]) {
      return true;
    }
    return false;
  }

  getStatusString(index: number) {
    let createTime = this.assignmentTable[index].createTime;
    let assessmentTimes = this.assignmentTable[index].assessmentTimes;
    
    if (this.now_time > Date.parse(assessmentTimes[assessmentTimes.length - 1].endTime)) {
      return "已結束"
    } else {
      if (this.now_time < Date.parse(assessmentTimes[0].startTime)) {
        return "未開放";
      } else {
        return "進行中";
      }
    }
  }

  getStatus(index: number) {
    let createTime = this.assignmentTable[index].createTime;
    let assessmentTimes = this.assignmentTable[index].assessmentTimes;
    
    if (this.now_time > Date.parse(assessmentTimes[assessmentTimes.length - 1].endTime)) {
      return "end"
    } else {
      if (this.now_time < Date.parse(assessmentTimes[0].startTime)) {
        return "not_allow";
      } else {
        return "ongoing";
      }
    }
  }

}
