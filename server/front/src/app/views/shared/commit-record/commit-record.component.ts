import { Component, OnInit, OnChanges, Input, Output, EventEmitter } from '@angular/core';
const commitRow = 5;
@Component({
  selector: 'app-commit-record',
  templateUrl: './commit-record.component.html',
  styleUrls: ['./commit-record.component.scss']
})
export class CommitRecordComponent implements OnInit, OnChanges {
  @Input() type: string;
  @Input() commits: Array<any>;
  @Input() feedbacks: JSON;
  @Output() messageToEmit = new EventEmitter<string>();
  displayCommits: Array<any>;
  currentPagination: number = 1;
  maxPagination: number;
  constructor() { }

  ngOnInit() { }

  ngOnChanges() {
    if (this.commits.length > 0) {
      this.changePagination(this.currentPagination);
      this.maxPagination = Math.ceil(this.commits.length / commitRow);
      console.log(this.maxPagination);
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


  updateFeedback(commitNumber: string) {
    this.messageToEmit.emit(commitNumber);
  }

}
