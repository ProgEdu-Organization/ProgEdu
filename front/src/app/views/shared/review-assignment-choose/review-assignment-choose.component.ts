import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { User } from '../../../models/user';
import { JwtService } from '../../../services/jwt.service';
import { TimeService } from '../../../services/time.service';
import { ReviewAssignmentChooseService } from './review-assignment-choose.service';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
@Component({
  selector: 'app-review-assignment-choose',
  templateUrl: './review-assignment-choose.component.html',
  styleUrls: ['./review-assignment-choose.component.scss']
})
export class ReviewAssignmentChooseComponent implements OnInit {

  username: string;
  assignmentName: string;
  isTeacher: boolean = false;
  user: User;

  assignment = { type: '', deadline: new Date(), assessmentTimes: new Array([{}])};
  assignmentTimes: Array<any> = [{assessmentAction:"", startTime:"", endTime:""}];

  commits: Array<any> = [];
  gitlabAssignmentURL: string;
  feedbacks: JSON;
  selectedCommitNumber;
  screenshotUrls: Array<string>;
  reviewFeedbacks: [{assessmentTimes:[{startTime:"", endTime:""}]}];
  public readonly now_time = Date.now() - (new Date().getTimezoneOffset() * 60 * 1000);
  review

  public Editor = ClassicEditor;
  public editorConfig = { toolbar: [] };

  constructor(private route: ActivatedRoute, private assignmentService: ReviewAssignmentChooseService,
    private timeService: TimeService, private jwtService?: JwtService) { }

  async ngOnInit() {
    this.username = this.route.snapshot.queryParamMap.get('username');
    this.assignmentName = this.route.snapshot.queryParamMap.get('assignmentName');
    this.user = new User(this.jwtService);
    if (this.user.isTeacher) {
      this.isTeacher = true;
    } else {
      this.isTeacher = false;
    }

    await this.getAssignment();
    await this.getGitAssignmentURL();
    await this.getCommitDetail();
    await this.getReviewFeedBack();
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
        /*for (const commit in this.commits) {
          if (commit) {
            this.commits[commit].time = (this.commits[commit].time);
          }
        }*/
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
      this.assignmentTimes = response.assessmentTimes;
      //this.assignment.deadline = this.timeService.getUTCTime(this.assignment.deadline);
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
        for (let i in this.feedbacks) {
          this.feedbacks[i].message.replace('/\n/g', '<br />');
          console.log(this.feedbacks[i].message);
        }
      },
      error => {
        console.log(error);
      }
    );
  }
  getReviewFeedBack() {
    this.assignmentService.getReviewFeedback(this.assignmentName, this.username).subscribe(response => {
      this.reviewFeedbacks = response.allRecordDetail;
    });
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

  isCompleted(index: number){
    if(this.now_time > Date.parse(this.assignmentTimes[index].endTime)) {
      return "complete";
    } else {
      return "";
    }
  }

}
