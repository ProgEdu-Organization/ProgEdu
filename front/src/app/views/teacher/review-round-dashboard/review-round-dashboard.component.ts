import { ReviewRoundDashboardService } from './review-round-dashboard.service';
import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { allStudentDatas } from '../../../containers/default-layout/_nav';


@Component({
  selector: 'app-review-round-dashboard',
  templateUrl: './review-round-dashboard.component.html',
  styleUrls: ['./review-round-dashboard.component.scss']
})
export class ReviewRoundDashboardComponent implements OnInit {

  public assignmentName: string;
  public allStudentDatas = allStudentDatas;
  public data: Array<any> = new Array<any>();
  public assignmentTable: Array<any> = new Array<any>();
  public round: Array<any> = [];
  public allReviewRoundStatus: Array<any> = new Array<any>();
  public allStudentCommitRecord: JSON;
  public search;
  constructor(private route: ActivatedRoute, private dashboardService: ReviewRoundDashboardService) { }
  async ngOnInit() {
    this.assignmentName = this.route.snapshot.queryParamMap.get('assignmentName');
    //await this.getAllAssignments();
    //await this.getAllStudent();
    await this.getAllStudentReviewRoundStatus();
  }

  async getAllAssignments() {
    this.dashboardService.getAllAssignments().subscribe(response => {
      this.assignmentTable = response.allReviewAssignments;
    });
  }

  async getAllStudent() {
    // clear student array
    this.dashboardService.getAllStudentCommitRecord().subscribe(response => {
      this.allStudentCommitRecord = response.allReviewStatus;
      if (this.allStudentCommitRecord[0] === undefined) {
        this.assignmentTable.length = 0;
      }
    });
  }

  async getAllStudentReviewRoundStatus() {
    this.dashboardService.getReviewRoundStauts(this.assignmentName).subscribe(response => {
      this.allReviewRoundStatus = response;
      this.round = [];
      for(let i = 0; i < this.allReviewRoundStatus[0].reviewRound.length; i++) {
        this.round.push(i);
      }
    })
  }

  getStatus(status: string, amount: number, count: number) {
    if (status == "COMPLETED") {
      if (count < amount) {
        return "POR";
      } else {
        return status;
      }
    } else {
      return status;
    }
  }
  
  isNA(commit: any) {
    if (JSON.stringify(commit.commitRecord) !== '{}') {
      return false;
    }
    return true;
  }
}