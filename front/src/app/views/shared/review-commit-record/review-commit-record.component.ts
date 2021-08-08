import { ReviewCommitRecordService } from './review-commit-record.service';
import { Component, OnInit, OnChanges, Input, Output, EventEmitter } from '@angular/core';
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
  constructor(private reviewCommitRecordService: ReviewCommitRecordService) { }

  ngOnInit() {}

  ngOnChanges() {
    if (this.commits.length > 0) {
      this.changePagination(this.currentPagination);
      this.maxPagination = Math.ceil(this.commits.length / commitRow);
    }
    const count = Object(this.reviewFeedbacks).length;
    this.currentReviewPagination = new Array(count);
    this.maxReviewPagination = new Array(count);
    for (let i = 0 ; i < count ; i++) {
      this.currentReviewPagination[i] = 1;
      this.maxReviewPagination[i] = this.reviewFeedbacks[i].totalCount;
      if (this.maxReviewPagination[i] === undefined) {
        this.maxReviewPagination[i] = 1;
      }
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

  nextReviewPage(index: number) { // Todo 好像根本沒用到
    if (this.currentReviewPagination[index] >= this.maxReviewPagination[index]) {
      return;
    }
    this.currentReviewPagination[index]++;
    this.changeReviewPagination(this.currentReviewPagination[index], index);
  }

  preReviewPage(index: number) { // // Todo 好像根本沒用到
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
    });
  }

  openReviewFeedbackModal(detail: JSON) {
    this.onClickedReviewDetail = detail;
  }


  updateFeedback(commitNumber: number) {
    this.commitNumber = commitNumber;
    this.messageToEmit.emit(commitNumber.toString());
  }

  isShowScreenshot(): Boolean {
    return (this.type === 'WEB' || this.type === 'ANDROID');
  }

}
