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

  public examBarChartData: any[] = [
    {
      data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], label: '',
      backgroundColor: '#3399ff',
      borderColor: '#3399ff',
      hoverBackgroundColor: '#3399ff',
      hoverBorderColor: '#3399ff',
    }
  ];

  public prRateLineChartData: any[] = [
    {
      data: [],
      label: 'Round 1',
      backgroundColor: 'transparent',
      borderColor: '#e55353',
      pointBackgroundColor: '#e55353',
      pointHoverBackgroundColor: '#e55353',
      pointHoverBorderColor: '#e55353'
    },
    {
      data: [],
      label: 'Round 2',
      backgroundColor: 'transparent',
      borderColor: '#3399ff',
      pointBackgroundColor: '#3399ff',
      pointHoverBackgroundColor: '#3399ff',
      pointHoverBorderColor: '#3399ff'
    }
  ];

  public MetricsCountChartData: any[] = [
    {
      data: [], label: 'PR1',
      backgroundColor: '#3399ff',
      borderColor: '#3399ff',
      hoverBackgroundColor: '#3399ff',
      hoverBorderColor: '#3399ff',
    },
    {
      data: [], label: 'PR2',
      backgroundColor: '#e55353',
      borderColor: '#e55353',
      hoverBackgroundColor: '#e55353',
      hoverBorderColor: '#e55353',
    }
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
  public isExamScoreTableReady = false;
  public isPrRateLineChartReady = false;
  public isExamScoreTableEmpty = true;
  public selectedAssignment = '';
  public selectedExam = '';
  // 班級學生人數
  public userCount;
  public prAssignmentNameList = [];
  public payAssignmentRate = [];
  public pr1Rate = [];
  public reviseRate = [];
  public pr2Rate = [];
  public examNameList = ['Midterm', 'Final'];
  public examScore = [];
  public examScoreMax = -1;
  public examScoreMin = -1;
  public examScoreMAvg = -1;
  public examRange = ['0-9', '10-19', '20-29', '30-39', '40-49', '50-59', '60-69', '70-79', '80-89', '90-99', '100'];

  constructor(private chartService: ChartService, private timeService: TimeService) {
  }

  async ngOnInit() {
    await this.getAllUser();
    await this.chartService.getAllCommits().subscribe(
      (response) => {
        console.log(response);
        this.commits = response.allCommitRecord;
        this.getStatusResultData();
        this.selectedAssignment = this.commits[0].name;
        this.calPayAssignmentRate();
      },
      (error) => {
        console.log('Get all assignments error');
      }
    );
    this.selectedExam = this.examNameList[0];
    await this.calReviewRate();
    await this.initScatterData();
    await this.getScatterData();
    // Review Record Metrics
    // await this.getReviewFeedback();
    await this.getExamScore(this.selectedExam);
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

  /**
   * Get all user's name and user count.
   */
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
        label: this.userNameList[i],
        backgroundColor: 'transparent',
        borderColor: 'transparent',
        pointBackgroundColor: '#f9b115',
        pointHoverBackgroundColor: '#f9b115',
        pointHoverBorderColor: '#f9b115',
      });
    }
  }

  /**
   * Get each assignment score of all user in class.
   */
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

  calPayAssignmentRate() {
    // let rate = 0;
    console.log('Commits' + this.commits.length);
    for (let i = 0; i < this.commits.length; i++) {
      console.log(this.commits[i]);
      this.assignmentNameList.push(this.commits[i].name);
      console.log(this.commits[i].commits.length);
      for (let j = 0; j < this.commits[i].commits.length; j++) {

      }
    }
    console.log(this.assignmentNameList);
  }

  /**
   * Calculate each assignment's peer review rate and create all prAssignment array.
   */
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
    this.prRateLineChartData[0].data = this.pr1Rate;
    this.prRateLineChartData[1].data = this.pr2Rate;
    this.isPrRateLineChartReady = true;
  }

  async getReviewFeedback() {
    for (let i = 0; i < this.prAssignmentNameList.length; i++) {
      const responseOfAssignment = await this.chartService.getAllPeerReviewAssignment().toPromise();
      // console.log(responseOfAssignment);
      const round = responseOfAssignment.allReviewAssignments[i].round;
      for (let j = 0; j < this.userNameList.length; j++) {
        const response = await this.chartService.getReviewFeedback(this.prAssignmentNameList[i], this.userNameList[j]).toPromise();
        console.log(response);
        // this.verifyDetailExist(response);
        console.log(response);
        console.log(this.prAssignmentNameList[i] + ' ' + this.userNameList[j]);
        for (let k = 0; k < response.allRecordDetail.length; k++) {
          for (let l = 1; l <= round; l++) {
            console.log('Round' + l);
            // tslint:disable-next-line:max-line-length
            const response3 = await this.chartService.getReviewPageDetail(this.userNameList[j], this.prAssignmentNameList[i], response.allRecordDetail[k].id, l.toString()).toPromise();
            console.log(response3);
          }
        }
      }
    }
  }
  //
  // verifyDetailExist(response: Object) {
  //   try {
  //     console.log(response.allRecordDetail[2].Detail[2].feedbackScore);
  //   } catch {
  //     console.log('not Exist');
  //   }
  // }

  /**
   * Initial ExamBarChartData's data and labels.
   */
  initExamBarChartData() {
    this.examBarChartData[0].label = '';
    for (let i = 0; i < this.examBarChartData[0].data.length; i++) {
      this.examBarChartData[0].data[i] = 0;
    }
  }

  /**
   * Get exam score array and get max、min score in exam.
   * @param examName selected exam
   */
  async getExamScore(examName: string) {
    // 初始化
    this.selectedExam = examName;
    this.isExamScoreTableEmpty = true;
    this.isExamScoreTableReady = false;
    let examScoreSum = 0;
    this.examScore.splice(0, this.examScore.length);
    this.initExamBarChartData();
    // 初始化End
    const response = await this.chartService.getAllUserScore(examName).toPromise();
    for (let i = 0; i < response.length; i++) {
      this.examScore.push(response[i].score);
    }
    console.log(examName);
    console.log(this.examScore);
    if (this.examScore.length === 0) {
      console.log('Exam no data');
    } else {
      this.examScoreMax = Math.max.apply(null, this.examScore);
      this.examScoreMin = Math.min.apply(null, this.examScore);
      examScoreSum = this.examScore.reduce((previous, current) => current += previous);
      this.examScoreMAvg = examScoreSum / this.examScore.length;
      // 取小數點後一位
      this.examScoreMAvg = Math.round((this.examScoreMAvg + Number.EPSILON) * 10) / 10;
      this.isExamScoreTableEmpty = false;
      this.examBarChartData[0].label = examName;
      this.countExamDistributed(this.examScore);
    }
    this.isExamScoreTableReady = true;
  }

  /**
   * Count Exam Score distribution.
   * @param examScore The array of selected exam Score
   */
  countExamDistributed(examScore: any) {
    for (let i = 0; i < this.examScore.length; i++) {
      console.log(Math.floor(this.examScore[i] / 10));
      switch (Math.floor(this.examScore[i] / 10)) {
        case 10 :
          this.examBarChartData[0].data[10] += 1;
          break;
        case 9 :
          this.examBarChartData[0].data[9] += 1;
          break;
        case 8 :
          this.examBarChartData[0].data[8] += 1;
          break;
        case 7 :
          this.examBarChartData[0].data[7] += 1;
          break;
        case 6 :
          this.examBarChartData[0].data[6] += 1;
          break;
        case 5 :
          this.examBarChartData[0].data[5] += 1;
          break;
        case 4 :
          this.examBarChartData[0].data[4] += 1;
          break;
        case 3 :
          this.examBarChartData[0].data[3] += 1;
          break;
        case 2 :
          this.examBarChartData[0].data[2] += 1;
          break;
        case 1 :
          this.examBarChartData[0].data[1] += 1;
          break;
        case 0 :
          this.examBarChartData[0].data[0] += 1;
          break;
      }
    }
  }

  test($event) {
    console.log($event);
  }
}
