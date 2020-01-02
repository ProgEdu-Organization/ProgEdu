import { Component, OnInit, NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ChartService } from './chart.service';
import { Status, Color } from './status';
import { TimeService } from '../../../services/time.service';
import { NgxLoadingModule, ngxLoadingAnimationTypes } from 'ngx-loading';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html'
})

@NgModule({
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  //...
  imports: [
    //...
    NgxLoadingModule.forRoot({
      animationType: ngxLoadingAnimationTypes.wanderingCubes,
      backdropBackgroundColour: 'rgba(0,0,0,0.1)',
      backdropBorderRadius: '4px',
      primaryColour: '#ffffff',
      secondaryColour: '#ffffff',
      tertiaryColour: '#ffffff'
    })
  ],
  //...
})

export class ChartComponent implements OnInit {
  public loading = true;
  public commits: Array<any>;
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
  public isBarChartReady: boolean = false;

  public mixedChartData: Array<any> = [
    {
      data: [], label: Status.notBuild.name, fill: false, type: 'line',
      backgroundColor: Status.notBuild.color,
      borderColor: Status.notBuild.color,
      hoverBackgroundColor: Status.notBuild.color,
      hoverBorderColor: Status.notBuild.color,
    },
    {
      data: [], label: Status.compilerFailure.name, fill: false, type: 'line',
      backgroundColor: Status.compilerFailure.color,
      borderColor: Status.compilerFailure.color,
      hoverBackgroundColor: Status.compilerFailure.color,
      hoverBorderColor: Status.compilerFailure.color,
    },
    {
      data: [], label: Status.codingStyleError.name, fill: false, type: 'line',
      backgroundColor: Status.codingStyleError.color,
      borderColor: Status.codingStyleError.color,
      hoverBackgroundColor: Status.codingStyleError.color,
      hoverBorderColor: Status.codingStyleError.color,
    },
    {
      data: [], label: Status.testFailure.name, fill: false, type: 'line',
      backgroundColor: Status.testFailure.color,
      borderColor: Status.testFailure.color,
      hoverBackgroundColor: Status.testFailure.color,
      hoverBorderColor: Status.testFailure.color,
    },
    {
      data: [], label: Status.success.name, fill: false, type: 'line',
      backgroundColor: Status.success.color,
      borderColor: Status.success.color
    },
    {
      data: [], label: 'Count',
      backgroundColor: '#ced2d8',
      borderColor: '#ced2d8'
    }
  ];
  public isMixedChartReady: boolean = false;

  public bubbleChartData: any[] = [];
  public bubbleChartLabels: Array<any> = [];
  public isbubbleChartReady = false;
  public selectedAssignment = '';

  constructor(private chartService: ChartService, private timeService: TimeService) { }

  ngOnInit() {
    this.chartService.getAllCommits().subscribe(
      (response) => {
        this.commits = response.allCommitRecord;
        this.getStatusResultData();
        this.selectedAssignment = this.commits[0].name;
      },
      (error) => {
        console.log('Get all assignments error');
      }
    );
  }

  computeStatus(commits: any) {
    let statusCount = [0, 0, 0, 0, 0];

    if (commits) {
      for (let i = 0; i < commits.length; i++) {
        if (Status.notBuild.status.includes(commits[i].status)) {
          statusCount[0] = statusCount[0] + 1;
        } else if (Status.compilerFailure.status.includes(commits[i].status)) {
          statusCount[1] = statusCount[1] + 1;
        } else if (Status.codingStyleError.status.includes(commits[i].status)) {
          statusCount[2] = statusCount[2] + 1;
        } else if (Status.testFailure.status.includes(commits[i].status)) {
          statusCount[3] = statusCount[3] + 1;
        } else if (Status.success.status.includes(commits[i].status)) {
          statusCount[4] = statusCount[4] + 1;
        }
      };
    }
    return statusCount;
  }

  getStatusResultData() {
    this.labels = [];
    this.isBarChartReady = false;
    for (let i = 0; i < this.commits.length; i++) {
      this.labels.push(this.commits[i].name);
      let statusCount = this.computeStatus(this.commits[i].commits);
      for (let j = 0; j < this.barChartData.length; j++) {
        this.barChartData[j].data.push(statusCount[j]);
      }
    }
    this.isBarChartReady = true;
  }

  getMixedChartData() {
    this.labels = [];
    this.isMixedChartReady = false;
    for (let i = 0; i < this.commits.length; i++) {
      this.labels.push(this.commits[i].name);
      let statusCount = this.computeStatus(this.commits[i].commits);
      for (let j = 0; j < this.barChartData.length; j++) {
        this.mixedChartData[j].data.push(statusCount[j]);
      }
      this.mixedChartData[this.mixedChartData.length - 1].data.push(this.commits[i].commits.length);
    }
    this.isMixedChartReady = true;
  }

  getBubbleChartData(assignmentName: string) {
    this.isbubbleChartReady = false;
    this.bubbleChartData = [];
    this.bubbleChartLabels = [];
    let commitRecord: any;
    this.selectedAssignment = assignmentName;
    for (let i = 0; i < this.commits.length; i++) {
      if (this.commits[i].name === assignmentName) {
        commitRecord = this.commits[i];
        break;
      }
    }
    let startTime = this.timeService.getUTCTime(commitRecord.releaseTime);
    let endTime = this.timeService.getUTCTime(commitRecord.deadline);
    this.bubbleChartLabels = Array.from(this.timeService.getIntervalTime(startTime, endTime));
    /////////////////////////////////////////////////////////////////////////////////
    for (let i = 0; i < commitRecord.commits.length; i++) {
      let date = this.timeService.getUTCTime(commitRecord.commits[i].time);
      let time = ((date.getMonth() + 1) + '-' + date.getDate());
      let chartData = {
        data: [{ x: time, y: date.getHours() + 1, z: 5 }],
        radius: 5, type: 'bubble',
        label: assignmentName,
        backgroundColor: Color.bubble,
        borderColor: Color.bubble,
        hoverBackgroundColor: Color.bubble,
        hoverBorderColor: Color.bubble,
      }
      // Compute the commit times
      let isDataDuplicate = false;
      for (let j = 0; j < this.bubbleChartData.length; j++) {
        if (chartData.data[0].x === this.bubbleChartData[j].data[0].x &&
          chartData.data[0].y === this.bubbleChartData[j].data[0].y) {
          // limit max radius = 40
          if (this.bubbleChartData[j].radius !== 40) {
            this.bubbleChartData[j].radius += 1;
          }
          isDataDuplicate = true;
          break;
        }
      }
      if (!isDataDuplicate) {
        this.bubbleChartData.push(chartData);
      }
    }
    this.isbubbleChartReady = true;
  }

  test($event) {
    console.log($event);
  }
}
