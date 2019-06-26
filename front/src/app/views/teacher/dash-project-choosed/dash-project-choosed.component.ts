import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpService } from '../../../services/http.service'
/*
example: http://localhost:4200/#/dashboard/dashprojectchoosed?userId=3&proName=WEB-HW5
*/
@Component({
  selector: 'app-dash-project-choosed',
  templateUrl: './dash-project-choosed.component.html',
  styleUrls: ['./dash-project-choosed.component.scss']
})
export class DashProjectChoosedComponent implements OnInit {
  gitlabId: string;
  proName: string;
  commitData: Array<JSON> = [];
  constructor(private route: ActivatedRoute,
    private http: HttpService) { }

  ngOnInit() {
    this.gitlabId = this.route.snapshot.queryParamMap.get('gitlabId');
    this.proName = this.route.snapshot.queryParamMap.get('proName');
    this.getCommitData();
    this.getFeedback();
  }

  async getCommitData() {
    let response = await this.http.getData('http://140.134.26.77:8080/ProgEdu/webapi/jenkins/buildDetail?num=1&proName=WEB-HW5&userName=D0350510');
    this.commitData.push(response);
    console.log(response);
  }

  async getFeedback() {

    let url = "http://140.134.26.71:58321/job/D0350510_WEB-HW5/1/console";
    let response = await this.http.postData('http://140.134.26.77:8080/ProgEdu/webapi/jenkins/getFeedbackInfo', url);
    console.log('post', response);
  }

}