import { ReviewStatusStudashboardService } from './review-status-studashboard.service';
import { Component, OnInit } from '@angular/core';
import { JwtService } from '../../../services/jwt.service';
import { User } from '../../../models/user';
import { TimeService } from '../../../services/time.service';
import { StudentEventsService } from './../../../services/student-events-log.service';
import { Router } from '@angular/router';



@Component({
  selector: 'app-review-status-studashboard',
  templateUrl: './review-status-studashboard.component.html',
  styleUrls: ['./review-status-studashboard.component.scss']
})
export class ReviewStatusStudashboardComponent implements OnInit {

  public assignmentTable: Array<any> = new Array<any>();
  public studentCommitRecord: JSON;
  public username: string;
  constructor(private reviewStatusStudashboardService: ReviewStatusStudashboardService, private timeService: TimeService,
    private jwtService?: JwtService, private router?: Router, private studentEventsService?: StudentEventsService) {
  }

  async ngOnInit() {
    this.username = new User(this.jwtService).getUsername();
    // review status dashboard viewed event emit
    const exit_event = {
      event: 'progedu.dashboard.review_status.viewed', event_type: 'view', context: 'progedu.dashboard.review_status.viewed',
      username: this.username, page: this.router.url, time: new Date().toISOString()
    };
    this.studentEventsService.createReviewRecord(exit_event).subscribe();
    await this.getAllAssignments();
    await this.getStudentCommitRecords();
  }

  async getAllAssignments() {
    this.reviewStatusStudashboardService.getAllAssignments().subscribe(response => {
      this.assignmentTable = response.allReviewAssignments;
    });
  }

  async getStudentCommitRecords() {
    // clear student array
    this.reviewStatusStudashboardService.getStudentCommitRecord(this.username).subscribe(response => {
      this.studentCommitRecord = response;
    });
  }

  isRelease(release: Date) {
    const now_time = new Date().getTime();
    const realease_time = new Date(this.timeService.getUTCTime(release)).getTime();
    if (now_time >= realease_time) {
      return true;
    }
    return false;
  }

  isNA(commit: any) {
    if (JSON.stringify(commit.commitRecord) !== '{}') {
      return false;
    }
    return true;
  }


}
