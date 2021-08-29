import { ReviewRoundDashboardService } from './review-round-dashboard.service';
import { ReviewDashboardComponent } from './../review-dashboard/review-dashboard.component';
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
  public allStudentCommitRecord: JSON;
  public search;
  constructor(private route: ActivatedRoute, private dashboardService: ReviewRoundDashboardService) { }
  async ngOnInit() {
    this.assignmentName = this.route.snapshot.queryParamMap.get('assignmentName');
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