import { StudentEvent } from '../../../services/student-event';
import { ReviewRoundStudashboardService } from './review-round-studashboard.service';
import { Component, OnInit } from '@angular/core';
import { JwtService } from '../../../services/jwt.service';
import { User } from '../../../models/user';
import { TimeService } from '../../../services/time.service';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { StudentEventsService } from '../../../services/student-events-log.service';

@Component({
  selector: 'app-review-round-studashboard',
  templateUrl: './review-round-studashboard.component.html',
  styleUrls: ['./review-round-studashboard.component.scss']
})

export class ReviewRoundStudashboardComponent implements OnInit {

  public assignmentTable: Array<any> = new Array<any>();
  public reviewDetail: Array<any> = new Array<any>();
  public reviewStatus: Array<any> = new Array<any>();
  public studentCommitRecord: JSON;
  public username: string;
  public assignmentName: string;
  public round = [1];
  constructor(private reviewRoundStudashboardService: ReviewRoundStudashboardService, private route: ActivatedRoute, 
    private timeService: TimeService,private jwtService?: JwtService, private router?: Router, 
    private studentEventsService?: StudentEventsService) {
    this.emitStudentEvent();
  }

  emitStudentEvent() {
    // review status dashboard viewed event emit
    const viewed_event: StudentEvent = {
      name: 'progedu.dashboard.review_status.viewed',
      page: this.router.url,
      event: {}
    };
    this.studentEventsService.createReviewRecord(viewed_event);
  }

  async ngOnInit() {
    this.username = new User(this.jwtService).getUsername();
    this.assignmentName = this.route.snapshot.queryParamMap.get('assignmentName');
    await this.getAllAssignments();
    await this.getStudentCommitRecords();
    await this.getReviewDetail();
  }

  async getAllAssignments() {
    this.reviewRoundStudashboardService.getAllAssignments().subscribe(response => {
      this.assignmentTable = response.allReviewAssignments;
    });
  }

  async getStudentCommitRecords() {
    // clear student array
    this.reviewRoundStudashboardService.getStudentCommitRecord(this.username).subscribe(response => {
      this.studentCommitRecord = response;
    });
  }

  async getReviewDetail() {
    this.reviewRoundStudashboardService.getReviewDetail(this.username, this.assignmentName).subscribe(response => {
      this.reviewDetail = response.allStatusDetail;
    })
  }

  async getReviewRoundStatus() {
    this.reviewRoundStudashboardService.getReviewRoundStatus(this.username, this.assignmentName).subscribe(response => {
      this.reviewStatus = response;
    })
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
    if (JSON.stringify(commit) !== '{}') {
      return false;
    }
    return true;
  }


}
