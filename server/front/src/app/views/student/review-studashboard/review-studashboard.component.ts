import { StudentEvent } from './../../../services/student-event';

import { ReviewStudashboardService } from './review-studashboard.service';
import { Component, OnInit } from '@angular/core';
import { JwtService } from '../../../services/jwt.service';
import { User } from '../../../models/user';
import { TimeService } from '../../../services/time.service';
import { Router } from '@angular/router';
import { StudentEventsService } from '../../../services/student-events-log.service';


@Component({
  selector: 'app-review-studashboard',
  templateUrl: './review-studashboard.component.html',
  styleUrls: ['./review-studashboard.component.scss']
})
export class ReviewStudashboardComponent implements OnInit {

  public assignmentTable: Array<any> = new Array<any>();
  public studentCommitRecord: JSON;
  public username: string;
  constructor(private reviewStudashboardService: ReviewStudashboardService, private timeService: TimeService,
    private jwtService?: JwtService, private router?: Router, private studentEventsService?: StudentEventsService) {
      this.emitStudentEvent();
  }
  emitStudentEvent() {
    // review record dashboard viewed event emit
    const event: StudentEvent = {
      name: 'progedu.dashboard.review_record.viewed',
      page: this.router.url,
      event: {}
    };
    this.studentEventsService.createReviewRecord(event);
  }
  async ngOnInit() {
    this.username = new User(this.jwtService).getUsername();
    await this.getAllAssignments();
    await this.getStudentCommitRecords();
  }

  async getAllAssignments() {
    this.reviewStudashboardService.getAllAssignments().subscribe(response => {
      this.assignmentTable = response.allReviewAssignments;
    });
  }

  async getStudentCommitRecords() {
    // clear student array
    this.reviewStudashboardService.getStudentCommitRecord(this.username).subscribe(response => {
      this.studentCommitRecord = response;
    });
  }

  isRelease(release: Date) {
    const now_time = new Date().getTime();
    const realease_time = new Date(release).getTime();
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
