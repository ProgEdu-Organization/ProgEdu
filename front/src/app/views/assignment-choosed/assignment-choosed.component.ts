import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AssignmentChoosedService } from './assignment-choosed.service';

@Component({
  selector: 'app-assignment-choosed',
  templateUrl: './assignment-choosed.component.html'
})
export class AssignmentChoosedComponent implements OnInit {

  username: string;
  assignmentName: string;
  assignment = { type: '' };
  commits: Array<JSON> = [];
  gitlabAssignmentURL: string;
  feedback: string;
  isCollapsed = true;
  selectedCommitNumber;
  screenshotUrls: Array<string>;

  constructor(private route: ActivatedRoute, private assignmentService: AssignmentChoosedService) { }

  ngOnInit() {
    this.username = this.route.snapshot.queryParamMap.get('username');
    this.assignmentName = this.route.snapshot.queryParamMap.get('assignmentName');
    this.getAssignment();
    this.getGitAssignmentURL();
    this.getCommitDetail();
    this.gitlabAssignmentURL = ``;
  }
  getGitAssignmentURL() {
    this.assignmentService.getGitAssignmentURL(this.assignmentName, this.username).subscribe(
      response => {
        this.gitlabAssignmentURL = response.url;
      },
      error => {
        console.log(error);
      });
  }

  getCommitDetail() {
    this.assignmentService.getCommitDetail(this.assignmentName, this.username).subscribe(response => {
      this.commits = response;
      this.selectedCommitNumber = this.commits.length;
      this.getFeedback();
      this.getScreenshotUrls();
    });
  }
  getAssignment() {
    this.assignmentService.getAssignment(this.assignmentName).subscribe(response => {
      this.assignment = response;
      console.log(this.assignment);
    });
  }

  public copyToClipboard() {
    const copyBox = document.createElement('textarea');
    copyBox.value = this.gitlabAssignmentURL;

    document.body.appendChild(copyBox);
    copyBox.focus();
    copyBox.select();
    document.execCommand('copy');
    document.body.removeChild(copyBox);
  }

  getFeedback() {
    this.assignmentService.getFeedback(this.assignmentName, this.username, this.commits.length.toString()).subscribe(
      response => {
        this.feedback = response.message;
      },
      error => {
        console.log(error);
      }
    );
  }

  updateFeedback(commitNumber: string) {
    this.assignmentService.getFeedback(this.assignmentName, this.username, commitNumber).subscribe(
      response => {
        this.feedback = response.message;
      },
      error => {
        console.log(error);
      }
    );
  }

  getScreenshotUrls() {
    console.log(this.selectedCommitNumber);
    this.assignmentService.getScreenshotUrls(this.username, this.assignmentName, this.selectedCommitNumber).subscribe(
      (resopnse) => {
        this.screenshotUrls = resopnse.urls;
      }
    );
  }
}
