import { ReviewRecord } from './ReviewRecord';
import { ReviewStatusAssignmentChooseService } from './review-status-assignment-choose.service';
import { Component, OnInit, ViewChildren } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { now } from 'jquery';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-review-status-assignment-choose',
  templateUrl: './review-status-assignment-choose.component.html',
  styleUrls: ['./review-status-assignment-choose.component.scss']
})
export class ReviewStatusAssignmentChooseComponent implements OnInit {

  assignment = { type: '', deadline: new Date() };
  username: string;
  assignmentName: string;
  allReviewDetail: JSON;
  reviewMetrics: JSON; // Queston
  currentReviewPagination: Array<number>;
  maxReviewPagination: number[];
  onClickedReviewDetail: JSON;
  metricsCount: number;
  reviewRecords: Array<ReviewRecord>;
  reviewOne: number;
  @ViewChildren('radioYes') public reviewForm: any;
  @ViewChildren('feedbackInput') public feedbackInput: any;

  errorResponse: HttpErrorResponse;
  errorTitle: string;

  constructor(private route: ActivatedRoute, private reviewStatusAssignmentChooseService: ReviewStatusAssignmentChooseService) {
  }


  async ngOnInit() {
    this.username = this.route.snapshot.queryParamMap.get('username');
    this.assignmentName = this.route.snapshot.queryParamMap.get('assignmentName');
    // review log
    this.reviewStatusAssignmentChooseService.getReviewDetail(this.username, this.assignmentName).subscribe(response => {
      this.allReviewDetail = response.allStatusDetail;
      const count = Object(this.allReviewDetail).length;
      this.currentReviewPagination = new Array(count);
      this.maxReviewPagination = new Array(count);
      for (let i = 0; i < count; i++) {
        this.currentReviewPagination[i] = 1;
        this.maxReviewPagination[i] = this.allReviewDetail[i].totalCount;
      }
    });
    // get metrics
    this.reviewStatusAssignmentChooseService.getReviewMetrics(this.assignmentName).subscribe(response => {
      this.reviewMetrics = response.allMetrics;
      this.metricsCount = this.reviewMetrics.length;
    });

  }


  nextReviewPage(index: number) {
    if (this.currentReviewPagination[index] >= this.maxReviewPagination[index]) {
      return;
    }
    this.currentReviewPagination[index]++;
    this.changeReviewPagination(this.currentReviewPagination[index], index);
  }

  preReviewPage(index: number) {
    if (this.currentReviewPagination[index] <= 1) {
      return;
    }
    this.currentReviewPagination[index]--;
    this.changeReviewPagination(this.currentReviewPagination[index], index);
  }

  changeReviewPagination(pageNumber: number, index: number) {
    if (pageNumber <= 0 || pageNumber > this.maxReviewPagination[index]) {
      return;
    }
    this.reviewStatusAssignmentChooseService.getReviewStatusPageDetail(this.assignmentName, this.username,
      this.allReviewDetail[index].id.toString(),
      pageNumber.toString()).subscribe(response => {
        this.allReviewDetail[index] = response;
      });
  }

  openReviewFeedbackModal(detail: JSON) {
    this.onClickedReviewDetail = detail;
  }

  createReviewForm() {
    const feedbacks = this.feedbackInput.toArray();
    this.reviewRecords = new Array<ReviewRecord>(this.metricsCount);
    const nowDate = new Date();
    for (let i = 0; i < this.metricsCount; i++) {
      const reviewRecord: ReviewRecord = {
        feedback: feedbacks[i].nativeElement.value,
        id: this.reviewMetrics[i].id, score: 1, time: nowDate.getFullYear() + '-' + (nowDate.getUTCMonth() + 1)
          + '-' + (nowDate.getUTCDate()) + ' ' + nowDate.getHours() + ':' + nowDate.getMinutes() + ':' + nowDate.getSeconds()
      };
      this.reviewRecords[i] = reviewRecord;
    }
    this.reviewStatusAssignmentChooseService.createReviewRecord(this.username, this.allReviewDetail[this.reviewOne].name,
      this.assignmentName, { allReviewRecord: this.reviewRecords }).subscribe(
        response => {
        },
        error => {
          this.errorTitle = 'This assignment has been expired';
          this.errorResponse = error;
        }
        );
  }

  downloadSourceCode() {
    window.open('http://140.134.26.66:22000/webapi/peerReview/sourceCode?username='
      + this.username + '&assignmentName=' + this.assignmentName, '_blank');
  }
  setReviewOne(index: number) {
    this.reviewOne = index;
  }

}
