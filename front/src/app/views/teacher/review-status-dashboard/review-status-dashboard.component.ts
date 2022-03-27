import { ReviewStatusDashboardService } from './../review-status-dashboard/review-status-dashboard.service';
import { ReviewDashboardComponent } from './../review-dashboard/review-dashboard.component';
import { Component, OnInit } from '@angular/core';
import { allStudentDatas } from '../../../containers/default-layout/_nav';
import { FormControl, FormGroup, FormArray, FormBuilder, Validators } from '@angular/forms';


@Component({
  selector: 'app-review-status-dashboard',
  templateUrl: './review-status-dashboard.component.html',
  styleUrls: ['./review-status-dashboard.component.scss']
})
export class ReviewStatusDashboardComponent implements OnInit {

  public allStudentDatas = allStudentDatas;
  public data: Array<any> = new Array<any>();
  public assignmentTable: Array<any> = new Array<any>();
  //public allStudentCommitRecord: JSON;
  public search;
  constructor(private dashboardService: ReviewStatusDashboardService) { }
  async ngOnInit() {
    await this.getAllAssignments();
    //await this.getAllStudent();
  }

  async getAllAssignments() {
    this.dashboardService.getAllAssignments().subscribe(response => {
      this.assignmentTable = response.allReviewAssignments;
    });
  }

  /*async getAllStudent() {
    // clear student array
    this.dashboardService.getAllStudentCommitRecord().subscribe(response => {
      this.allStudentCommitRecord = response.allReviewStatus;
      if (this.allStudentCommitRecord[0] === undefined) {
        this.assignmentTable.length = 0;
      }

    });
  }*/

  isNA(commit: any) {
    if (JSON.stringify(commit.commitRecord) !== '{}') {
      return false;
    }
    return true;
  }
  
  getStatus(index: number) {
    let createTime = this.assignmentTable[index].createTime;
    let assessmentTimes = this.assignmentTable[index].assessmentTimes;
    const now_time = Date.now() - (new Date().getTimezoneOffset() * 60 * 1000);
    
    if (now_time > Date.parse(assessmentTimes[assessmentTimes.length - 1].endTime)) {
      return "completed"
    } else {
      if (now_time < Date.parse(assessmentTimes[0].startTime)) {
        return "initialization";
      } else {
        return "por";
      }
    }
  }

  getNowRound(index: number) {
    const now_time = Date.now() - (new Date().getTimezoneOffset() * 60 * 1000);
    let assessmentTimes = this.assignmentTable[index].assessmentTimes;
    let round = 0;
    for(let i = 0; i < assessmentTimes.length; i+=2) {
      if (now_time < assessmentTimes[i].startTime) {
        break;
      } else {
        round++;
      }
    }
    return round;
  }

}
