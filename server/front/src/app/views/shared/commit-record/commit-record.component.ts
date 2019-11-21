import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-commit-record',
  templateUrl: './commit-record.component.html',
  styleUrls: ['./commit-record.component.scss']
})
export class CommitRecordComponent implements OnInit {
  @Input() type: string;
  @Input() commits: JSON;
  @Input() feedbacks: JSON;
  @Output() messageToEmit = new EventEmitter<string>();

  constructor() { }

  ngOnInit() {
  }

  updateFeedback(commitNumber: string) {
    this.messageToEmit.emit(commitNumber);
  }

}
