import { Component, OnInit } from '@angular/core';
import { ChartService } from './chart.service';
import { Status } from './status';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html'
})

export class ChartComponent implements OnInit {
  public commits : Array<any>;
  public labels: string[] = [];
  public barChartData: any[] = [
    {
      data: [], label: Status.notBuild.name,
      backgroundColor: Status.notBuild.color,
      borderColor: Status.notBuild.color,
      hoverBackgroundColor: Status.notBuild.color,
      hoverBorderColor: Status.notBuild.color,
    },
    {
      data: [], label: Status.compilerFailure.name,
      backgroundColor: Status.compilerFailure.color,
      borderColor: Status.compilerFailure.color,
      hoverBackgroundColor: Status.compilerFailure.color,
      hoverBorderColor: Status.compilerFailure.color,
    },
    {
      data: [], label: Status.codingStyleError.name,
      backgroundColor: Status.codingStyleError.color,
      borderColor: Status.codingStyleError.color,
      hoverBackgroundColor: Status.codingStyleError.color,
      hoverBorderColor: Status.codingStyleError.color,
    },
    {
      data: [], label: Status.testFailure.name,
      backgroundColor: Status.testFailure.color,
      borderColor: Status.testFailure.color,
      hoverBackgroundColor: Status.testFailure.color,
      hoverBorderColor: Status.testFailure.color,
    },
    {
      data: [], label: Status.success.name,
      backgroundColor: Status.success.color,
      borderColor: Status.success.color,
      hoverBackgroundColor: Status.success.color,
      hoverBorderColor: Status.success.color,
    },
  ];

  public mixedChartData: Array<any> = [
    {
      data: [], label: Status.notBuild.name, fill: false, type: 'line',
      backgroundColor: Status.notBuild.color,
      borderColor:Status.notBuild.color,
      hoverBackgroundColor: Status.notBuild.color,
      hoverBorderColor:Status.notBuild.color,
    },
    {
      data: [], label: Status.compilerFailure.name, fill: false, type: 'line',
      backgroundColor: Status.compilerFailure.color,
      borderColor:Status.compilerFailure.color,
      hoverBackgroundColor: Status.compilerFailure.color,
      hoverBorderColor:Status.compilerFailure.color,
    },
    {
      data: [], label: Status.codingStyleError.name, fill: false, type: 'line',
      backgroundColor: Status.codingStyleError.color,
      borderColor:Status.codingStyleError.color,
      hoverBackgroundColor: Status.codingStyleError.color,
      hoverBorderColor:Status.codingStyleError.color,
    },
    {
      data: [], label: Status.testFailure.name, fill: false, type: 'line',
      backgroundColor: Status.testFailure.color,
      borderColor:Status.testFailure.color,
      hoverBackgroundColor: Status.testFailure.color,
      hoverBorderColor:Status.testFailure.color,
    },
    {
      data: [], label: Status.success.name, fill: false,type: 'line',
      backgroundColor: Status.success.color, 
      borderColor:Status.success.color
    },
    { data: [], label: 'Count',
      backgroundColor: '#ced2d8',
      borderColor:'#ced2d8'
    }
  ];

  public bubbleChartData: any[] = [
    { data: [{x: 0, y: 17, z: 5},{x: 0, y: 17, z: 5}], label: 'HW1', radius: 20, type: 'bubble' },
  ];
  public bubbleChartLabels: Array<any> = [];

  constructor(private chartService: ChartService) { }

  ngOnInit() {
    this.chartService.getAllCommits().subscribe(
      (response) => {
        console.log(response);
        /////////////////////////////////////////////////////
        this.commits = response.allCommitRecord;
        this.getStatusResultData();
        this.getMixedChartData();
        this.getBubbleChartData();
      },
      (error)=>{
        console.log('get all assignments error');
      }
    );
  }

  computeStatus(commits: any) {
    let statusCount = [0, 0, 0, 0, 0];
    if(commits){
      for(let i=0; i< commits.length; i++){
        if(Status.notBuild.status.includes(commits[i].status)){
          statusCount[0] = statusCount[0] + 1;
        }  else if (Status.compilerFailure.status.includes(commits[i].status)){
          statusCount[1] = statusCount[1] + 1;
        } else if (Status.codingStyleError.status.includes(commits[i].status)){
          statusCount[2] = statusCount[2] + 1;
        } else if (Status.testFailure.status.includes(commits[i].status)){
          statusCount[3] = statusCount[3] + 1;
        } else if (Status.success.status.includes(commits[i].status)){
          statusCount[4] = statusCount[4] + 1;
        } 
      };
    }
    return statusCount;
  }

  getStatusResultData() {
    this.labels = [];
    for(let i=0; i < this.commits.length; i++){
      this.labels.push(this.commits[i].name);
      let statusCount = this.computeStatus(this.commits[i].commits);
      for(let j = 0; j < this.barChartData.length; j++){
        this.barChartData[j].data.push(statusCount[j]);
      }
    }
  }

  getMixedChartData() {
    this.labels = [];
    for(let i=0; i < this.commits.length; i++){
      this.labels.push(this.commits[i].name);
      let statusCount = this.computeStatus(this.commits[i].commits);
      for(let j = 0; j < this.barChartData.length; j++){
        this.mixedChartData[j].data.push(statusCount[j]);
      }
      this.mixedChartData[this.mixedChartData.length - 1].data.push(this.commits[i].commits.length);
    }
  }

  getBubbleChartData() {
    let commitTime : Array<any> = [];
    let map: Array<any> = [];
    let chartStartTime :Date;
    let chartFinishTime :Date;
    for(let i=0; i < this.commits.length; i++){
      this.bubbleChartData[i] = {data: [], label: this.commits[i].name, type: 'bubble'}
      if(i === 0) {
        chartStartTime = new Date(this.commits[i].releaseTime);
        chartFinishTime = new Date(this.commits[i].deadline);
      } else {
        if(chartStartTime.getTime() > new Date(this.commits[i].releaseTime).getTime()) {
          chartStartTime = new Date(this.commits[i].releaseTime);
        }
        if(chartFinishTime.getTime() < new Date(this.commits[i].deadline).getTime()) {
          chartFinishTime = new Date(this.commits[i].deadline);
        }
      }
    }
    this.bubbleChartLabels = Array.from(this.getCommitTime(chartStartTime, chartFinishTime));
    for(let i=0; i < this.commits.length; i++){
        for(let j=0; j < this.commits[i].commits.length; j++){
          let date = new Date(this.commits[i].commits[j].time);
          let time = ((date.getMonth() + 1) + '-' + date.getDate());
          this.bubbleChartData[i].data.push({x: this.bubbleChartLabels.indexOf(time), y: date.getHours(), z: 5});
        }
    }

    console.log(this.labels);
    console.log(commitTime);
  }



  getCommitTime(releaseTime: Date, deadline: Date) {
    //
    let set: Set<any> = new Set();
    let time_diff: number = deadline.getTime() - releaseTime.getTime();
    for(let i=0; i <= time_diff; i += 86400000){
      var date = new Date(releaseTime.getTime() + i);
      set.add((date.getMonth() + 1) +'-'+ date.getDate());
      // commitTime.push({x: 4, y: date.getHours(), z: 5});
    }

    set.add((releaseTime.getMonth() + 1) +'-'+ releaseTime.getDate());
    set.add((deadline.getMonth() + 1) +'-'+ deadline.getDate());
    return set;

  }
}
