import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TimeService } from '../../../services/time.service'
import { AssignmentChoosedService } from './assignment-choose.service';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';

@Component({
  selector: 'app-assignment-choose',
  templateUrl: './assignment-choose.component.html'
})
export class AssignmentChooseComponent implements OnInit {
  username: string;
  assignmentName: string;

  assignment = { type: '', deadline: new Date() };
  commits: Array<any> = [];
  gitlabAssignmentURL: string;
  feedbacks: JSON;
  selectedCommitNumber;
  currentPage: string = "1";
  screenshotUrls: Array<string>;

  public Editor = ClassicEditor;
  public editorConfig = { toolbar: [] }

  constructor(private route: ActivatedRoute, private assignmentService: AssignmentChoosedService,
    private timeService: TimeService) { }

  async ngOnInit() {
    this.username = this.route.snapshot.queryParamMap.get('username');
    this.assignmentName = this.route.snapshot.queryParamMap.get('assignmentName');
    await this.getAssignment();
    await this.getGitAssignmentURL();
    //await this.getCommitDetail();
    await this.getPartCommitDetail();
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
      if (this.isWebOrAndroid()) {
        this.getScreenshotUrls();
      }
    });
  }

  getPartCommitDetail() {
    this.assignmentService.getPartCommitDetail(this.assignmentName, this.username, this.currentPage).subscribe(response => {
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
      if (this.isWebOrAndroid()) {
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
    copyBox.select();
    document.execCommand('copy');
    document.body.removeChild(copyBox);
  }

  getFeedback() {
    this.assignmentService.getFeedback(this.assignmentName, this.username, this.commits[0].totalCommit.toString()).subscribe(
      response => {
        this.feedbacks = response;
        for (let i in this.feedbacks) {
          this.feedbacks[i].message.replace('/\n/g', '<br />');
        }
      },
      error => {
        console.log(error);
      }
    );
  }

  isWebOrAndroid(): Boolean {
    return this.assignment.type === 'WEB' || this.assignment.type === 'ANDROID';
  }

  updateFeedback(commitNumber: string) {
    this.assignmentService.getFeedback(this.assignmentName, this.username, commitNumber).subscribe(
      response => {
        this.feedbacks = response;
        this.selectedCommitNumber = commitNumber;
        if (this.isWebOrAndroid()) {
          this.getScreenshotUrls();
        }
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
