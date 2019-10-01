import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProjectChoosedService } from './project-choosed.service';
@Component({
  selector: 'app-project-choosed',
  templateUrl: './project-choosed.component.html',
  styleUrls: ['./project-choosed.component.scss']
})
export class ProjectChoosedComponent implements OnInit {
  public groupName;
  public projectName;
  public group;
  public commits: Array<any> = [];
  public feedback;
  public isCollapsed = true;
  public selectedCommitNumber;
  public selectedScreenshotName;
  public screenshotUrls: Array<string>;
  public gitlabprojectUrl: string;

  constructor(private activeRoute: ActivatedRoute, private projectService: ProjectChoosedService) { }

  ngOnInit() {
    this.groupName = this.activeRoute.snapshot.queryParamMap.get('groupName');
    this.projectName = this.activeRoute.snapshot.queryParamMap.get('projectName');
    this.getCommitResult();
    this.getGroupMembers(this.groupName);
    this.getProjectUrl(this.groupName, this.projectName);
  }

  getCommitResult() {
    this.projectService.getCommitResult(this.groupName, this.projectName).subscribe(
      (resopnse) => {
        this.commits = resopnse;
        this.getFeedback();
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
        this.feedback = response.message;
      },
      error => {
        console.log(error);
      }
    );
  }

  updateFeedback(commitNumber: string) {
    this.projectService.getFeedback(this.groupName, this.projectName, commitNumber).subscribe(
      response => {
        this.feedback = response.message;
        this.selectedCommitNumber = commitNumber;
        // this.getScreenshotUrls();
      },
      error => {
        console.log(error);
      }
    );
  }

  getGroupMembers(groupName: string) {
    this.projectService.getGroupMembers(groupName).subscribe(
      response => {
        console.log(response);
        this.group = response;
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

}
