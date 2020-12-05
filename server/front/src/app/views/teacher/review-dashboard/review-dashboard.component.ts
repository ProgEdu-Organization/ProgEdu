import { Component, OnInit } from '@angular/core';
import { allStudentDatas } from '../../../containers/default-layout/_nav';
import { ReviewDashboardService } from './review-dashboard.service';

@Component({
  selector: 'app-review-dashboard',
  templateUrl: './review-dashboard.component.html',
  styleUrls: ['./review-dashboard.component.scss']
})
export class ReviewDashboardComponent implements OnInit {

  public allStudentDatas = allStudentDatas;
  public data: Array<any> = new Array<any>();
  public assignmentTable: Array<any> = new Array<any>();
  public allStudentCommitRecord: JSON;
  public search;
  constructor(private dashboardService: ReviewDashboardService) { }
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
      this.allStudentCommitRecord = response.allReviewedRecord;
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
