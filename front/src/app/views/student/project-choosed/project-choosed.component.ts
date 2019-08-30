import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProjectChoosedService } from './project-choosed.service';

@Component({
  selector: 'app-project-choosed',
  templateUrl: './project-choosed.component.html'
})
export class ProjectChoosedComponent implements OnInit {

  username: string;
  assignmentName: string;
  assignment = { type: '' };
  commits: Array<JSON> = [];
  gitlabAssignmentURL: string;
  feedback: string;
  constructor(private route: ActivatedRoute, private projectService: ProjectChoosedService) { }

  ngOnInit() {
    this.username = this.route.snapshot.queryParamMap.get('username');
    this.assignmentName = this.route.snapshot.queryParamMap.get('assignmentName');
    this.getAssignment();
    this.getCommitDetail();
    this.getFeedback();
    // ex http://140.134.26.71:20008/victor6666/hoky3

    this.gitlabAssignmentURL = `test`;
  }

  async getCommitDetail() {
    this.projectService.getCommitDetail(this.assignmentName, this.username).subscribe(response => {
      this.commits = response;
    });
  }
  async getAssignment() {
    await this.projectService.getAssignment(this.assignmentName).subscribe(response => {
      this.assignment = response;
    });
  }

  public copyToClipboard() {
    const copyBox = document.createElement('textarea');
    copyBox.value = this.gitlabAssignmentURL;

    document.body.appendChild(copyBox);
    copyBox.focus();
    copyBox.select();
    document.execCommand('copy');
    document.body.removeChild(copyBox);
    console.log('copy successful');
  }

  async getFeedback() {
    this.projectService.getFeedback(this.assignmentName, this.username, '1').subscribe(
      response => {
        this.feedback = response.message;
      }
    );
  }

}
