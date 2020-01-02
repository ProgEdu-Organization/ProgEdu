import { Component, OnInit, SystemJsNgModuleLoader } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AssignmentChoosedService } from './assignment-choosed.service';
import { TimeService } from '../../../services/time.service'

@Component({
  selector: 'app-assignment-choosed',
  templateUrl: './assignment-choosed.component.html'
})
export class AssignmentChoosedComponent implements OnInit {

  username: string;
  assignmentName: string;
  assignment = { type: '', deadline: new Date() };
  commits: Array<any> = [];
  gitlabAssignmentURL: string;
  feedbacks: JSON;
  isCollapsed = true;
  selectedCommitNumber;
  selectedScreenshotName;
  screenshotUrls: Array<string>;

  constructor(private route: ActivatedRoute, private assignmentService: AssignmentChoosedService,
    private timeService: TimeService) { }

  async ngOnInit() {
    this.username = this.route.snapshot.queryParamMap.get('username');
    this.assignmentName = this.route.snapshot.queryParamMap.get('assignmentName');
    await this.getAssignment();
    await this.getGitAssignmentURL();
    await this.getCommitDetail();

    // this.selectedScreenshotName = $('#screenshot:visible').attr('src');
    // console.log(this.selectedScreenshotName);
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

  updateScrennshotName() {
    const url_split = $('.screenshot:visible').attr('src').split('/');
    this.selectedScreenshotName = url_split[url_split.length - 1];
  }

  getCommitDetail() {
    this.assignmentService.getCommitDetail(this.assignmentName, this.username).subscribe(response => {
      this.commits = response;
      this.selectedCommitNumber = this.commits.length;
      this.getFeedback();
      if (this.commits) {
        for (const commit in this.commits) {
          if (commit) {
            this.commits[commit].time = this.timeService.getUTCTime(this.commits[commit].time);
          }
        }

        this.commits.reverse();

      }

      if (this.assignment.type === 'WEB') {
        this.getScreenshotUrls();
      }
    });
  }
  getAssignment() {
    this.assignmentService.getAssignment(this.assignmentName).subscribe(response => {
      this.assignment = response;
      this.assignment.deadline = this.timeService.getUTCTime(this.assignment.deadline);
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
        this.feedbacks = response;
      },
      error => {
        console.log(error);
      }
    );
  }

  updateFeedback(commitNumber: string) {
    this.assignmentService.getFeedback(this.assignmentName, this.username, commitNumber).subscribe(
      response => {
        this.feedbacks = response;
        this.selectedCommitNumber = commitNumber;
        this.getScreenshotUrls();
      },
      error => {
        console.log(error);
      }
    );
  }

  getScreenshotUrls() {
    if (this.assignment) {
      this.assignmentService.getScreenshotUrls(this.username, this.assignmentName, this.selectedCommitNumber).subscribe(
        (resopnse) => {
          this.screenshotUrls = resopnse.urls;
        }
      );
    }
  }
}
