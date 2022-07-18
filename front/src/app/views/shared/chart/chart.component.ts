import {Component, OnInit, NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {ChartService} from './chart.service';
import {Status, Color} from './status';
import {TimeService} from '../../../services/time.service';
import {NgxLoadingModule, ngxLoadingAnimationTypes} from 'ngx-loading';
import {ChartsModule} from 'ng2-charts';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.scss']
})

@NgModule({
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  // ...
  imports: [
    // ...
    ChartsModule,
    FormsModule,
    NgxLoadingModule.forRoot({
      animationType: ngxLoadingAnimationTypes.wanderingCubes,
      backdropBackgroundColour: 'rgba(0,0,0,0.1)',
      backdropBorderRadius: '4px',
      primaryColour: '#ffffff',
      secondaryColour: '#ffffff',
      tertiaryColour: '#ffffff'
    })
  ],
  // ...
})

export class ChartComponent implements OnInit {
  public loading = true;
  public commits: Array<any>;
  public commitsDetailEachStudent: Array<any>;
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
  userNameList: string[] = [];
  assignmentNameList: string[] = [];
  secondPRRate = new Map();
  reviseSubmissionRate = new Map();
  public bubbleChartData: any[] = [];
  public bubbleChartLabels: Array<any> = [];
  public scatterChartData: any[] = [];
  public scatterChartLabels: Array<any> = [];
  public isbubbleChartReady = false;
  public isScatterChartReady = false;
  public selectedAssignment = '';
  // 班級學生人數
  public userCount;
  public prAssignmentNameList = [];
  public payAssignmentRate = [];
  public pr1Rate = [];
  public reviseRate = [];
  public pr2Rate = [];
  public payAssignmentRateDisplay = 0;
  public pr1RateDisplay = 0;
  public reviseRateDisplay = 0;
  public pr2RateDisplay = 0;

  constructor(private chartService: ChartService, private timeService: TimeService) {
  }

  async ngOnInit() {
    await this.getAllUser();
    await this.chartService.getAllCommits().subscribe(
      (response) => {
        this.commits = response.allCommitRecord;
        this.getStatusResultData();
        this.selectedAssignment = this.commits[0].name;
      },
      (error) => {
        console.log('Get all assignments error');
      }
    );
    await this.calReviewRate();
    await this.initScatterData();
    await this.getScatterData();
  }

  computeStatus(commits: any) {
    const statusCount = [0, 0, 0, 0, 0];

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
      }
    }
    return statusCount;
  }

  getStatusResultData() {
    this.labels = [];
    this.isBarChartReady = false;
    for (let i = 0; i < this.commits.length; i++) {
      this.labels.push(this.commits[i].name);
      const statusCount = this.computeStatus(this.commits[i].commits);
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
      const statusCount = this.computeStatus(this.commits[i].commits);
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
    const startTime = this.timeService.getUTCTime(commitRecord.releaseTime);
    const endTime = this.timeService.getUTCTime(commitRecord.deadline);
    this.bubbleChartLabels = Array.from(this.timeService.getIntervalTime(startTime, endTime));
    /////////////////////////////////////////////////////////////////////////////////
    for (let i = 0; i < commitRecord.commits.length; i++) {
      const date = this.timeService.getUTCTime(commitRecord.commits[i].time);
      const time = ((date.getMonth() + 1) + '-' + date.getDate());
      const chartData = {
        data: [{x: time, y: date.getHours() + 1, z: 5}],
        radius: 5, type: 'bubble',
        label: assignmentName,
        backgroundColor: Color.bubble,
        borderColor: Color.bubble,
        hoverBackgroundColor: Color.bubble,
        hoverBorderColor: Color.bubble,
      };
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

  getAllUser() {
    this.chartService.getAllUser().subscribe(
      (response) => {
        this.userCount = response.Users.length;
        for (let i = 0; i < response.Users.length; i++) {
          this.userNameList.push(response.Users[i].name);
        }
      },
      error => {
        console.log('Get all user error');
      }
    );
  }

  initScatterData() {
    for (let i = 0; i < this.userCount; i++) {
      this.scatterChartData.push({
        data: [],
        labels: this.userNameList[i],
        backgroundColor: 'transparent',
        borderColor: 'transparent',
        pointBackgroundColor: '#f9b115',
        pointHoverBackgroundColor: '#f9b115',
        pointHoverBorderColor: '#f9b115'
      });
    }
  }

  async getScatterData() {
    this.scatterChartLabels = this.prAssignmentNameList;
    // console.log(this.scatterChartData[0].labels);
    console.log(this.scatterChartLabels);
    // 取得特定作業的所有成績
    for (let i = 0; i < this.prAssignmentNameList.length; i++) {
      const response = await this.chartService.getAllUserScore(this.prAssignmentNameList[i]).toPromise();
      for (let j = 0; j < this.userCount; j++) {
        this.scatterChartData[j].data.push(Number(response[j].score));
      }
    }
    // End
    this.isScatterChartReady = true;
    console.log(this.scatterChartData);
    console.log(this.barChartData);
  }

  async calReviewRate() {
    const responseTimeLine = await this.chartService.getAllPeerReviewAssignment().toPromise();
    // Rate Array 初始化
    for (let i = 0; i < responseTimeLine.allReviewAssignments.length; i++) {
      this.prAssignmentNameList.push(responseTimeLine.allReviewAssignments[i].name);
      this.payAssignmentRate.push(0);
      this.pr1Rate.push(0);
      this.reviseRate.push(0);
      this.pr2Rate.push(0);
    }
    // 取得每個作業每個Round是否完成
    for (let i = 0; i < this.prAssignmentNameList.length; i++) {
      const response = await this.chartService.getPeerReviewStatusRoundAllUser(this.prAssignmentNameList[i]).toPromise();
      let rateRound1 = 0;
      let rateRound2 = 0;
      const total = response[0].reviewRound[0].amount * this.userCount;
      for (let j = 0; j < response.length; j++) {
        rateRound1 = rateRound1 + response[j].reviewRound[0].count;
        rateRound2 = rateRound2 + response[j].reviewRound[1].count;
      }
      if (rateRound1 !== 0) {
        this.pr1Rate[i] = rateRound1 / total;
        // 取小數點後一位
        this.pr1Rate[i] = Math.round((this.pr1Rate[i] * 100 + Number.EPSILON) * 10) / 10;
      } else {
        this.pr1Rate[i] = rateRound1;
      }
      if (rateRound2 !== 0) {
        this.pr2Rate[i] = rateRound2 / total;
        // 取小數點後一位
        this.pr2Rate[i] = Math.round((this.pr2Rate[i] * 100 + Number.EPSILON) * 10) / 10;
      } else {
        this.pr2Rate[i] = rateRound2;
      }
    }
  }

  test($event) {
    console.log($event);
  }
}
