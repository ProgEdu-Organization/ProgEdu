import { ReviewStatusDashboardService } from './../review-status-dashboard/review-status-dashboard.service';
import { ReviewDashboardComponent } from './../review-dashboard/review-dashboard.component';
import { Component, OnInit } from '@angular/core';
import { allStudentDatas } from '../../../containers/default-layout/_nav';


@Component({
  selector: 'app-review-status-dashboard',
  templateUrl: './review-status-dashboard.component.html',
  styleUrls: ['./review-status-dashboard.component.scss']
})
export class ReviewStatusDashboardComponent implements OnInit {


  public allStudentDatas = allStudentDatas;
  public data: Array<any> = new Array<any>();
  public assignmentTable: Array<any> = new Array<any>();
  public allStudentCommitRecord: JSON;
  public search;
  constructor(private dashboardService: ReviewStatusDashboardService) { }
  async ngOnInit() {
    await this.getAllAssignments();
    await this.getAllStudent();
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
  isNA(commit: any) {
    if (JSON.stringify(commit.commitRecord) !== '{}') {
      return false;
    }
    return true;
  }
}
