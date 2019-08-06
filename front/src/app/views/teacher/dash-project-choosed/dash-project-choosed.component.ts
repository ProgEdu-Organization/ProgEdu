import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CarouselConfig } from 'ngx-bootstrap/carousel';
import { DashProjectChoosedService } from './dash-project-choosed.service';
/*
example: http://localhost:4200/#/dashboard/dashprojectchoosed?userId=3&proName=WEB-HW5
*/
@Component({
  selector: 'app-dash-project-choosed',
  templateUrl: './dash-project-choosed.component.html',
  providers: [
    { provide: CarouselConfig, useValue: { interval: 1500, noPause: false } },
  ]
})
export class DashProjectChoosedComponent implements OnInit {
  gitlabId: string;
  proName: string;
  commitData: Array<JSON> = [];
  constructor(private route: ActivatedRoute, private dashProjectService: DashProjectChoosedService) { }

  ngOnInit() {
    this.gitlabId = this.route.snapshot.queryParamMap.get('gitlabId');
    this.proName = this.route.snapshot.queryParamMap.get('proName');
    this.getCommitData();
    this.getFeedback();
  }

  async getCommitData() {
    this.dashProjectService.getCommitData().subscribe(response => {
      this.commitData.push(response);
      console.log(response);
    });
  }

  async getFeedback() {
    const url = 'http://140.134.26.71:58321/job/D0350510_WEB-HW5/1/console';
  }

}
