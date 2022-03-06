import { Component, OnInit, OnChanges, Input, Output, EventEmitter } from '@angular/core';
import { AssignmentChoosedService } from '../assignment-choose/assignment-choose.service';
import { ProjectChoosedService } from '../project-choose/project-choose.service';
import { TimeService } from '../../../services/time.service';
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
  @Input() commits: Array<any> = [{totalCommit:""}];
  @Input() feedbacks: JSON;
  @Input() screenshotUrls: Array<any>;
  @Output() messageToEmit = new EventEmitter<string>();
  assignmentName: string;
  username: string;
  projectName: string;
  groupName: string;
  displayCommits: Array<any>;
  currentPagination: number = 1;
  maxPagination: number;
  commitNumber: number = 0;
  totalCommits: number;
  constructor(private route: ActivatedRoute, private assignmentService: AssignmentChoosedService, 
    private timeService: TimeService, private projectService: ProjectChoosedService) { }

  ngOnInit() { 
    this.username = this.route.snapshot.queryParamMap.get('username');
    this.assignmentName = this.route.snapshot.queryParamMap.get('assignmentName');
    this.groupName = this.route.snapshot.queryParamMap.get('groupName');
    this.projectName = this.route.snapshot.queryParamMap.get('projectName');
  }

  ngOnChanges() {
    if (this.commits.length > 0) {
      this.changePagination(this.currentPagination);
      this.maxPagination = Math.ceil(this.commits[0].totalCommit / commitRow);
    }
    if (this.commitNumber == 0 && this.commits[0].totalCommit !== undefined) {
      this.commitNumber = Number(this.commits[0].totalCommit);
      this.totalCommits = Number(this.commits[0].totalCommit);
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
        this.displayCommits = this.commits;
      }
    });
  }

  getPartCommitResult() {
    this.projectService.getPartCommitResult(this.groupName, this.projectName, this.currentPagination.toString()).subscribe(
      (resopnse) => {
        this.commits = resopnse;
        /*if (this.isShowScreenshot()) {
          this.getScreenshotUrls();
        }*/
        if (this.commits) {
          for (const commit in this.commits) {
            if (commit) {
              this.commits[commit].time = this.timeService.getUTCTime(this.commits[commit].time);
            }
          }
          this.commits.reverse();
          this.displayCommits = this.commits;
        }
      }
    );
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

  pageChanged(event) {
    this.currentPagination = event.page;
    if (this.commits[0].committer == null) {
      this.getPartCommitDetail();
    } else {
      this.getPartCommitResult();
    }
  }


  updateFeedback(commitNumber: number) {
    this.commitNumber = commitNumber;
    this.messageToEmit.emit(commitNumber.toString());
  }

  isShowScreenshot(): Boolean {
    return (this.type === 'WEB' || this.type === 'ANDROID');
  }

}

