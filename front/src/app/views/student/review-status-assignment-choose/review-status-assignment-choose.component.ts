import { FormControl } from '@angular/forms';
import { StudentEvent } from '../../../services/student-event';
import { environment } from './../../../../environments/environment.prod';
import { ReviewRecord } from './ReviewRecord';
import { ReviewStatusAssignmentChooseService } from './review-status-assignment-choose.service';
import { Component, OnInit, ViewChild, ViewChildren, OnChanges, Renderer2 } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { StudentEventsService } from '../../../services/student-events-log.service';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { JwtService } from '../../../services/jwt.service';
import { HttpClient } from '@angular/common/http';
import { AddJwtTokenHttpClient } from '../../../services/add-jwt-token.service';
import { PeerReviewAPI } from '../../../api/PeerReviewAPI';

@Component({
  selector: 'app-review-status-assignment-choose',
  templateUrl: './review-status-assignment-choose.component.html',
  styleUrls: ['./review-status-assignment-choose.component.scss']
})
export class ReviewStatusAssignmentChooseComponent implements OnInit {

  username: string;
  assignmentName: string;
  round: string;
  order: string;
  allReviewDetail: JSON;
  reviewMetrics: JSON; // Queston
  currentReviewPagination: Array<number>;
  maxReviewPagination: number[];
  onClickedReviewDetail: any;
  metricsCount: number;
  reviewRecords: Array<ReviewRecord>;
  reviewOne: number;
  feedbackInputLast: any;
  feedbackInit: boolean = false;
  submitDisabled: boolean = true;
  @ViewChildren('radioYes') public reviewYesRadio: any;
  @ViewChildren('radioNo') public reviewNoRadio: any;
  @ViewChildren('feedbackInput') public feedbackInput: any;
  @ViewChild('reviewFormFillModal', { static: false }) public reviewFormFillModal: ModalDirective;
  @ViewChild('reviewFormModal', { static: false }) public reviewFormModal: ModalDirective;

  errorResponse: HttpErrorResponse;
  errorTitle: string;

  constructor(private route: ActivatedRoute, private reviewStatusAssignmentChooseService: ReviewStatusAssignmentChooseService,
    private addJwtTokenHttpClient: AddJwtTokenHttpClient, private router?: Router, private studentEventsService?: StudentEventsService, private renderer?: Renderer2) {
  }

  emitStudentEvent(event: StudentEvent) {
    this.studentEventsService.createReviewRecord(event);
  }
  emitReviewFormOpenedEvent() {
    // progedu review_status review_form opened event emit
    const review_form_event: StudentEvent = {
      name: 'progedu.review_status.review_form.opened',
      page: this.router.url,
      event: { assignment_name: this.assignmentName, reviewed_name: this.allReviewDetail[this.reviewOne].name }
    };
    this.emitStudentEvent(review_form_event);
    const oldFeedbacks = this.feedbackInput.toArray();
    this.feedbackInputLast = new Array(oldFeedbacks.length);
    for (let i = 0; i < Object.keys(oldFeedbacks).length; i++) {
      this.feedbackInputLast[i] = oldFeedbacks[i].nativeElement.value;
    }
  }

  async ngOnInit() {
    this.username = this.route.snapshot.queryParamMap.get('username');
    this.assignmentName = this.route.snapshot.queryParamMap.get('assignmentName');
    this.round = this.route.snapshot.queryParamMap.get('round');
    this.order = this.route.snapshot.queryParamMap.get('order');
    // review status assignment viewed event emit
    const viewed_event: StudentEvent = {
      name: 'progedu.review_status.assignment.viewed',
      page: this.router.url,
      event: { assignment_name: this.assignmentName }
    };
    this.emitStudentEvent(viewed_event);
    // review record
    /*this.reviewStatusAssignmentChooseService.getReviewDetail(this.username, this.assignmentName).subscribe(response => {
      this.allReviewDetail = response.allStatusDetail;
      const count = Object(this.allReviewDetail).length;
      this.currentReviewPagination = new Array(count);
      this.maxReviewPagination = new Array(count);
      for (let i = 0; i < count; i++) {
        this.currentReviewPagination[i] = 1;
        this.maxReviewPagination[i] = this.allReviewDetail[i].totalCount;
      }
    });*/
    // review round record
    this.reviewStatusAssignmentChooseService.getReviewRoundDetail(this.username, this.assignmentName, this.round, this.order).subscribe(response => {
      this.allReviewDetail = response.roundStatusDetail;
    });
    // get review metrics
    this.reviewStatusAssignmentChooseService.getReviewMetrics(this.assignmentName).subscribe(response => {
      this.reviewMetrics = response.allMetrics;
      this.metricsCount = Object.keys(this.reviewMetrics).length;
      // reviewFormFill hide listener
      this.reviewFormFillModal.onHidden.subscribe(
        res => {
          // review status review form canceled
          const review_form_event: StudentEvent = {
            name: 'progedu.review_status.review_form.canceled',
            page: this.router.url,
            event: { assignment_name: this.assignmentName, reviewed_name: this.allReviewDetail[this.reviewOne].name }
          };
          this.emitStudentEvent(review_form_event);
        }
      );
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
    const review_form_event: StudentEvent = {
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
    this.emitStudentEvent(review_form_event);
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
    const review_form_event: StudentEvent = {
      name: 'progedu.review_status.review_form.answer.filled',
      page: this.router.url,
      event: {
        assignment_name: this.assignmentName,
        reviewed_name: this.allReviewDetail[this.reviewOne].name,
        metrics: metrics.metrics,
        answer: answer
      }
    };
    this.emitStudentEvent(review_form_event);
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

  nextReviewPage(index: number) { // Todo 沒用到
    if (this.currentReviewPagination[index] >= this.maxReviewPagination[index]) {
      return;
    }
    this.currentReviewPagination[index]++;
    this.changeReviewPagination(this.currentReviewPagination[index], index);
  }

  preReviewPage(index: number) { // Todo 沒用到
    if (this.currentReviewPagination[index] <= 1) {
      return;
    }
    this.currentReviewPagination[index]--;
    this.changeReviewPagination(this.currentReviewPagination[index], index);
  }

  changeReviewPagination(pageNumber: number, index: number) { // Todo 沒用到
    if (pageNumber <= 0 || pageNumber > this.maxReviewPagination[index]) {
      return;
    }
    this.reviewStatusAssignmentChooseService.getReviewStatusPageDetail(this.assignmentName, this.username,
      this.allReviewDetail[index].id.toString(),
      pageNumber.toString()).subscribe(response => {
        this.allReviewDetail[index] = response;
      });
  }

  openReviewFeedbackModal(detail: any) {
    this.onClickedReviewDetail = detail;
  }

  reviewFeedbackEvent(detail: any, reviewed_name: any) {
    // review status review record feedback viewed
    const review_form_event: StudentEvent = {
      name: 'progedu.review_status.review_record.feedback.viewed',
      page: this.router.url,
      event: {
        assignment_name: this.assignmentName, reviewed_name: reviewed_name,
        record_detail: detail
      }
    };
    this.emitStudentEvent(review_form_event);
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
    this.reviewStatusAssignmentChooseService.createReviewRecord(this.username, this.allReviewDetail[this.reviewOne].name,
      this.assignmentName, { allReviewRecord: this.reviewRecords }, this.round, false).subscribe(
        response => {
          window.location.reload();
        },
        error => {
          this.errorTitle = 'This assignment has been expired';
          this.errorResponse = error;
        }
      );
    // progedu review_status review_form opened event emit
    const review_form_event: StudentEvent = {
      name: 'progedu.review_status.review_form.submitted',
      page: this.router.url,
      event: {
        assignment_name: this.assignmentName, reviewed_name: this.allReviewDetail[this.reviewOne].name,
        review_record: this.reviewRecords
      }
    };
    this.emitStudentEvent(review_form_event);
  }

  downloadSourceCode() {
    // progedu review_status review_form source_code downloaded event emit
    const review_form_event: StudentEvent = {
      name: 'progedu.review_status.review_form.source_code.downloaded',
      page: this.router.url,
      event: { assignment_name: this.assignmentName, reviewed_name: this.allReviewDetail[this.reviewOne].name }
    };
    this.emitStudentEvent(review_form_event);

    let downloadApi = this.addJwtTokenHttpClient.get(PeerReviewAPI.getSourceCode + '?username='
    + this.allReviewDetail[this.reviewOne].name + '&assignmentName=' + this.assignmentName);

    downloadApi.subscribe(function (res){
      window.open(res.url, '_blank');
    });
  }
  setReviewOne(index: number) {
    this.reviewOne = index;
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
