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
  assignmentSubmissionRate = new Map();
  firstPRRate = new Map();
  secondPRRate = new Map();
  reviseSubmissionRate = new Map();
  public bubbleChartData: any[] = [];
  public bubbleChartLabels: Array<any> = [];
  public isbubbleChartReady = false;
  public selectedAssignment = '';
  // 班級學生人數
  public userCount;
  public prAssignmentNameList = [];
  public payAssignmentRate = [];
  public pr1Rate = [];
  public reviseRate = [];
  public pr2Rate = [];

  constructor(private chartService: ChartService, private timeService: TimeService) {
  }

  async ngOnInit() {
    await this.getAllUser();
    await this.chartService.getAllCommits().subscribe(
      (response) => {
        this.commits = response.allCommitRecord;
        for (let i = 0; i < this.commits.length; i++) {
          if (this.commits[i].commits[0]) {
            this.assignmentNameList.push(this.commits[i].name);
          }
        }
        console.log(this.assignmentNameList);
        this.getStatusResultData();
        this.selectedAssignment = this.commits[0].name;
        for (let i = 0; i < this.assignmentNameList.length; i++) {
          for (let j = 0; j < this.userCount; j++) {
            // tslint:disable-next-line:no-shadowed-variable
            this.chartService.getCommitRecord(this.userNameList[j], this.assignmentNameList[i]).subscribe(response => {
              this.commitsDetailEachStudent = response;
              // console.log(this.commitsDetailEachStudent);
            });
          }
        }
        this.compareCommitTime();
      },
      (error) => {
        console.log('Get all assignments error');
      }
    );
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
        console.log(response);
        this.userCount = response.Users.length;
        for (let i = 0; i < response.Users.length; i++) {
          this.userNameList.push(response.Users[i].name);
        }
        console.log(this.userNameList);
      },
      error => {
        console.log('Get all user error');
      }
    );
  }

  async compareCommitTime() {
    const responseTimeLine = await this.chartService.getAllPeerReviewAssignment().toPromise();
    let flag = 0;
    console.log(responseTimeLine);
    // Rate Array 初始化
    for (let i = 0; i < responseTimeLine.allReviewAssignments.length; i++) {
      this.prAssignmentNameList.push(responseTimeLine.allReviewAssignments[i].name);
      this.payAssignmentRate.push(0);
      this.pr1Rate.push(0);
      this.reviseRate.push(0);
      this.pr2Rate.push(0);
    }
    // 每個作業
    for (let i = 0; i < responseTimeLine.allReviewAssignments.length; i++) {
      // 作業開始
      const assignmentStartTime = new Date(responseTimeLine.allReviewAssignments[i].assessmentTimes[0].startTime);
      // 做作業死線
      const assignmentEndTime = new Date(responseTimeLine.allReviewAssignments[i].assessmentTimes[0].endTime);
      // 第一次PR開始
      const PR1StartTime = new Date(responseTimeLine.allReviewAssignments[i].assessmentTimes[1].startTime);
      // 第一次PR死線
      const PR1EndTime = new Date(responseTimeLine.allReviewAssignments[i].assessmentTimes[1].endTime);
      // 修改作業開始
      const reviseStartTime = new Date(responseTimeLine.allReviewAssignments[i].assessmentTimes[2].startTime);
      // 修改作業死線
      const reviseEndTime = new Date(responseTimeLine.allReviewAssignments[i].assessmentTimes[2].endTime);
      // 第二次PR開始
      const PR2StartTime = new Date(responseTimeLine.allReviewAssignments[i].assessmentTimes[3].startTime);
      // 第二次PR死線
      const PR2EndTime = new Date(responseTimeLine.allReviewAssignments[i].assessmentTimes[3].endTime);
      const createTime = new Date(responseTimeLine.allReviewAssignments[i].createTime);
      console.log(responseTimeLine.allReviewAssignments[i].name);
      for (let j = 0; j < this.userNameList.length; j++) {
        console.log(this.userNameList[j]);
        // tslint:disable-next-line:max-line-length
        const responseGCR = await this.chartService.getCommitRecord(this.userNameList[j], responseTimeLine.allReviewAssignments[i].name).toPromise();
        for (let k = 0; k < responseGCR.length; k++) {
          // date 為每一個commit的時間
          const date = new Date(responseGCR[k].time);
          if (createTime < date && date < assignmentStartTime) {
            console.log('init.');
            console.log(date);
            console.log(createTime);
          } else if (assignmentStartTime < date && date < assignmentEndTime) {
            console.log('做作業');
            console.log(assignmentStartTime);
            console.log(date);
            console.log(assignmentEndTime);
            if (flag === 0) {
              this.payAssignmentRate[i] = this.payAssignmentRate[i] + 1;
              flag = flag + 1;
            }
          } else if (PR1StartTime < date && date < PR1EndTime) {
            console.log('PR1');
            console.log(PR1StartTime);
            console.log(date);
            console.log(PR1EndTime);
            if (flag === 1) {
              this.pr1Rate[i] = this.pr1Rate[i] + 1;
              flag = flag + 1;
            }
          } else if (reviseStartTime < date && date < reviseEndTime) {
            console.log('Revise');
            console.log(reviseStartTime);
            console.log(date);
            console.log(reviseEndTime);
            if (flag === 2) {
              this.reviseRate[i] = this.reviseRate[i] + 1;
              flag = flag + 1;
            }
          } else if (PR2StartTime < date && date < PR2EndTime) {
            console.log('PR2');
            console.log(PR2StartTime);
            console.log(date);
            console.log(PR2EndTime);
            if (flag === 3) {
              this.pr2Rate[i] = this.pr2Rate[i] + 1;
              flag = 0;
            }
          } else {
            console.log('過期啦');
            console.log(date);
          }
        }
        flag = 0;
      }
    }
    console.log(this.prAssignmentNameList);
    console.log(this.payAssignmentRate);
    console.log(this.pr1Rate);
    console.log(this.reviseRate);
    console.log(this.pr2Rate);
  }

  test($event) {
    console.log($event);
  }
}
