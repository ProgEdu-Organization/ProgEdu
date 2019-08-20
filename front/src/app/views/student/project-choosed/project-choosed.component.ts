import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CarouselConfig } from 'ngx-bootstrap/carousel';
import { ProjectChoosedService } from './project-choosed.service';
import { FormsModule } from '@angular/forms';
/*
example: http://localhost:4200/#/dashboard/dashprojectchoosed?userId=3&proName=WEB-HW5
*/
@Component({
  selector: 'app-project-choosed',
  templateUrl: './project-choosed.component.html'
})
export class ProjectChoosedComponent implements OnInit {

  gitlabId: string;
  proName: string;
  commitData: Array<JSON> = [];
  copyHwURL: string;
  constructor(private route: ActivatedRoute, private projectService: ProjectChoosedService) { }

  ngOnInit() {
    this.gitlabId = this.route.snapshot.queryParamMap.get('gitlabId');
    this.proName = this.route.snapshot.queryParamMap.get('proName');
    this.getCommitData();
    this.getFeedback();
    this.copyHwURL = 'http://140.134.26.71:58321/job/D0350510_WEB-HW3/1/artifact/target/screenshot/index.png';
  }

  async getCommitData() {
    this.projectService.getCommitData().subscribe(response => {
      this.commitData.push(response);
      console.log(response);
    });
  }

  public copyToClipboard() {
    const copyBox = document.createElement('textarea');
    copyBox.value = this.copyHwURL;

    document.body.appendChild(copyBox);
    copyBox.focus();
    copyBox.select();
    document.execCommand('copy');
    document.body.removeChild(copyBox);
    console.log('copy successful');
  }

  async getFeedback() {
    const url = 'http://140.134.26.71:58321/job/D0350510_WEB-HW5/1/console';
  }

}
