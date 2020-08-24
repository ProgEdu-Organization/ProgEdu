import { Component, OnInit, OnChanges, Input, Output, EventEmitter } from '@angular/core';
import { AssignmentChoosedService } from '../assignment-choose/assignment-choose.service'
import { TimeService } from '../../../services/time.service'
import { ActivatedRoute } from '@angular/router';
const commitRow = 5;
@Component({
  selector: 'app-commit-record',
  templateUrl: './commit-record.component.html',
  styleUrls: ['./commit-record.component.scss']
})
export class CommitRecordComponent implements OnInit, OnChanges {
  @Input() type: string;
  @Input() showCommiter: boolean = false;
  @Input() commits: Array<any>;
  @Input() feedbacks: JSON;
  @Input() screenshotUrls: Array<any>;
  @Output() messageToEmit = new EventEmitter<string>();
  assignmentName: string;
  username: string;
  displayCommits: Array<any>;
  currentPagination: number = 1;
  maxPagination: number;
  commitNumber;
  totalCommits: number;
  constructor(private route: ActivatedRoute, private assignmentService: AssignmentChoosedService, 
    private timeService: TimeService) { }

  ngOnInit() { 
    this.username = this.route.snapshot.queryParamMap.get('username');
    this.assignmentName = this.route.snapshot.queryParamMap.get('assignmentName');
    if (this.commitNumber == null) {
      this.commitNumber = this.commits[0].totalCommit;
      this.updateFeedback(this.commitNumber);
    }
  }

  ngOnChanges() {
    if (this.commits.length > 0) {
      this.changePagination(this.currentPagination);
      this.maxPagination = Math.ceil(this.commits[0].totalCommit / commitRow);
    }
    if (this.commitNumber == null) {
      this.commitNumber = this.commits[0].totalCommit;
      this.totalCommits = this.commits[0].totalCommit;
      //this.updateFeedback(this.commitNumber);
    }
  }

  getPartCommitDetail() {
    this.assignmentService.getPartCommitDetail(this.assignmentName, this.username, this.currentPagination.toString()).subscribe(response => {
      this.commits = response;
      if (this.commits) {
        for (const commit in this.commits) {
          if (commit) {
            this.commits[commit].time = this.timeService.getUTCTime(this.commits[commit].time);
          }
        }
        this.commits.reverse();
        this.displayCommits = this.commits ;
      }
    });
  }

  /*nextPage() {
    if (this.currentPagination >= this.maxPagination) {
      return;
    }
    this.currentPagination++;
    this.changePagination(this.currentPagination);
    this.getPartCommitDetail();
  }

  prePage() {
    if (this.currentPagination == 1) {
      return;
    }
    this.currentPagination--;
    this.changePagination(this.currentPagination);
    this.getPartCommitDetail();
  }

  firstPage() {
    if (this.currentPagination == 1) {
      return;
    }
    this.currentPagination = 1;
    this.changePagination(this.currentPagination);
    this.getPartCommitDetail();
  }

  lastPage() {
    if (this.currentPagination == this.maxPagination) {
      return;
    }
    this.currentPagination = this.maxPagination;
    this.changePagination(this.currentPagination);
    this.getPartCommitDetail();
  }*/

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

  pageChanged(event) {
    this.currentPagination = event.page;
    this.getPartCommitDetail();
  }


  updateFeedback(commitNumber: number) {
    this.commitNumber = commitNumber;
    this.messageToEmit.emit(commitNumber.toString());
  }

  isShowScreenshot(): Boolean {
    return (this.type === 'WEB' || this.type === 'ANDROID');
  }

}

