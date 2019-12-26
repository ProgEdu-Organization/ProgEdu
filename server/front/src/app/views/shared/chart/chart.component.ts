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

  public bubbleChartData: any[] = [];
  public bubbleChartLabels: Array<any> = [];
  public isbubbleChartReady = false;
  constructor(private chartService: ChartService) { }

  ngOnInit() {
    this.chartService.getAllCommits().subscribe(
      (response) => {
        console.log(response);
        /////////////////////////////////////////////////////
        this.commits = response.allCommitRecord;
        this.getStatusResultData();
        this.getMixedChartData();
        this.getBubbleChartData(this.commits[0].name);
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

  getBubbleChartData(assignmentName: string) {
    let commitRecord : any;
    for(let i=0; i < this.commits.length; i++){
      if(this.commits[i].name === assignmentName){
        commitRecord = this.commits[i];
        break;
      }
    }
    this.bubbleChartLabels = Array.from(this.getIntervalTime(new Date(commitRecord.releaseTime)
                                        , new Date(commitRecord.deadline)));
    /////////////////////////////////////////////////////////////////////////////////
    for(let i=0; i < commitRecord.commits.length; i++) {
      let date = new Date(commitRecord.commits[i].time);
      let time = ((date.getMonth() + 1) + '-' + date.getDate());
      let chartData = {
        data: [{x: time, y: date.getHours(), z: 5}],
        radius: 5, type: 'bubble',
        label: assignmentName,
        backgroundColor: Status.notBuild.color,
        borderColor:Status.notBuild.color,
        hoverBackgroundColor: Status.notBuild.color,
        hoverBorderColor:Status.notBuild.color,
      }
      // Compute the commit times
      let isDataDuplicate = false;
      for(let j = 0; j < this.bubbleChartData.length; j++){
        if(chartData.data[0].x === this.bubbleChartData[j].data[0].x && 
            chartData.data[0].y === this.bubbleChartData[j].data[0].y) {
          // limit max radius = 40
          if(this.bubbleChartData[j].radius !== 40 ) {
            this.bubbleChartData[j].radius += 1;
          }
          isDataDuplicate = true;
          break;
        }
      }
      if(!isDataDuplicate) {
          this.bubbleChartData.push(chartData);
      }
    }
    for(let j = 0; j < this.bubbleChartData.length; j++){
      if(this.bubbleChartData[j].data[0].x ==='10-23' && this.bubbleChartData[j].data[0].y === 15){
        console.log(this.bubbleChartData[j]);
      }
    }
  }



  getIntervalTime(releaseTime: Date, deadline: Date) {
    // ex: 12-12 ~ 12-20 -> return [12-12, 12-13, 12-14 ... 12-20]
    let set: Set<any> = new Set();
    let time_diff: number = deadline.getTime() - releaseTime.getTime();
    for(let i=0; i <= time_diff; i += 86400000){
      var date = new Date(releaseTime.getTime() + i);
      set.add((date.getMonth() + 1) +'-'+ date.getDate());
    }
    set.add((releaseTime.getMonth() + 1) +'-'+ releaseTime.getDate());
    set.add((deadline.getMonth() + 1) +'-'+ deadline.getDate());
    return set;

  }
}
