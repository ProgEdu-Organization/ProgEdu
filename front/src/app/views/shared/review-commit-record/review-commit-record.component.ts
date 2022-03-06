import { ReviewCommitRecordService } from './review-commit-record.service';
import { ReviewStatusAssignmentChooseService } from '../../student/review-status-assignment-choose/review-status-assignment-choose.service';
import { Component, OnInit, OnChanges, Input, Output, EventEmitter, ViewChild, ViewChildren } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { AddJwtTokenHttpClient } from '../../../services/add-jwt-token.service';
import { HttpErrorResponse } from '@angular/common/http';
import { PeerReviewAPI } from '../../../api/PeerReviewAPI';
import { ReviewRecord } from '../../student/review-status-assignment-choose/ReviewRecord';

const commitRow = 5;

@Component({
  selector: 'app-review-commit-record',
  templateUrl: './review-commit-record.component.html',
  styleUrls: ['./review-commit-record.component.scss']
})
export class ReviewCommitRecordComponent implements OnInit, OnChanges {

  @Input() type: string;
  @Input() showCommiter: boolean = false;
  @Input() commits: Array<any>;
  @Input() feedbacks: JSON;
  @Input() screenshotUrls: Array<any>;
  @Output() messageToEmit = new EventEmitter<string>();
  @Input() reviewFeedbacks: JSON;
  @Input() assignmentName: string;
  @Input() username: string;
  @Input() isTeacher: boolean;
  displayCommits: Array<any>;
  currentPagination: number = 1;
  currentReviewPagination: Array<number>;
  maxReviewPagination: number[];
  maxPagination: number;
  commitNumber = 1;
  onClickedReviewDetail: JSON;
  onClickedFeedbackRound: number;
  onClickedMetricOrder: number;
  reviewMetrics: JSON;
  metricsCount: number;
  errorResponse: HttpErrorResponse;
  errorTitle: string;
  reviewRecords: Array<ReviewRecord>;
  feedbackInputLast: any;
  feedbackInit: boolean = false;
  submitDisabled: boolean = true;
  feedbackSubmitDisabled: boolean = true;
  reviewOne: number;
  isShowReviewButton: Array<boolean>;
  @ViewChildren('radioYes') public reviewYesRadio: any;
  @ViewChildren('radioNo') public reviewNoRadio: any;
  @ViewChildren('radioFeedbackScore') public feedbackScoreRadio: any;
  @ViewChildren('feedbackInput') public feedbackInput: any;
  @ViewChild('reviewFormFillModal', { static: false }) public reviewFormFillModal: ModalDirective;
  @ViewChild('feedbackFormFillModal', { static: false }) public feedbackFormFillModal: ModalDirective;

  constructor(private reviewCommitRecordService: ReviewCommitRecordService, private addJwtTokenHttpClient: AddJwtTokenHttpClient,
    private reviewStatusAssignmentChooseService: ReviewStatusAssignmentChooseService) { }

  emitReviewFormOpenedEvent() {
    // progedu review_status review_form opened event emit
    /*const review_form_event: StudentEvent = {
      name: 'progedu.review_status.review_form.opened',
      page: this.router.url,
      event: { assignment_name: this.assignmentName, reviewed_name: this.allReviewDetail[this.reviewOne].name }
    };
    this.emitStudentEvent(review_form_event);*/
    const oldFeedbacks = this.feedbackInput.toArray();
    this.feedbackInputLast = new Array(oldFeedbacks.length);
    for (let i = 0; i < Object.keys(oldFeedbacks).length; i++) {
      this.feedbackInputLast[i] = oldFeedbacks[i].nativeElement.value;
    }
  }

  ngOnInit() {
    this.reviewStatusAssignmentChooseService.getReviewMetrics(this.assignmentName).subscribe(response => {
      this.reviewMetrics = response.allMetrics;
      this.metricsCount = Object.keys(this.reviewMetrics).length;
      // reviewFormFill hide listener
      /*this.reviewFormFillModal.onHidden.subscribe(
        res => {
          // review status review form canceled
          const review_form_event: StudentEvent = {
            name: 'progedu.review_status.review_form.canceled',
            page: this.router.url,
            event: { assignment_name: this.assignmentName, reviewed_name: this.allReviewDetail[this.reviewOne].name }
          };
          this.emitStudentEvent(review_form_event);
        }
      );*/
    });
  }

  ngOnChanges() {
    if (this.commits.length > 0) {
      this.changePagination(this.currentPagination);
      this.maxPagination = Math.ceil(this.commits.length / commitRow);
    }
    const count = Object(this.reviewFeedbacks).length;
    this.currentReviewPagination = new Array(count);
    this.maxReviewPagination = new Array(count);
    this.isShowReviewButton = new Array(count);
    for (let i = 0 ; i < count ; i++) {
      this.currentReviewPagination[i] = 1;
      if(this.reviewFeedbacks[i].latestCompletedRound !== undefined) {
        if(this.reviewFeedbacks[i].latestCompletedRound !== 0) {
          this.currentReviewPagination[i] = this.reviewFeedbacks[i].latestCompletedRound;
        } else {
          this.currentReviewPagination[i] = 1;
        }
      }
      this.maxReviewPagination[i] = this.reviewFeedbacks[i].totalCount;
      if (this.maxReviewPagination[i] === undefined) {
        this.maxReviewPagination[i] = 1;
      }
      this.isShowReviewButton[i] = !this.reviewFeedbacks[i].status;
    }
  }

  nextPage() {
    if (this.currentPagination >= this.maxPagination) {
      return;
    }
    this.currentPagination++;
    this.changePagination(this.currentPagination);
  }

  prePage() {
    if (this.currentPagination <= 1) {
      return;
    }
    this.currentPagination--;
    this.changePagination(this.currentPagination);
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


  changePagination(pageNumber: number) {
    if (pageNumber <= 0 || pageNumber > this.maxPagination) {
      return;
    }

    if (this.commits.length >= 6) {
      this.displayCommits = this.commits.slice((this.currentPagination - 1) * commitRow, pageNumber * commitRow);
    } else {
      this.displayCommits = this.commits;
    }
  }

  changeReviewPagination(pageNumber: number, index: number) { // Todo 好像根本沒用到
    if (pageNumber <= 0 || pageNumber > this.maxReviewPagination[index]) {
      return;
    }
    this.reviewCommitRecordService.getReviewPageDetail(this.assignmentName, this.username, this.reviewFeedbacks[index].id.toString(),
     pageNumber.toString() ).subscribe(response => {
      this.reviewFeedbacks[index] = response;
      this.checkIsShowReviewButton(index);
    });
  }

  openReviewFeedbackModal(round: number, detail: JSON, metricOrder: number) {
    this.onClickedReviewDetail = detail;
    this.onClickedFeedbackRound = round;
    this.onClickedMetricOrder = metricOrder;
  }


  updateFeedback(commitNumber: number) {
    this.commitNumber = commitNumber;
    this.messageToEmit.emit(commitNumber.toString());
  }

  isShowScreenshot(): Boolean {
    return (this.type === 'WEB' || this.type === 'ANDROID');
  }

  downloadSourceCode() {
    // progedu review_status review_form source_code downloaded event emit

    let downloadApi = this.addJwtTokenHttpClient.get(PeerReviewAPI.getSourceCode + '?username='
    + this.username + '&assignmentName=' + this.assignmentName);

    downloadApi.subscribe(function (res){
      window.open(res.url, '_blank');
    });
  }

  feedbackChanged(event, id, metrics: any) {
    const before_feedback = String(this.feedbackInputLast[id]);
    const after_feedback = String(event.target.value);
    let action_type = '';
    if (after_feedback.length > before_feedback.length) {
      action_type = 'add';
    } else if (after_feedback.length < before_feedback.length) {
      action_type = 'delete';
    } else {
      action_type = 'edit';
    }
    this.feedbackInputLast[id] = event.target.value;
    // emit feedback filled event
    // review status review form feedback filled
    /*const review_form_event: StudentEvent = {
      name: 'progedu.review_status.review_form.feedback.filled',
      page: this.router.url,
      event: {
        assignment_name: this.assignmentName,
        reviewed_name: this.allReviewDetail[this.reviewOne].name,
        metrics: metrics.metrics,
        action_type: action_type,
        before_feedback: before_feedback,
        after_feedback: after_feedback
      }
    };
    this.emitStudentEvent(review_form_event);*/
    this.checkReviewForm();
  }

  answerChanged(event, metrics: any) {
    let answer;
    // filled answer yes
    if (event.target.id === 'methodRadio1') {
      answer = 'Yes';
    } else {
      answer = 'No';
    }
    // emit answer filled event
    // review status review form answer filled
    /*const review_form_event: StudentEvent = {
      name: 'progedu.review_status.review_form.answer.filled',
      page: this.router.url,
      event: {
        assignment_name: this.assignmentName,
        reviewed_name: this.allReviewDetail[this.reviewOne].name,
        metrics: metrics.metrics,
        answer: answer
      }
    };
    this.emitStudentEvent(review_form_event);*/
    this.checkReviewForm();
  }

  checkReviewForm() {
    // check if answer is empty
    let i = 0;
    const yesRadios = this.reviewYesRadio.toArray();
    const noRadios = this.reviewNoRadio.toArray();
    for (i = 0 ; i < yesRadios.length ; i++ ) {
      if (yesRadios[i].nativeElement.checked === false && noRadios[i].nativeElement.checked === false) {
          this.submitDisabled = true;
          return;
      }
    }
    // check if feedback is empty
    for (i = 0 ; i < this.feedbackInputLast.length ; i++) {
      if (noRadios[i].nativeElement.checked === true && this.feedbackInputLast[i] === '') {
        this.submitDisabled = true;
        return;
      }
    }
    if ( i === yesRadios.length ) {
      this.submitDisabled = false;
    }
  }

  checkFeedbackForm() {
    let i = 0;
    const feedbackScoreRadios = this.feedbackScoreRadio.toArray();
    for (i = 0; i < feedbackScoreRadios.length; i++) {
      if (feedbackScoreRadios[i].nativeElement.checked === true) {
        this.feedbackSubmitDisabled = false;
        return;
      }
    }
    this.feedbackSubmitDisabled = true;
  }

  createReviewForm() {
    const feedbacks = this.feedbackInput.toArray();
    const yesRadios = this.reviewYesRadio.toArray();
    const noRadios = this.reviewNoRadio.toArray();
    this.reviewRecords = new Array<ReviewRecord>(this.metricsCount);
    const nowDate = new Date();
    for (let i = 0; i < this.metricsCount; i++) {
      const reviewRecord: ReviewRecord = {
        feedback: feedbacks[i].nativeElement.value,
        id: this.reviewMetrics[i].id, score: (yesRadios[i].nativeElement.checked === true ? 1 : 2), time: nowDate.toLocaleString()
      };
      this.reviewRecords[i] = reviewRecord;
    }
    this.reviewStatusAssignmentChooseService.createReviewRecord(this.reviewFeedbacks[this.reviewOne].name, this.username,
      this.assignmentName, { allReviewRecord: this.reviewRecords }, this.currentReviewPagination[this.reviewOne].toString(), this.isTeacher).subscribe(
        response => {
          window.location.reload();
        },
        error => {
          this.errorTitle = 'Create record failed.';
          this.errorResponse = error;
        }
      );
    // progedu review_status review_form opened event emit
    /*const review_form_event: StudentEvent = {
      name: 'progedu.review_status.review_form.submitted',
      page: this.router.url,
      event: {
        assignment_name: this.assignmentName, reviewed_name: this.allReviewDetail[this.reviewOne].name,
        review_record: this.reviewRecords
      }
    };
    this.emitStudentEvent(review_form_event);*/
  }

  createFeedbackForm() {
    const feedbackScoreRadios = this.feedbackScoreRadio.toArray();
    let score = -1;
    for(let i = 0; i < feedbackScoreRadios.length; i++) {
      if(feedbackScoreRadios[i].nativeElement.checked === true) {
        score = i;
      }
    }
    this.reviewStatusAssignmentChooseService.createFeedbackScore(this.assignmentName, this.username, this.reviewFeedbacks[this.reviewOne].id,
      this.onClickedFeedbackRound, this.reviewMetrics[this.onClickedMetricOrder].id, score).subscribe(
        response => {
          window.location.reload();
        },
        error => {
          this.errorTitle = 'Create feedback score failed.';
          this.errorResponse = error;
        }
    );
  }

  setReviewOne(index: number) {
    this.reviewOne = index;
  }

  checkIsShowReviewButton(order: number) {
    if(Array(this.reviewFeedbacks[order].Detail)[0] == undefined || Array(this.reviewFeedbacks[order].Detail)[0].length == []) {
      this.isShowReviewButton[order] = true;
    } else {
      this.isShowReviewButton[order] = false;
    }
  }

  getFeedbackScoreDetail(score: number) {
    switch(score) {
      case 0:
        return "沒有意義的審查意見"
      case 1:
        return "較沒有幫助的審查意見"
      case 2:
        return "一般的審查意見"
      case 3:
        return "有幫助的審查意見"
      case 4:
        return "非常有幫助且詳細的審查意見"
      default:
        return ""
    }
  }

}
