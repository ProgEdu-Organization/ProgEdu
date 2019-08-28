import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CarouselConfig } from 'ngx-bootstrap/carousel';
import { DashProjectChoosedService } from './dash-project-choosed.service';
/*
example: http://localhost:4200/#/dashboard/dashprojectchoosed?userId=3&proName=WEB-HW5
*/
@Component({
  selector: 'app-dash-project-choosed',
  templateUrl: './dash-project-choosed.component.html',
  providers: [
    { provide: CarouselConfig, useValue: { interval: 1500, noPause: false } },
  ]
})
export class DashProjectChoosedComponent implements OnInit {
  username: string;
  assignmentName: string;
  assignment;
  commits: Array<JSON> = [];
  constructor(private route: ActivatedRoute, private dashProjectService: DashProjectChoosedService) { }

  ngOnInit() {
    this.username = this.route.snapshot.queryParamMap.get('username');
    this.assignmentName = this.route.snapshot.queryParamMap.get('assignmentName');
    this.getCommitDetail();
    this.getAssignment();
  }

  async getCommitDetail() {
    this.dashProjectService.getCommitDetail(this.assignmentName, this.username).subscribe(response => {
      this.commits = response;
    });
  }

  async getAssignment() {
    this.dashProjectService.getAssignment(this.assignmentName).subscribe(response => {
      this.assignment = response;
    });
  }

  async getFeedback(commitNumber) {
    this.dashProjectService.getFeedback(this.assignmentName, this.username, commitNumber).subscribe(
      response => {

      },
      error => { },
    );
  }

}
