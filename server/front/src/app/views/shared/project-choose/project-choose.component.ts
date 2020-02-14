import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TimeService } from '../../../services/time.service';
import { ProjectChoosedService } from './project-choose.service';
import { ModalDirective } from 'ngx-bootstrap/modal';
@Component({
  selector: 'app-project-choose',
  templateUrl: './project-choose.component.html',
  styleUrls: []
})
export class ProjectChooseComponent implements OnInit {
  @ViewChild('screenshotModal', { static: true }) public screenshotModal: ModalDirective;

  public group;
  public groupName;

  public projectName;
  public projectType;
  public gitlabprojectUrl: string;

  public commits: Array<any> = [];
  public feedbacks;
  public selectedCommitNumber;
  public screenshotUrls: Array<string>;

  constructor(private activeRoute: ActivatedRoute, private projectService: ProjectChoosedService,
    private timeService: TimeService) { }

  ngOnInit() {
    this.groupName = this.activeRoute.snapshot.queryParamMap.get('groupName');
    this.projectName = this.activeRoute.snapshot.queryParamMap.get('projectName');
    this.getCommitResult();
    this.getGroup(this.groupName);
    this.getProjectUrl(this.groupName, this.projectName);
  }

  getCommitResult() {
    this.projectService.getCommitResult(this.groupName, this.projectName).subscribe(
      (resopnse) => {
        this.commits = resopnse;
        this.selectedCommitNumber = this.commits.length;
        this.getFeedback();
        if (this.isShowScreenshot()) {
          this.getScreenshotUrls();
        }
        if (this.commits) {
          for (const commit in this.commits) {
            if (commit) {
              this.commits[commit].time = this.timeService.getUTCTime(this.commits[commit].time);
            }
          }
          this.commits.reverse();
        }
      }
    );
  }

  public copyToClipboard() {
    const copyBox = document.createElement('textarea');
    copyBox.value = this.gitlabprojectUrl;

    document.body.appendChild(copyBox);
    copyBox.focus();
    copyBox.select();
    document.execCommand('copy');
    document.body.removeChild(copyBox);
  }

  getFeedback() {
    this.projectService.getFeedback(this.groupName, this.projectName, this.commits.length.toString()).subscribe(
      response => {
        this.feedbacks = response;
      },
      error => {
        console.log(error);
      }
    );
  }

  updateFeedback(commitNumber: string) {
    this.projectService.getFeedback(this.groupName, this.projectName, commitNumber).subscribe(
      response => {
        this.feedbacks = response;
        this.selectedCommitNumber = commitNumber;
        if (this.isShowScreenshot()) {
          this.getScreenshotUrls();
        }
      },
      error => {
        console.log(error);
      }
    );
  }

  getGroup(groupName: string) {
    this.projectService.getGroup(groupName).subscribe(
      response => {
        this.group = response;
        this.getProjectType(this.projectName);
      }
    );
  }

  getProjectUrl(groupName: string, projectName: string) {
    this.projectService.getProjectUrl(groupName, projectName).subscribe(
      response => {
        this.gitlabprojectUrl = response.url;
      }
    );
  }

  getProjectType(projectName: string) {
    if (this.group.project) {
      for (const p of this.group.project) {
        if (p.name === projectName) {
          this.projectType = p.type.typeName.toUpperCase();
        }
      }
    }
  }

  getScreenshotUrls() {
    if (this.group) {
      this.projectService.getScreenshotUrls(this.groupName, this.projectName, this.selectedCommitNumber).subscribe(
        (resopnse) => {
          this.screenshotUrls = resopnse.urls;
        }
      );
    }
  }

  isShowScreenshot(): Boolean {
    return this.projectType === 'WEB' || this.projectType === 'ANDROID';
  }
}
