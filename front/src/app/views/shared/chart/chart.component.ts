import {Component, OnInit, NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {ChartService} from './chart.service';
import {Status, Color} from './status';
import {TimeService} from '../../../services/time.service';
import {NgxLoadingModule, ngxLoadingAnimationTypes} from 'ngx-loading';
import {ChartsModule} from 'ng2-charts';
import {FormsModule} from '@angular/forms';
import {Category} from '../../teacher/review-metrics-management/Category';

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
      data: [0, 0, 0, 0, 0, 0, 0, 0], label: '',
    }
  ];

  public passAllMetricsBarChartData: any[] = [
    {
      data: [], label: '第一次審查',
      backgroundColor: '#e55353',
      borderColor: '#e55353',
      hoverBackgroundColor: '#e55353',
      hoverBorderColor: '#e55353',
    },
    {
      data: [], label: '第二次審查',
      backgroundColor: '#3399ff',
      borderColor: '#3399ff',
      hoverBackgroundColor: '#3399ff',
      hoverBorderColor: '#3399ff',
    }
  ];
  public isPassAllMetricsBarChartReady = false;

  public prRateLineChartData: any[] = [
    {
      data: [],
      label: '第一次審查',
      backgroundColor: 'transparent',
      borderColor: '#e55353',
      pointBackgroundColor: '#e55353',
      pointHoverBackgroundColor: '#e55353',
      pointHoverBorderColor: '#e55353'
    },
    {
      data: [],
      label: '第二次審查',
      backgroundColor: 'transparent',
      borderColor: '#3399ff',
      pointBackgroundColor: '#3399ff',
      pointHoverBackgroundColor: '#3399ff',
      pointHoverBorderColor: '#3399ff'
    }
  ];

  public payAssignmentRateLineChartData: any[] = [
    {
      data: [],
      label: 'All assignment',
      backgroundColor: 'transparent',
      borderColor: '#e55353',
      pointBackgroundColor: '#e55353',
      pointHoverBackgroundColor: '#e55353',
      pointHoverBorderColor: '#e55353'
    }
  ];

  public feedbackMixedChartData: any[] = [
    // 最高
    {
      data: [], fill: false, type: 'line',
      label: '最高回饋星星數',
      backgroundColor: 'transparent',
      borderColor: '#e55353',
      pointBackgroundColor: '#e55353',
      pointHoverBackgroundColor: '#e55353',
      pointHoverBorderColor: '#e55353'
    },
    // 平均
    {
      data: [], label: '平均回饋星星數',
      backgroundColor: '#ced2d8',
      borderColor: '#ced2d8'
    }
  ];
  public isFeedbackScoreChartReady = false;

  public MetricsCountChartData: any[] = [
    {
      data: [], label: '第一次審查',
      backgroundColor: '#3399ff',
      borderColor: '#3399ff',
      hoverBackgroundColor: '#3399ff',
      hoverBorderColor: '#3399ff',
    },
    {
      data: [], label: '第二次審查',
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

  // 每一作業Detail
  public prAssignmentDetail: Array<any> = [];
  userNameList: string[] = [];
  categories: Category[];
  // 圖表顯示資料變數
  public bubbleChartLabels: Array<any> = [];
  public bubbleChartData: any[] = [];
  public scatterChartLabels: Array<any> = [];
  public scatterChartData: any[] = [];
  public wholeSemesterMetricsChartData: any[] = [];
  public isExamScoreTableEmpty = true;
  // 班級學生人數
  public userCount;
  public prAssignmentNameList = [];
  public payAssignmentRate = [];
  public allMetrics = [];
  public pr1Rate = [];
  public reviseRate = [];
  public pr2Rate = [];
  public examNameList = [];
  public examScore = [];
  public examScoreMax = -1;
  public examScoreMin = -1;
  public examScoreMAvg = -1;
  public examRange = ['100', '90~99', '80~89', '70~79', '60~69', '50~59', '40~49', '40以下'];
  // 圖表資料是否準備好
  public isMixedChartReady: boolean = false;
  public isbubbleChartReady = false;
  public isScatterChartReady = false;
  public isExamScoreTableReady = false;
  public isPrRateLineChartReady = false;
  public ispayAssignmentRateReady = false;
  public isMetricsCountChartReady = false;
  public isWholeSemesterMetricsChartReady = false;
  // 下拉式預設選項變數
  public selectedAssignment = '';
  public selectedMetricsAssignment = '';
  public selectedExam = '';

  public passFlagOne = 1;
  public passFlagTwo = 1;
  constructor(private chartService: ChartService, private timeService: TimeService) {
  }

  async ngOnInit() {
    await this.getAllUser();
    await this.getAllCategorys();
    await this.getPrAssignmentDetail();
    await this.chartService.getAllCommits().subscribe(
      (response) => {
        // console.log(response);
        this.commits = response.allCommitRecord;
        this.getStatusResultData();
        this.selectedAssignment = this.commits[0].name;
        this.calPayAssignmentRate();
      },
      (error) => {
        console.log('Get all assignments error');
      }
    );
    await this.calReviewRate();
    // await this.calReviseRate();
    await this.initScatterData();
    await this.getScatterData();
    // Review Record Metrics
    this.selectedMetricsAssignment = this.prAssignmentNameList[0];
    await this.getReviewFeedback();
    await this.getFeedbackScore();
    await this.calReviseRate();
    await this.getPassAllMetricsCount();
    await this.setMetricsCountChartData(this.selectedMetricsAssignment);
    await this.setWholeSemesterMetricsData();
    await this.initExamList();
    this.selectedExam = this.examNameList[0];
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
    // console.log(this.scatterChartLabels);
    // 取得特定作業的所有成績
    for (let i = 0; i < this.prAssignmentNameList.length; i++) {
      const response = await this.chartService.getAllUserScore(this.prAssignmentNameList[i]).toPromise();
      for (let j = 0; j < this.userCount; j++) {
        if (response[j]) {
          this.scatterChartData[j].data.push(Number(response[j].score));
        }
      }
    }
    // End
    this.isScatterChartReady = true;
    // console.log(this.scatterChartData);
    // console.log(this.barChartData);
  }

  initWholeSemesterMetricsData() {
    this.wholeSemesterMetricsChartData.push(
      {
        data: [], fill: false, type: 'line',
        label: '班級平均',
        backgroundColor: 'transparent',
        borderColor: '#e55353',
        pointBackgroundColor: '#e55353',
        pointHoverBackgroundColor: '#e55353',
        pointHoverBorderColor: '#e55353',
      }
    );
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      this.wholeSemesterMetricsChartData.push(
        {
          data: [],
          label: this.prAssignmentDetail[i].name,
          backgroundColor: '#ced2d8',
          borderColor: '#ced2d8',
          hoverBackgroundColor: '#ced2d8',
          hoverBorderColor: '#ced2d8',
        }
      );
    }
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      this.wholeSemesterMetricsChartData[0].data.push(0);
    }
  }

  setWholeSemesterMetricsData() {
    this.isWholeSemesterMetricsChartReady = false;
    this.initWholeSemesterMetricsData();
    // 整學期Metrics分布圖顏色
    const colors = ['#321fdb', '#9da5b1', '#2eb85c', '#AFCBFF', '#3399ff', '#F0A7A0', '#4f5d73', '#0E1C36', '#f9b115', '#55C6A9'];
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      // 記得改回 Round2
      this.wholeSemesterMetricsChartData[i + 1].data = this.prAssignmentDetail[i].round2;
      this.wholeSemesterMetricsChartData[i + 1].label = this.prAssignmentDetail[i].name;
      this.wholeSemesterMetricsChartData[i + 1].hoverBackgroundColor = colors[i];
      this.wholeSemesterMetricsChartData[i + 1].hoverBorderColor = colors[i];
      this.wholeSemesterMetricsChartData[i + 1].borderColor = colors[i];
      this.wholeSemesterMetricsChartData[i + 1].backgroundColor = colors[i];
      for (let j = 0; j < this.prAssignmentDetail[i].round2.length; j++) {
        this.wholeSemesterMetricsChartData[0].data[j] += this.prAssignmentDetail[i].round2[j];
      }
    }
    // console.log(this.wholeSemesterMetricsChartData[0].data);
    for (let i = 0; i < this.wholeSemesterMetricsChartData[0].data.length; i++) {
      // console.log(this.wholeSemesterMetricsChartData[0].data[i]);
      // tslint:disable-next-line:max-line-length
      this.wholeSemesterMetricsChartData[0].data[i] = Math.round(((this.wholeSemesterMetricsChartData[0].data[i] / this.prAssignmentDetail.length) + Number.EPSILON) * 10) / 10;
    }
    this.isWholeSemesterMetricsChartReady = true;
  }

  calPayAssignmentRate() {
    // let rate = 0;
    let count = 0;
    for (let i = 0; i < this.commits.length; i++) {
      if (this.prAssignmentNameList.includes(this.commits[i].name)) {
        if (this.commits[i].commits.length === this.userCount) {
          count = 0;
        } else {
          for (let j = 0; j < this.commits[i].commits.length; j++) {
            if (this.commits[i].commits[j].number === 2) {
              count += 1;
            }
          }
        }
        count = Math.round((count / this.userCount * 100 + Number.EPSILON) * 10) / 10;
        this.payAssignmentRate.push(count);
        this.payAssignmentRateLineChartData[0].data.push(count);
        count = 0;
      }
    }
    this.ispayAssignmentRateReady = true;
    // console.log(this.prAssignmentNameList);
    // console.log(this.payAssignmentRate);
  }

  async getPrAssignmentDetail() {
    const response = await this.chartService.getAllPeerReviewAssignment().toPromise();
    for (let i = 0; i < response.allReviewAssignments.length; i++) {
      this.prAssignmentDetail.push(
        {
          name: response.allReviewAssignments[i].name,
          round: response.allReviewAssignments[i].round,
          amount: response.allReviewAssignments[i].amount,
          assessmentTimes: response.allReviewAssignments[i].assessmentTimes,
          // round each metrics 錯誤總數
          round1: [],
          round2: [],
          feedbackScoreMax: 0,
          feedbackScoreCount: 0,
          feedbackScoreTotal: 0,
          passAllMetricsCountRound1: [],
          passAllMetricsCountRound2: [],
          isReviseAssignment: []
        },
      );
      for (let j = 0; j < this.allMetrics.length; j++) {
        this.prAssignmentDetail[i].round1.push(0);
        this.prAssignmentDetail[i].round2.push(0);
      }
      for (let j = 0; j < this.userNameList.length; j++) {
        this.prAssignmentDetail[i].passAllMetricsCountRound1.push(0);
        this.prAssignmentDetail[i].passAllMetricsCountRound2.push(0);
      }
    }
  }

  /**
   * Calculate each assignment's peer review rate and create all prAssignment array.
   */
  async calReviewRate() {
    const responseTimeLine = await this.chartService.getAllPeerReviewAssignment().toPromise();
    // Rate Array 初始化
    for (let i = 0; i < responseTimeLine.allReviewAssignments.length; i++) {
      this.prAssignmentNameList.push(responseTimeLine.allReviewAssignments[i].name);
      // this.payAssignmentRate.push(0);
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

  initFlag() {
    const flag = [];
    for (let i = 0; i < this.allMetrics.length; i++) {
      flag.push(0);
    }
    return flag;
  }

  initTmp (tmp: any) {
    for (let i = 0; i < this.allMetrics.length; i++) {
      if (tmp.length === 0) {
        tmp.push(0);
      } else {
        tmp[i] = 0;
      }
    }
    return tmp;
  }

  async getReviewFeedback() {
    let tmp1 = [];
    let tmp2 = [];
    let flag = 0;
    // 所有作業
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      for (let j = 0; j < this.userNameList.length; j++) {
        tmp1 = this.initTmp(tmp1);
        tmp2 = this.initTmp(tmp2);
        // tslint:disable-next-line:max-line-length
        const reviewFeedbackPerStu = await this.chartService.getReviewFeedback(this.prAssignmentDetail[i].name, this.userNameList[j]).toPromise();
        // console.log('作業名稱 ' + this.prAssignmentDetail[i].name + ' ' + '被審查者' + this.userNameList[j]);
        // 審查人數
        // console.log(this.prAssignmentDetail[i].amount);
        for (let k = 0; k < this.prAssignmentDetail[i].amount; k++) {
          // 是否有審查紀錄
          if (reviewFeedbackPerStu.allRecordDetail[k] !== undefined) {
            // console.log(reviewFeedbackPerStu.allRecordDetail[k]);
            // 最後參與審查round
            if (reviewFeedbackPerStu.allRecordDetail[k].latestCompletedRound === 2) {
              // 第一次審查統計
              // tslint:disable-next-line:max-line-length
              const reviewFeedbackPerStuR1 = await this.chartService.getReviewPageDetail(this.userNameList[j], this.prAssignmentNameList[i], reviewFeedbackPerStu.allRecordDetail[k].id, '1').toPromise();
              // console.log('第一次審查結果');
              if (reviewFeedbackPerStuR1.Detail !== undefined) {
                tmp1 = this.calNotPassCount(i, reviewFeedbackPerStuR1.Detail, tmp1, 1);
              } else {
                // this.passFlagOne = 0;
              }
              // 第二次審查結果統計
              if (reviewFeedbackPerStu.allRecordDetail[k].Detail !== undefined) {
                tmp2 = this.calNotPassCount(i, reviewFeedbackPerStu.allRecordDetail[k].Detail, tmp2, 2);
              } else {
                // this.passFlagTwo = 0;
              }
            } else if (reviewFeedbackPerStu.allRecordDetail[k].latestCompletedRound === 1) {
              if (reviewFeedbackPerStu.allRecordDetail[k].Detail !== undefined) {
                tmp1 = this.calNotPassCount(i, reviewFeedbackPerStu.allRecordDetail[k].Detail, tmp1, 1);
              } else {
                // this.passFlagOne = 0;
              }
              flag += 1;
              if (flag === this.prAssignmentDetail[i].amount) {
                this.passFlagTwo = 0;
              }
            } else {
              // 0
              this.passFlagOne = 0;
              this.passFlagTwo = 0;
            }
          } else {
            this.passFlagOne = 0;
            this.passFlagTwo = 0;
          }
        }
        // 1為該學生所有 Metrics All pass
        if (this.passFlagOne === 1) {
          this.prAssignmentDetail[i].passAllMetricsCountRound1[j] = 1;
        }
        if (this.passFlagTwo === 1) {
          this.prAssignmentDetail[i].passAllMetricsCountRound2[j] = 1;
        }
        this.passFlagOne = 1;
        this.passFlagTwo = 1;
      }
      // console.log(this.prAssignmentDetail[i].passAllMetricsCountRound1);
      // console.log(this.prAssignmentDetail[i].passAllMetricsCountRound2);
    }
  }

  calNotPassCount(i: number, Detail: any, tmp, round: number) {
    // console.log(Detail);
    // 每個Metrics
    for (let l = 0; l < Detail.length; l++) {
      // 未通過
      if (Detail[l].score === 2) {
        if (tmp[this.allMetrics.indexOf(Detail[l].metrics)] === 0) {
          if (round === 1) {
            this.prAssignmentDetail[i].round1[this.allMetrics.indexOf(Detail[l].metrics)] += 1;
            this.passFlagOne = 0;
          } else {
            this.prAssignmentDetail[i].round2[this.allMetrics.indexOf(Detail[l].metrics)] += 1;
            this.passFlagTwo = 0;
          }
          tmp[this.allMetrics.indexOf(Detail[l].metrics)] = 1;
        }
      }
      // 統計給予回饋分數
      if (Detail[l].feedbackScore !== undefined) {
        this.countFeedbackScore(i, Detail[l].feedbackScore);
      }
    }
    // console.log(tmp);
    return tmp;
  }

  // async getReviewFeedback() {
  //   // 作業審查回合數
  //   console.log(this.prAssignmentDetail[0].round);
  //   for (let i = 0; i < this.prAssignmentDetail.length; i++) {
  //     for (let j = 0; j < this.userNameList.length; j++) {
  //       const response = await this.chartService.getReviewFeedback(this.prAssignmentDetail[i].name, this.userNameList[j]).toPromise();
  //       const flagR1 = this.initFlag();
  //       const flagR2 = this.initFlag();
  //       console.log('作業名稱 ' + this.prAssignmentDetail[i].name + ' ' + '被審查者' + this.userNameList[j]);
  //       console.log(response);
  //       if (response.allRecordDetail[0] !== undefined) {
  //         for (let k = 0; k < this.prAssignmentDetail[i].amount; k++) {
  //           let passMetricsCount = 0;
  //           if (response.allRecordDetail[k].latestCompletedRound === 1) {
  //             // console.log('last Round1');
  //             // 回饋意見
  //             console.log(response.allRecordDetail[k].Detail);
  //             for (let l = 0; l < response.allRecordDetail[k].Detail.length; l++) {
  //               if (response.allRecordDetail[k].Detail[l].score === 2) {
  //                 this.prAssignmentDetail[i].round1[this.allMetrics.indexOf(response.allRecordDetail[k].Detail[l].metrics)] += 1;
  //               } else if (response.allRecordDetail[k].Detail[l].score === 1) {
  //                 passMetricsCount += 1;
  //               }
  //               if (response.allRecordDetail[k].Detail[l].feedbackScore !== undefined) {
  //                 this.countFeedbackScore(i, response.allRecordDetail[k].Detail[l].feedbackScore);
  //               }
  //             }
  //             if (passMetricsCount === response.allRecordDetail[k].Detail.length && response.allRecordDetail[k].Detail.length !== 0) {
  //               // console.log('Round1 All Pass');
  //               this.prAssignmentDetail[i].passAllMetricsCountRound1 += 1;
  //               passMetricsCount = 0;
  //             }
  //           } else if (response.allRecordDetail[k].latestCompletedRound === 2) {
  //             // console.log('last Round2');
  //             // tslint:disable-next-line:max-line-length
  //             const reviewPageDetailResponse = await this.chartService.getReviewPageDetail(this.userNameList[j], this.prAssignmentNameList[i], response.allRecordDetail[k].id, '1').toPromise();
  //             console.log(reviewPageDetailResponse.Detail);
  //             for (let l = 0; l < reviewPageDetailResponse.Detail.length; l++) {
  //               if (reviewPageDetailResponse.Detail[l].score === 2) {
  //                 this.prAssignmentDetail[i].round1[this.allMetrics.indexOf(reviewPageDetailResponse.Detail[l].metrics)] += 1;
  //               } else if (reviewPageDetailResponse.Detail[l].score === 1) {
  //                 passMetricsCount += 1;
  //               }
  //               if (reviewPageDetailResponse.Detail[l].feedbackScore !== undefined) {
  //                 this.countFeedbackScore(i, reviewPageDetailResponse.Detail[l].feedbackScore);
  //               }
  //             }
  //             if (passMetricsCount === reviewPageDetailResponse.Detail.length && reviewPageDetailResponse.Detail.length !== 0) {
  //               // console.log('Round1 All Pass');
  //               this.prAssignmentDetail[i].passAllMetricsCountRound1 += 1;
  //               passMetricsCount = 0;
  //             }
  //             console.log(response.allRecordDetail[k].Detail);
  //             for (let l = 0; l < response.allRecordDetail[k].Detail.length; l++) {
  //               if (response.allRecordDetail[k].Detail[l].score === 2) {
  //                 this.prAssignmentDetail[i].round2[this.allMetrics.indexOf(response.allRecordDetail[k].Detail[l].metrics)] += 1;
  //               } else if (response.allRecordDetail[k].Detail[l].score === 1) {
  //                 passMetricsCount += 1;
  //               }
  //               if (response.allRecordDetail[k].Detail[l].feedbackScore !== undefined) {
  //                 this.countFeedbackScore(i, response.allRecordDetail[k].Detail[l].feedbackScore);
  //               }
  //             }
  //             if (passMetricsCount === response.allRecordDetail[k].Detail.length && response.allRecordDetail[k].Detail.length !== 0) {
  //               // console.log('Round2 All Pass');
  //               this.prAssignmentDetail[i].passAllMetricsCountRound2 += 1;
  //               passMetricsCount = 0;
  //             }
  //           } else {
  //             console.log('Not Review');
  //           }
  //         }
  //       } else {
  //         console.log('Not join this assignment');
  //       }
  //     }
  //   }
  //   this.testPrAsiignmentInformation();
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

  async initExamList () {
    const response = await this.chartService.getAllAvgScore().toPromise();
    for (let i = 0; i < response.length; i++) {
      if (response[i].type === 'EXAM') {
        this.examNameList.push(response[i].assignmentName);
      }
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
      switch (Math.floor(this.examScore[i] / 10)) {
        case 10 :
          this.examBarChartData[0].data[0] += 1;
          break;
        case 9 :
          this.examBarChartData[0].data[1] += 1;
          break;
        case 8 :
          this.examBarChartData[0].data[2] += 1;
          break;
        case 7 :
          this.examBarChartData[0].data[3] += 1;
          break;
        case 6 :
          this.examBarChartData[0].data[4] += 1;
          break;
        case 5 :
          this.examBarChartData[0].data[5] += 1;
          break;
        case 4 :
          this.examBarChartData[0].data[6] += 1;
          break;
        case 3 :
          this.examBarChartData[0].data[7] += 1;
          break;
        case 2 :
          this.examBarChartData[0].data[7] += 1;
          break;
        case 1 :
          this.examBarChartData[0].data[7] += 1;
          break;
        case 0 :
          this.examBarChartData[0].data[7] += 1;
          break;
      }
    }
  }

  countFeedbackScore(indexOfAssignment, feedbackScore: number) {
    this.prAssignmentDetail[indexOfAssignment].feedbackScoreCount += 1;
    if (feedbackScore > this.prAssignmentDetail[indexOfAssignment].feedbackScoreMax) {
      this.prAssignmentDetail[indexOfAssignment].feedbackScoreMax = feedbackScore;
    }
    this.prAssignmentDetail[indexOfAssignment].feedbackScoreTotal += feedbackScore;
  }

  getFeedbackScore() {
    let avg = 0;
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      if (this.prAssignmentDetail[i].feedbackScoreCount === 0) {
        avg = 0;
      } else {
        avg = this.prAssignmentDetail[i].feedbackScoreTotal / this.prAssignmentDetail[i].feedbackScoreCount;
        avg = Math.round((avg + Number.EPSILON) * 10) / 10;
      }
      this.feedbackMixedChartData[0].data.push(this.prAssignmentDetail[i].feedbackScoreMax);
      this.feedbackMixedChartData[1].data.push(avg);
    }
    this.isFeedbackScoreChartReady = true;
  }

  getPassAllMetricsCount() {
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      this.passAllMetricsBarChartData[0].data.push(0);
      this.passAllMetricsBarChartData[1].data.push(0);
      for (let j = 0; j < this.userNameList.length; j++) {
        if (this.prAssignmentDetail[i].passAllMetricsCountRound1[j] === 1) {
          this.passAllMetricsBarChartData[0].data[i] += 1;
        }
        if (this.prAssignmentDetail[i].passAllMetricsCountRound2[j] === 1) {
          this.passAllMetricsBarChartData[1].data[i] += 1;
        }
      }
    }
    this.isPassAllMetricsBarChartReady = true;
  }

  async getAllCategorys() {
    const response = await this.chartService.getAllCategory().toPromise();
    this.categories = response['allCategory'];
    this.getAllMetrics();
  }

  async getAllMetrics() {
    for (const category of this.categories) {
      const response = await this.chartService.getMetrics(category).toPromise();
      for (let i = 0; i < response.allMetrics.length; i++) {
        this.allMetrics.push(response.allMetrics[i].metrics);
      }
    }
    // console.log(this.allMetrics);
  }

  setMetricsCountChartData(prAssignmentName: string) {
    this.selectedMetricsAssignment = prAssignmentName;
    this.isMetricsCountChartReady = false;
    this.MetricsCountChartData[0].data = this.prAssignmentDetail[this.prAssignmentNameList.indexOf(prAssignmentName)].round1;
    this.MetricsCountChartData[1].data = this.prAssignmentDetail[this.prAssignmentNameList.indexOf(prAssignmentName)].round2;
    this.isMetricsCountChartReady = true;
  }

  testPrAsiignmentInformation() {
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      console.log(this.prAssignmentDetail[i].name);
      console.log(this.prAssignmentDetail[i].round1);
      console.log(this.prAssignmentDetail[i].round2);
      console.log(this.prAssignmentDetail[i].feedbackScoreCount);
      console.log(this.prAssignmentDetail[i].feedbackScoreTotal);
      console.log(this.prAssignmentDetail[i].feedbackScoreMax);
      console.log(this.prAssignmentDetail[i].passAllMetricsCountRound1);
      console.log(this.prAssignmentDetail[i].passAllMetricsCountRound2);
    }
  }

  calReviseRate() {
    // console.log(this.commits);
    // 判斷該作業是否為同儕審查類型作業
    // for (let i = 0; i < this.commits.length; i++) {
    //   if (this.prAssignmentNameList.includes(this.commits[i].name)) {
    //     console.log(this.commits[i].name);
    //     console.log(this.commits[i].commits);
    //     // for (let j = 0; j < this.commits[i].commits.length; j++) {
    //     //   console.log();
    //     // }
    //   }
    // }
    let needReviseCount = 0;
    let isReviseCount = 0;
    let isReviseAndPassCount = 0;
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      console.log(this.prAssignmentDetail[i].name);
      // 開放修改作業時間
      console.log(this.prAssignmentDetail[i].assessmentTimes[2]);
      console.log(this.prAssignmentDetail[i].passAllMetricsCountRound1);
      console.log(this.prAssignmentDetail[i].passAllMetricsCountRound2);
      for (let j = 0; j < this.userNameList.length; j++) {
        // 若第一輪審查未通過，檢查在修改作業期間是否上傳commit
        if (this.prAssignmentDetail[i].passAllMetricsCountRound1[j] === 0) {
          // 計算需要修改作業人數
          needReviseCount += 1;
        }
      }
    }
  }

  test($event) {
    console.log($event);
  }
}
