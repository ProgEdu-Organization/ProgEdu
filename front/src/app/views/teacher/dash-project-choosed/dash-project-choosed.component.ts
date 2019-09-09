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
  assignment = { type: '' };
  commits: Array<JSON> = [];
  feedback: string;
  selectedCommitNumber: number;
  screenshotUrls: Array<String>;
  isCollapsed = true;
  constructor(private route: ActivatedRoute, private dashProjectService: DashProjectChoosedService) { }

  ngOnInit() {
    this.username = this.route.snapshot.queryParamMap.get('username');
    this.assignmentName = this.route.snapshot.queryParamMap.get('assignmentName');
    this.getCommitDetail();
    this.getAssignment();
  }


  getCommitDetail() {
    this.dashProjectService.getCommitDetail(this.assignmentName, this.username).subscribe(response => {
      this.commits = response;
      this.selectedCommitNumber = this.commits.length;
      this.getFeedback();
      this.getScreenshotUrls();
    });
  }

  getAssignment() {
    this.dashProjectService.getAssignment(this.assignmentName).subscribe(response => {
      this.assignment = response;
    });
  }

  getFeedback() {

    this.dashProjectService.getFeedback(this.assignmentName, this.username, this.commits.length).subscribe(
      response => {
        this.feedback = response.message;
      },
      error => {
        console.log(error);
      },
    );
  }

  updateFeedback(commmitNumber: number) {
    this.dashProjectService.getFeedback(this.assignmentName, this.username, commmitNumber).subscribe(
      response => {
        this.feedback = response.message;
        this.selectedCommitNumber = commmitNumber;
      },
      error => {
        console.log(error);
      },
    );
  }

  getScreenshotUrls() {
    console.log(this.selectedCommitNumber);
    this.dashProjectService.getScreenshotUrls(this.username, this.assignmentName, this.selectedCommitNumber).subscribe(
      (resopnse) => {
        this.screenshotUrls = resopnse.urls;
      }
    );
  }

}
