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
  public roundStatus: Array<any> = new Array<any>();
  public round: number = 0;
  public studentCommitRecord: JSON;
  public username: string;
  public assignmentName: string;
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
    //await this.getStudentCommitRecords();
    await this.getRoundStatus();
    
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

  async getRoundStatus() {
    await this.reviewRoundStudashboardService.getRoundStatus(this.username, this.assignmentName).subscribe(response => {
      this.roundStatus = response;
      this.round = this.roundStatus[0].round;
    });
  }

  getStatus(status: string, endTime: Date) {
    const now_time = new Date().getTime();
    const deadline = new Date(endTime).getTime();
    if (status == "COMPLETED") {
      return status;
    } else if (status == "INIT") {
      if (now_time >= deadline) {
        return "UNCOMPLETED";
      } else {
        return "INIT";
      }
    }
    return "INIT";
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
    /*if (JSON.stringify(commit.commitRecord) !== '{}') {
      return false;
    }*/
    if (JSON.stringify(commit) !== '{}') {
      return false;
    }
    return true;
  }


}
