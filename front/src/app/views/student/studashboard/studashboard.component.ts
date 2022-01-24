import { StudentEvent } from '../../../services/student-event';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { StudashboardService } from './studashboard.service';
import { JwtService } from '../../../services/jwt.service';
import { User } from '../../../models/user';
import { TimeService } from '../../../services/time.service';
import { StudentEventsService } from '../../../services/student-events-log.service';


@Component({
  selector: 'app-studashboard',
  templateUrl: './studashboard.component.html'
})
export class StudashboardComponent implements OnInit {
  public assignmentTable: Array<any> = [{assessmentTimeList:[{startTime:"", endTime:""}]}];
  public studentCommitRecord: JSON;
  public username: string;
  constructor(private studashboardService: StudashboardService, private timeService: TimeService,
              private jwtService?: JwtService, private router?: Router, private studentEventsService?: StudentEventsService) {
    this.emitStudentEvent();
  }

  emitStudentEvent() {
    // assignment dasgboard viewed event emit
    const event: StudentEvent = {
      name: 'progedu.dashboard.assignment.viewed',
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
    this.studashboardService.getAllAssignments().subscribe(response => {
      this.assignmentTable = response.allAutoAssessment;
    });
  }

  async getStudentCommitRecords() {
    // clear student array
    this.studashboardService.getStudentCommitRecord(this.username).subscribe(response => {
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
