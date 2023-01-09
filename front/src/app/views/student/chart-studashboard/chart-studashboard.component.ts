import {Component, OnInit, NgModule, CUSTOM_ELEMENTS_SCHEMA, SystemJsNgModuleLoader} from '@angular/core';
import {User} from '../../../models/user';
import {JwtService} from '../../../services/jwt.service';
import {TimeService} from '../../../services/time.service';
import {NgxLoadingModule, ngxLoadingAnimationTypes} from 'ngx-loading';
import {ChartsModule} from 'ng2-charts';
import {FormsModule} from '@angular/forms';
import {StudentChartService} from './chart-studashboard.service';
import {Status} from '../../shared/chart/status';
import {defaultThrottleConfig} from 'rxjs/internal-compatibility';


@Component({
  selector: 'chart-management',
  templateUrl: './chart-studashboard.component.html',
  styleUrls: ['./chart-studashboard.component.scss']
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

export class StudentChartComponent implements OnInit {
  // Assignment
  public assignmentAvgScoreTable: Array<any> = new Array<any>();
  public assignmentNameList: Array<any> = new Array<any>();
  public allAssignmentUserScore: Array<any> = new Array<any>();
  public prAssignmentDetail: Array<any> = [];
  public prAssignmentNameList: Array<any> = [];
  public commits: Array<any>;
  public prCommits: Array<any>;

  // Exam
  public examAvgScoreTable: Array<any> = new Array<any>();
  public examNameList: Array<any> = new Array<any>();
  public allExamUserScore: Array<any> = new Array<any>();
  public userList: Array<any> = new Array<any>();
  public selectedExam: string;
  public selectedExamScore: string;
  public examAvgScore: string;
  public studentExamRank: string;
  public selectedExamSD: number; // 標準差
  public examBackgroundColor: string;

  // Ranking
  public selectedAssignment: string;
  public usersRankingByAssignmentScore: Array<any> = new Array<any>();
  public usersRankingIndex: Array<any> = new Array<any>();
  public peerReviewCommitRecord: Array<any> = new Array<any>();
  public peerReviewRound1ParticipationCount: Array<any> = [];
  public peerReviewRound2ParticipationCount: Array<any> = [];
  public allReviewStudentCount = 0;
  public usersRankingByParticipation: Array<any> = new Array<any>();
  public participation: Array<any> = new Array<any>();

  // Others
  public username: string;
  public assignmentScoreChartDisplay: boolean = false;
  public examBarChartDisplay: boolean = false;
  public scatterChartDisplay: boolean = false;
  public assignmentMasteryBarChartDisplay: boolean = false;
  public timePercent: Array<any> = [];

  // Feedback
  public feedbackMsgTopLabel: Array<any> = [];
  public feedbackMsgLastLabel: Array<any> = [];
  public feedbackMsgTopMetrics: Array<any> = [];
  public feedbackMsgLastMetrics: Array<any> = [];
  public feedbackMsgTop: Array<any> = [];
  public feedbackMsgLast: Array<any> = [];
  public star: Array<any> = [];
  public starAvg: string;

  // participation
  public participationOfEachUser: Array<any> = [];

  // Time Format
  public commitTimeFormatted: Array<any> = [];
  public startTimeFormatted: Array<any> = [];
  public endTimeFormatted: Array<any> = [];
  public commitTimeDate = new Date();
  public startTimeDate = new Date();
  public endTimeDate = new Date();

  constructor(private studentChartService: StudentChartService, private jwtService?: JwtService) {
  }

  lineChartLegend = true;

  assignmentScoreChartData = [
    {
      data: [], label: '班級最高', fill: false, type: 'line',
      backgroundColor: '#e55353',
      borderColor: '#e55353',
      hoverBackgroundColor: '#e55353',
      hoverBorderColor: '#e55353',
    },
    {
      data: [], label: '班級平均', fill: false, type: 'line',
      backgroundColor: '#3399ff',
      borderColor: '#3399ff',
      hoverBackgroundColor: '#3399ff',
      hoverBorderColor: '#3399ff',
    },
    {
      data: [], label: '我的作業成績',
      backgroundColor: '#FEEA87',
      borderColor: '#FEEA87'
    },
  ];

  public assignmentScatterChartData: Array<any> = [];

  // examBarChartLabel = ['100', '90~99', '80~89', '70~79', '60~69', '50~59', '40~49', '40以下'];
  examBarChartLabel = ['40以下', '40~49', '50~59', '60~69', '70~79', '80~89', '90~99', '100'];
  examBarChartData = [
    {data: [0, 0, 0, 0, 0, 0, 0, 0]},
  ];

  assignmentMasteryBarChartData = [
    {
      data: [], label: '班級平均', fill: false, type: 'line',
      backgroundColor: '#3399ff',
      borderColor: '#3399ff',
      hoverBackgroundColor: '#3399ff',
      hoverBorderColor: '#3399ff',
    },
    {
      data: [], label: '班級最高', fill: false, type: 'line',
      backgroundColor: '#e55353',
      borderColor: '#e55353',
      hoverBackgroundColor: '#e55353',
      hoverBorderColor: '#e55353',
    },
    {
      data: [], label: '我的作業掌握度',
      backgroundColor: '#FEEA87',
      borderColor: '#FEEA87'
    },
  ];

  RadarChartLabel = ['完成時間', '解決問題的能力', '找出問題的能力', '課程參與率', '課程掌握度'];
  RadarChartData = [
    {
      data: [], label: '我的能力'
    },
    // {
    //   data: [], label: '班級平均'
    // },
    // {
    //   data: [], label: '班級最高'
    // }
  ];

  async ngOnInit() {
    this.username = new User(this.jwtService).getUsername();
    await this.initAllCommit();
    await this.initAllScoreChart();
    await this.initPrAssignmentDetail();
    await this.getFeedbackScoreAvg();
    await this.getFeedbackDetail();
    await this.getAssignmentMastery();
    await this.updateClassParticipationRanking();
    await this.getRadarData();
    await console.log('Success');
  }

  async initAllCommit() {
    const response = await this.studentChartService.getAllCommits().toPromise();
    this.commits = response.allCommitRecord;
  }

  async initAllScoreChart() {
    const avgScoreTable = await this.studentChartService.getAllAvgScore().toPromise();
    for (let i = 0; i < avgScoreTable.length; i++) {
      const userScores = await this.studentChartService.getAllUsersScore(avgScoreTable[i].assignmentName).toPromise();
      // classify data
      if (avgScoreTable[i].type === 'EXAM') {
        this.examAvgScoreTable.push(avgScoreTable[i]);
        this.examNameList.push(avgScoreTable[i].assignmentName);
        this.allExamUserScore.push(userScores);
      } else {
        this.assignmentAvgScoreTable.push(avgScoreTable[i]);
        this.assignmentNameList.push(avgScoreTable[i].assignmentName);
        this.allAssignmentUserScore.push(userScores);
      }
    }
    // console.log(this.examAvgScoreTable);
    if (this.examAvgScoreTable.length === 0) {
      this.selectedExam = '';
    } else {
      this.selectedExam = this.examAvgScoreTable[this.examAvgScoreTable.length - 1].assignmentName;
    }
    if (this.assignmentAvgScoreTable[0]) {
      this.selectedAssignment = this.assignmentAvgScoreTable[0].assignmentName;
    } else {
      this.selectedAssignment = '';
    }
    this.updateAssignmentScoreRanking(this.selectedAssignment);

    // add average score data to assignment chart
    for (let i = 0; i < this.assignmentAvgScoreTable.length; i++) {
      this.assignmentScoreChartData[1].data.push(this.assignmentAvgScoreTable[i].averageScore);
    }

    // add the highest score data and my score to assignment chart
    for (let i = 0; i < this.allAssignmentUserScore.length; i++) {
      let maxScore = 0;
      for (let j = 0; j < this.allAssignmentUserScore[i].length; j++) {
        // the highest score data
        if (parseInt(this.allAssignmentUserScore[i][j].score, 10) > maxScore) {
          maxScore = parseInt(this.allAssignmentUserScore[i][j].score, 10);
        }
        // my score
        if (this.allAssignmentUserScore[i][j].userName === this.username) {
          if (this.allAssignmentUserScore[i][j].score < 0 || this.allAssignmentUserScore[i][j] === undefined) {
            this.assignmentScoreChartData[2].data.push(0);
          } else {
            this.assignmentScoreChartData[2].data.push(this.allAssignmentUserScore[i][j].score);
          }
        }
      }
      this.assignmentScoreChartData[0].data.push(maxScore);
    }
    this.assignmentScoreChartDisplay = true;

    // init assignment scatter chart data
    const allUserResponse = await this.studentChartService.getAllUser().toPromise();
    this.userList = allUserResponse.Users;
    for (let i = 0; i < this.userList.length; i++) {
      this.assignmentScatterChartData.push({
        data: this.getOneUserAssignmentScoreList(this.userList[i].username),
        label: this.userList[i].username,
        backgroundColor: 'transparent',
        borderColor: 'transparent',
        pointBackgroundColor: '#f9b115',
        pointHoverBackgroundColor: '#f9b115',
        pointHoverBorderColor: '#f9b115'
      });
    }
    this.scatterChartDisplay = true;

    // count exam score rank
    this.setExamScoreData(this.selectedExam);
  }

  async initPrAssignmentDetail() {
    const allPeerReviewAsignment = await this.studentChartService.getAllPeerReviewAssignment().toPromise();
    for (let i = 0; i < allPeerReviewAsignment.allReviewAssignments.length; i++) {
      this.prAssignmentNameList.push(allPeerReviewAsignment.allReviewAssignments[i].name);
      this.prAssignmentDetail.push(
        {
          name: allPeerReviewAsignment.allReviewAssignments[i].name,
          amount: allPeerReviewAsignment.allReviewAssignments[i].amount,
          round: allPeerReviewAsignment.allReviewAssignments[i].round,
          deadline: allPeerReviewAsignment.allReviewAssignments[i].assessmentTimes,
          // Each Metrics是否通過
          myAssignmentMastery: [],
          // Metrics 通過數量
          classAssignmentMasteryPassCount: [],
          // 是否完成作業
          classIsPayAssignment: [],
          // 是否需要修改作業
          isNeedRevise: [],
          // 是否修改作業
          isRevise: []
        }
      );
    }
  }

  initParticipation() {
    for (let i = 0; i < this.userList.length; i++) {
      this.participationOfEachUser.push(
        {
          name: this.userList[i].username,
          isPayAssignment: [],
          isPRRound1: [],
          isReviseAssignment: [],
          isPRRound2: []
        }
      );
    }
  }

  async getFeedbackDetail() {
    // 此使用者名稱
    // console.log(this.username);
    for (let i = this.prAssignmentDetail.length - 1; i >= 0; i--) {
      // console.log(this.prAssignmentDetail[i].name);
      const response = await this.studentChartService.getReviewFeedback(this.prAssignmentDetail[i].name, this.username).toPromise();
      for (let j = 0; j < response.allRecordDetail.length; j++) {
        if (response.allRecordDetail[j].Detail) {
          for (let k = 0; k < response.allRecordDetail[j].Detail.length; k++) {
            if (response.allRecordDetail[j].Detail[k].score === 1) {
              // console.log('已通過');
              if (this.feedbackMsgTop.length < 5 && response.allRecordDetail[j].Detail[k].feedback !== '') {
                this.feedbackMsgTopLabel.push(this.prAssignmentDetail[i].name);
                this.feedbackMsgTopMetrics.push(response.allRecordDetail[j].Detail[k].metrics);
                this.feedbackMsgTop.push(response.allRecordDetail[j].Detail[k].feedback);
              }
            } else if (response.allRecordDetail[j].Detail[k].score === 2) {
              // console.log('未通過');
              if (this.feedbackMsgLast.length < 5) {
                this.feedbackMsgLastLabel.push(this.prAssignmentDetail[i].name);
                this.feedbackMsgLastMetrics.push(response.allRecordDetail[j].Detail[k].metrics);
                this.feedbackMsgLast.push(response.allRecordDetail[j].Detail[k].feedback);
              }
            } else {
              console.log('Feedback score Error');
            }
            // 都達五則結束迴圈
            if (this.feedbackMsgTop.length === 5 && this.feedbackMsgLast.length === 5) {
              // console.log(this.feedbackMsgTopLabel);
              // console.log(this.feedbackMsgLastLabel);
              return;
            }
          }
        }
      }
    }
  }

  async getFeedbackScoreAvg() {
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      for (let j = 1; j <= this.prAssignmentDetail[i].round; j++) {
        for (let k = 1; k <= this.prAssignmentDetail[i].amount; k++) {
          // 自己給別人的回饋 order 第幾個審查學生
          // tslint:disable-next-line:max-line-length
          const response = await this.studentChartService.getReviewRoundDetail(this.username, this.prAssignmentDetail[i].name, j.toString(), k.toString()).toPromise();
          if (response.roundStatusDetail[0].Detail) {
            for (let l = 0; l < response.roundStatusDetail[0].Detail.length; l++) {
              if (response.roundStatusDetail[0].Detail[l].feedbackScore) {
                this.star.push(response.roundStatusDetail[0].Detail[l].feedbackScore);
              }
            }
          }
        }
      }
    }
    // console.log(this.star);
    let starSum = 0;
    for (let i = 0; i < this.star.length; i++) {
      starSum += this.star[i];
    }
    this.starAvg = (starSum / this.star.length).toFixed(2);
    // console.log(this.starAvg);
  }

  // 作業掌握度
  async getAssignmentMastery() {
    let count = 0;
    // 每一作業
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      // console.log(this.prAssignmentDetail[i].name);
      // 每位同學
      for (let j = 0; j < this.userList.length; j++) {
        // console.log(this.userList[j].username);
        // tslint:disable-next-line:max-line-length
        const response = await this.studentChartService.getReviewFeedback(this.prAssignmentDetail[i].name, this.userList[j].username).toPromise();
        // 每個審查
        for (let k = 0; k < response.allRecordDetail.length; k++) {
          if (response.allRecordDetail[k].Detail) {
            // console.log(response.allRecordDetail[k].Detail);
            // 每個 Metrics
            for (let l = 0; l < response.allRecordDetail[k].Detail.length; l++) {
              // console.log(response.allRecordDetail[k].Detail[l].score);
              if (response.allRecordDetail[k].Detail[l].score === 1) {
                count += 1;
                // console.log('通過');
              } else if (response.allRecordDetail[k].Detail[l].score === 2) {
                // console.log('不通過');
              }
            }
          } else {
            if (response.allRecordDetail[k].latestCompletedRound === 2) {
              count += 6;
            }
          }
        }
        this.prAssignmentDetail[i].classAssignmentMasteryPassCount.push(count);
        count = 0;
      }
      // console.log(this.prAssignmentDetail[i].classAssignmentMasteryPassCount);
    }
    const nameList = Object.values(this.userList).map(item => item.username);
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      let sum = 0;
      // console.log(this.prAssignmentDetail[i].classAssignmentMasteryPassCount);
      for (let j = 0; j < this.prAssignmentDetail[i].classAssignmentMasteryPassCount.length; j++) {
        sum += this.prAssignmentDetail[i].classAssignmentMasteryPassCount[j];
      }
      // tslint:disable-next-line:max-line-length
      this.assignmentMasteryBarChartData[0].data.push(Number((sum / this.prAssignmentDetail[i].classAssignmentMasteryPassCount.length).toFixed(1)));
      this.assignmentMasteryBarChartData[1].data.push(Math.max(...this.prAssignmentDetail[i].classAssignmentMasteryPassCount));
      // tslint:disable-next-line:max-line-length
      this.assignmentMasteryBarChartData[2].data.push(this.prAssignmentDetail[i].classAssignmentMasteryPassCount[nameList.indexOf(this.username)]);
    }
    // console.log(this.assignmentMasteryBarChartData);
    this.assignmentMasteryBarChartDisplay = true;
  }

  getOneUserAssignmentScoreList(username: string) {
    const scoreList = [];
    for (let i = 0; i < this.allAssignmentUserScore.length; i++) {
      for (let j = 0; j < this.allAssignmentUserScore[i].length; j++) {
        if (this.allAssignmentUserScore[i][j].userName === username) {
          scoreList.push(this.allAssignmentUserScore[i][j].score);
        }
      }
    }
    return scoreList;
  }

  setExamScoreData(examName: string) {
    this.selectedExam = examName;
    for (let i = 0; i < this.examAvgScoreTable.length; i++) {
      if (this.examAvgScoreTable[i].assignmentName === examName) {
        this.examAvgScore = this.examAvgScoreTable[i].averageScore;
        break;
      }
    }
    for (let i = 0; i < this.examNameList.length; i++) {
      if (this.examNameList[i] === examName) {
        const scoreList = [];
        for (let j = 0; j < this.allExamUserScore[i].length; j++) {
          if (this.allExamUserScore[i][j].userName === this.username) {
            this.selectedExamScore = this.allExamUserScore[i][j].score;
          }
          scoreList.push(parseInt(this.allExamUserScore[i][j].score, 10));
        }
        let examRank = scoreList.length;
        for (let k = 0; k < scoreList.length; k++) {
          if (this.selectedExamScore > scoreList[k]) {
            examRank -= 1;
          }
        }
        this.studentExamRank = examRank.toString();
        // 計算標準差
        this.selectedExamSD = this.calSD(scoreList);
        // 計算分數等級
        this.calScoreLevel(Number(this.selectedExamScore), Number(this.examAvgScore), this.selectedExamSD);
        // console.log(this.selectedExamSD);
        break;
      }
    }
    this.updateExamDistributeData(examName);
  }

  updateExamDistributeData(examName: string) {
    this.examBarChartDisplay = false;
    this.examBarChartData = [
      {data: [0, 0, 0, 0, 0, 0, 0, 0]},
    ];
    let selectedExamIndex = 0;
    for (let i = 0; i < this.examNameList.length; i++) {
      if (this.examNameList[i] === examName) {
        selectedExamIndex = i;
        break;
      }
    }
    if (this.allExamUserScore[selectedExamIndex]) {
      for (let i = 0; i < this.allExamUserScore[selectedExamIndex].length; i++) {
        const scoreRank = Math.floor(this.allExamUserScore[selectedExamIndex][i].score / 10);
        if (scoreRank < 4) {
          this.examBarChartData[0].data[0] += 1;
        } else {
          this.examBarChartData[0].data[scoreRank - 3] += 1;
        }
      }
    }
    this.examBarChartDisplay = true;
  }

  getHighestScore(scoreArray: Array<any>) {
    return Math.max.apply(scoreArray);
  }

  async updateAssignmentScoreRanking(assignmentName: string) {
    this.selectedAssignment = assignmentName;
    this.usersRankingByAssignmentScore = [];
    this.usersRankingIndex = [];
    let selectedAssignmentIndex = 0;
    const sortedScoreListSelected = [];
    let index = -1;
    for (let i = 0; i < this.assignmentNameList.length; i++) {
      if (this.assignmentNameList[i] === assignmentName) {
        selectedAssignmentIndex = i;
        break;
      }
    }
    if (this.allAssignmentUserScore[selectedAssignmentIndex]) {
      // Sort array by score
      const sortedUserScoreList = this.allAssignmentUserScore[selectedAssignmentIndex].sort(
        function (a, b) {
          return b.score - a.score;
        });

      while (true) {
        index += 1;
        if (sortedScoreListSelected.length === 5) {
          if (sortedUserScoreList[index]) {
            while (sortedUserScoreList[index].score === sortedScoreListSelected[4].score) {
              sortedScoreListSelected.push(sortedUserScoreList[index]);
              index += 1;
              if (!sortedUserScoreList[index]) {
                break;
              }
            }
          } else {
            break;
          }
          break;
        } else {
          sortedScoreListSelected.push(sortedUserScoreList[index]);
        }
      }
      for (let i = 0; i < sortedScoreListSelected.length; i++) {
        // tslint:disable-next-line:max-line-length
        const response = await this.studentChartService.getPartCommitDetail(sortedScoreListSelected[i].userName, assignmentName, '1').toPromise();
        sortedScoreListSelected[i].commitTime = Date.parse(response[response.length - 1].time);
      }

      // 分數、時間排序
      const result1 = [...sortedScoreListSelected].sort((a, b) => {
        // 針對分數排序
        if (a.score < b.score) {
          return 1;
        }
        if (a.score > b.score) {
          return -1;
        }

        // 分數相同，再針對commit time排序
        if (a.commitTime < b.commitTime) {
          return -1;
        }
        if (a.commitTime > b.commitTime) {
          return 1;
        }
      });
      for (let i = 0; i < 5; i++) {
        this.usersRankingByAssignmentScore.push(result1[i].userName);
      }
    }
  }

  initNeedReviseNameList() {
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      console.log(this.prAssignmentDetail[i].classAssignmentMasteryPassCount);
    }
  }

  /**
   * 計算標準差
   * @param scoreList
   */
  calSD(scoreList: Array<any>) {
    let avg = 0;
    const len = scoreList.length;
    let sum = 0;
    for (let i = 0; i < len; i++) {
      sum += scoreList[i];
    }
    avg = sum / len;
    const tmp = [];
    for (let i = 0; i < len; i++) {
      const dev = scoreList[i] - avg;
      tmp[i] = Math.pow(dev, 2);
    }
    let powSum = 0;
    for (let i = 0; i < tmp.length; i++) {
      if (tmp[i]) {
        powSum += tmp[i];
      }
    }
    return parseFloat(Math.sqrt(powSum / len).toFixed(2));
  }

  calScoreLevel(score: number, average: number, SD: number) {
    if (score >= average) {
      // 白色
      this.examBackgroundColor = '#ffffff';
    } else if (score >= average - SD) {
      // 黃色
      this.examBackgroundColor = '#FDF89B';
    } else {
      // 紅色
      this.examBackgroundColor = '#F3869F';
    }
  }

  async updateClassParticipationRanking() {
    const sortParticipation = [];
    // 有繳交作業數
    const isPay = [];
    // 有修改作業數
    const isRevise = [];
    // 需修改作業數
    const needRevise = [];
    for (let i = 0; i < this.userList.length; i++) {
      this.participation.push(
        {
          name: this.userList[i].username,
          participation: [],
          isPay: 0,
          isNeedRevise: 0,
          isRevise: 0
        }
      );
      isPay.push(0);
      isRevise.push(0);
      needRevise.push(0);
    }

    // 是否繳交作業
    await this.classIsPayAssignment();
    // 是否修改作業
    await this.classIsEditAssignment();
    // 是否PR
    await this.classIsReview();

    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      for (let j = 0; j < this.prAssignmentDetail[i].classIsPayAssignment.length; j++) {
        if (this.prAssignmentDetail[i].classIsPayAssignment[j]) {
          isPay[j] += 1;
        }
        if (this.prAssignmentDetail[i].isRevise[j]) {
          isRevise[j] += 1;
        }
        if (this.prAssignmentDetail[i].isNeedRevise[j]) {
          needRevise[j] += 1;
        }
      }
    }
    for (let i = 0; i < this.userList.length; i++) {
      // tslint:disable-next-line:max-line-length
      this.participation[i].participation = (isPay[i] / this.prAssignmentDetail.length) * 0.25 + (isRevise[i] / needRevise[i]) * 0.25 + (this.peerReviewRound1ParticipationCount[i] / this.allReviewStudentCount) * 0.25 + (this.peerReviewRound2ParticipationCount[i] / this.allReviewStudentCount) * 0.25;
      this.participation[i].isPay = isPay[i];
      this.participation[i].isRevise = isRevise[i];
      this.participation[i].needRevise = needRevise[i];
    }
    const result = [...this.participation].sort((a, b) => {
      // 參與度排序
      if (a.participation < b.participation) {
        return 1;
      }
      if (a.participation > b.participation) {
        return -1;
      }
      // 繳交作業數排序
      if (a.isPay < b.isPay) {
        return 1;
      }
      if (a.isPay > b.isPay) {
        return -1;
      }
      // 需修改Metrics 數排序
      if (a.needRevise > b.needRevise) {
        return 1;
      }
      if (a.needRevise < b.needRevise) {
        return -1;
      }
      // 已修改 Metrics 數排序
      if (a.isRevise < b.isRevise) {
        return 1;
      }
      if (a.isRevise > b.isRevise) {
        return -1;
      }
    });
    // console.log(result);
    for (let i = 0; i < 5; i++) {
      this.usersRankingByParticipation[i] = result[i].name;
    }
    // console.log(this.usersRankingByParticipation);
  }

  /**
   * 全班是否繳交作業紀錄
   */
  async classIsPayAssignment() {
    let assignmentIndex = -1;
    // 初始化班級是否繳交作業陣列
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      for (let j = 0; j < this.userList.length; j++) {
        this.prAssignmentDetail[i].classIsPayAssignment.push(false);
      }
    }
    for (let i = 0; i < this.commits.length; i++) {
      // 篩選是否為同儕審查之作業
      if (this.prAssignmentNameList.includes(this.commits[i].name)) {
        let count = -1;
        assignmentIndex = this.prAssignmentNameList.indexOf(this.commits[i].name);
        for (let j = 0; j < this.commits[i].commits.length; j++) {
          if (this.commits[i].commits[j].number === 1) {
            count += 1;
          } else if (this.commits[i].commits[j].number === 2) {
            this.prAssignmentDetail[assignmentIndex].classIsPayAssignment[count] = true;
          }
        }
      }
    }
  }

  /**
   * 全班是否審查(含 1、2 Round)
   */
  async classIsReview() {
    for (let i = 0; i < this.userList.length; i++) {
      this.peerReviewRound1ParticipationCount.push(0);
      this.peerReviewRound2ParticipationCount.push(0);
    }
    for (let i = 0; i < this.prAssignmentNameList.length; i++) {
      const response = await this.studentChartService.getPeerReviewStatusRoundAllUser(this.prAssignmentNameList[i]).toPromise();
      this.allReviewStudentCount += response[0].reviewRound[0].amount;
      for (let j = 0; j < response.length; j++) {
        // k = round數
        for (let k = 0; k < response[j].reviewRound.length; k++) {
          // 第一次PR
          if (k === 0) {
            this.peerReviewRound1ParticipationCount[j] += response[j].reviewRound[k].count;
          } else if (k === 1) {
            this.peerReviewRound2ParticipationCount[j] += response[j].reviewRound[k].count;
          }
        }
      }
    }
    // // 需要PR數
    // console.log(this.allReviewStudentCount);
    // // 有PR數
    // console.log(this.peerReviewRound1ParticipationCount);
    // console.log(this.peerReviewRound2ParticipationCount);
  }

  /**
   * 全班是否修改作業紀錄
   */
  async classIsEditAssignment() {
    //////////////////////////////////////////////////////////////////////
    // 判斷學生是否需要修改作業
    let pass = 0;
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      // console.log(this.prAssignmentDetail[i].name);
      for (let j = 0; j < this.userList.length; j++) {
        this.prAssignmentDetail[i].isNeedRevise.push(true);
        this.prAssignmentDetail[i].isRevise.push(false);
        // console.log(this.userList[j].username);
        const response = await this.studentChartService.getReviewFeedback(this.prAssignmentDetail[i].name, this.username).toPromise();
        let tmp = 0;
        // console.log(response.allRecordDetail);
        for (let k = 0; k < response.allRecordDetail.length; k++) {
          if (response.allRecordDetail[k].Detail) {
            // console.log(response.allRecordDetail[k].Detail);
            let count = 0;
            for (let l = 0; l < response.allRecordDetail[k].Detail.length; l++) {
              // console.log(response.allRecordDetail[k].Detail[l].score);
              if (response.allRecordDetail[k].Detail[l].score === 1) {
                // console.log('通過');
                count += 1;
              } else {
                // console.log('未通過');
              }
              if (count === response.allRecordDetail[k].Detail.length) {
                // console.log('好棒棒全部通過');
                pass += 1;
              }
            }
            if (pass === response.allRecordDetail.length) {
              // console.log('審查者全部通過');
              this.prAssignmentDetail[i].isNeedRevise[j] = false;
            }
            pass = 0;
          } else {
            tmp += 1;
            if (tmp === response.allRecordDetail[k].totalCount) {
              // 需要修改
              // console.log('沒有人Review, 需要修改');
            }
          }
        }
      }
    }
    //////////////////////////////////////////////////////////////////////
    // console.log(this.commits);
    let stuIndex = -1;
    let assignmentIndex = -1;
    let commitsIndex = -1;
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      // console.log(this.prAssignmentDetail[i].isNeedRevise);
      assignmentIndex = i;
      for (let j = 0; j < this.prAssignmentDetail[i].isNeedRevise.length; j++) {
        // 需要修改作業的同學
        if (this.prAssignmentDetail[i].isNeedRevise[j]) {
          stuIndex = j;
          // 找該作業的commit紀錄
          for (let k = 0; k < this.commits.length; k++) {
            if (this.commits[k].name === this.prAssignmentDetail[i].name) {
              commitsIndex = k;
              break;
            }
          }
          let index = -1;
          for (let k = 0; k < this.commits[commitsIndex].commits.length; k++) {
            if (this.commits[commitsIndex].commits[k].number === 1) {
              index += 1;
            } else {
              // tslint:disable-next-line:max-line-length
              if (index === stuIndex && this.compareTime(this.commits[commitsIndex].commits[k].time, this.commits[commitsIndex].assessmentTimes[2].startTime, this.commits[commitsIndex].assessmentTimes[2].endTime)) {
                // 有修改作業
                // console.log(this.prAssignmentDetail[assignmentIndex].name);
                // console.log(this.userList[stuIndex].name);
                // console.log('有修改作業');
                this.prAssignmentDetail[assignmentIndex].isRevise[stuIndex] = true;
                break;
              }
            }
            if (index > stuIndex) {
              break;
            }
          }
        }
      }
    }
    // console.log(this.prAssignmentDetail);
  }

  async getRadarData() {
    // 本學生Index
    let stuIndex = 0;
    let mesteryPassMetricsTotal = 0;
    for (let i = 0; i < this.userList.length; i++) {
      if (this.userList[i].name === this.username) {
        stuIndex = i;
        break;
      }
    }
    await this.calCompleteTime();
    await this.calAbilityToSolveProblem();
    this.RadarChartData[0].data[2] = (Number(this.starAvg) / 4) * 100;
    this.RadarChartData[0].data[3] = this.participation[stuIndex].participation * 100;
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      mesteryPassMetricsTotal += this.prAssignmentDetail[i].classAssignmentMasteryPassCount[stuIndex];
    }
    this.RadarChartData[0].data[4] = mesteryPassMetricsTotal / this.prAssignmentDetail.length;
    this.RadarChartData[0].data[4] = this.RadarChartData[0].data[4] / 18 * 100;
    console.log(this.RadarChartData[0]);
  }

  async calCompleteTime() {
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      let response = await this.studentChartService.getPartCommitDetail(this.username, this.prAssignmentDetail[i].name, '1').toPromise();
      if (response[0].totalCommit === 1) {
        this.timePercent[i] = 0;
      } else if (Math.ceil(response[0].totalCommit / 5) === 1) {
        // tslint:disable-next-line:radix
        this.calTimePercent(i, response);
      } else {
        // tslint:disable-next-line:max-line-length
        response = await this.studentChartService.getPartCommitDetail(this.username, this.prAssignmentDetail[i].name, Math.ceil(response[0].totalCommit / 5).toString()).toPromise();
        this.calTimePercent(i, response);
      }
    }
    let sum = 0;
    for (let i = 0; i < this.timePercent.length; i++) {
      if (this.timePercent[i] === undefined) {
        this.timePercent[i] = 0;
      }
      sum += this.timePercent[i];
    }
    this.RadarChartData[0].data[0] = sum / this.timePercent.length;
  }

  calTimePercent(i: number, response: any) {
    for (let j = 1; j <= response.length; j++) {
      // console.log(response[response.length - i].time);
      // tslint:disable-next-line:max-line-length
      if (this.isInDoAssignmentTime(response[response.length - j].time, this.prAssignmentDetail[i].deadline[0].startTime, this.prAssignmentDetail[i].deadline[0].endTime)) {
        // 100 - 計算完成時間佔總時間的幾趴
        // console.log('計算時間');
        // tslint:disable-next-line:max-line-length
        this.timePercent[i] = (this.endTimeDate.getTime() - this.commitTimeDate.getTime()) / (this.endTimeDate.getTime() - this.startTimeDate.getTime());
        // TODO 若第一頁都不在時間內
        this.timePercent[i] = Math.round((this.timePercent[i] * 100 + Number.EPSILON) * 10) / 10;
        // console.log(this.RadarChartData[0].data[0]);
        break;
      }
    }
  }

  compareTime(commitTime: string, startTime: string, endTime: string) {
    // 年 月 日 時 分
    let commitTimeFormatted = [];
    let startTimeFormatted = [];
    let endTimeFormatted = [];
    const commitTimeDate = new Date();
    const startTimeDate = new Date();
    const endTimeDate = new Date();
    // tslint:disable-next-line:max-line-length
    commitTimeFormatted = [Number(commitTime.split('-')[0]), Number(commitTime.split('-')[1]), Number(commitTime.split('-')[2].split('T')[0]), Number(commitTime.split('-')[2].split('T')[1].split(':')[0]) + 8, Number(commitTime.split('-')[2].split('T')[1].split(':')[1]), Number(commitTime.split('-')[2].split('T')[1].split(':')[2].split('.')[0])];
    // tslint:disable-next-line:max-line-length
    startTimeFormatted = [Number(startTime.split('-')[0]), Number(startTime.split('-')[1]), Number(startTime.split('-')[2].split(' ')[0]), Number(startTime.split('-')[2].split(' ')[1].split(':')[0]), Number(startTime.split('-')[2].split(' ')[1].split(':')[1])];
    // console.log('startTimeFormatted');
    // console.log(startTimeFormatted);
    // tslint:disable-next-line:max-line-length
    endTimeFormatted = [Number(endTime.split('-')[0]), Number(endTime.split('-')[1]), Number(endTime.split('-')[2].split(' ')[0]), Number(endTime.split('-')[2].split(' ')[1].split(':')[0]), Number(endTime.split('-')[2].split(' ')[1].split(':')[1])];
    // console.log('endTimeFormatted');
    // console.log(endTimeFormatted);
    // date format
    commitTimeDate.setFullYear(commitTimeFormatted[0]);
    commitTimeDate.setMonth(commitTimeFormatted[1]);
    commitTimeDate.setDate(commitTimeFormatted[2]);
    commitTimeDate.setHours(commitTimeFormatted[3]);
    commitTimeDate.setMinutes(commitTimeFormatted[4]);
    commitTimeDate.setSeconds(commitTimeFormatted[5]);
    startTimeDate.setFullYear(startTimeFormatted[0]);
    startTimeDate.setMonth(startTimeFormatted[1]);
    startTimeDate.setDate(startTimeFormatted[2]);
    startTimeDate.setHours(startTimeFormatted[3]);
    startTimeDate.setMinutes(startTimeFormatted[4]);
    startTimeDate.setSeconds(0);
    endTimeDate.setFullYear(endTimeFormatted[0]);
    endTimeDate.setMonth(endTimeFormatted[1]);
    endTimeDate.setDate(endTimeFormatted[2]);
    endTimeDate.setHours(endTimeFormatted[3]);
    endTimeDate.setMinutes(endTimeFormatted[4]);
    endTimeDate.setSeconds(0);
    // console.log(commitTimeDate);
    // console.log(startTimeDate);
    // console.log(endTimeDate);
    // 時間比較
    if (startTimeDate < commitTimeDate && commitTimeDate < endTimeDate) {
      // console.log('有修改');
      return true;
    } else {
      // console.log('不在修改時間內');
      return false;
    }
  }

  isInDoAssignmentTime(commitTime: string, startTime: string, endTime: string) {
    this.timeFormat(commitTime, startTime, endTime);
    // 時間比較
    if (this.startTimeDate < this.commitTimeDate && this.commitTimeDate < this.endTimeDate) {
      // console.log('有修改');
      return true;
    } else {
      // console.log('不在修改時間內');
      return false;
    }
  }

  timeFormat(commitTime: string, startTime: string, endTime: string) {
    // console.log(commitTime);
    // console.log(startTime);
    // console.log(endTime);
    // 年 月 日 時 分
    // tslint:disable-next-line:max-line-length
    this.commitTimeFormatted = [commitTime.split('-')[0], commitTime.split('-')[1], commitTime.split('-')[2].split(' ')[0], commitTime.split('-')[2].split(' ')[1].split(':')[0], commitTime.split('-')[2].split(' ')[1].split(':')[1], commitTime.split('-')[2].split(' ')[1].split(':')[2].split('.')[0]];
    // tslint:disable-next-line:max-line-length
    this.startTimeFormatted = [startTime.split('-')[0], startTime.split('-')[1], startTime.split('-')[2].split(' ')[0], startTime.split('-')[2].split(' ')[1].split(':')[0], startTime.split('-')[2].split(' ')[1].split(':')[1], startTime.split('-')[2].split(' ')[1].split(':')[2]];
    this.endTimeFormatted = [endTime.split('-')[0], endTime.split('-')[1], endTime.split('-')[2].split(' ')[0], endTime.split('-')[2].split(' ')[1].split(':')[0], endTime.split('-')[2].split(' ')[1].split(':')[1], endTime.split('-')[2].split(' ')[1].split(':')[2]];
    // console.log(this.commitTimeFormatted);
    // console.log(this.startTimeFormatted);
    // console.log(this.endTimeFormatted);
    this.commitTimeDate.setFullYear(this.commitTimeFormatted[0]);
    this.commitTimeDate.setMonth(this.commitTimeFormatted[1]);
    this.commitTimeDate.setDate(this.commitTimeFormatted[2]);
    this.commitTimeDate.setHours(this.commitTimeFormatted[3]);
    this.commitTimeDate.setMinutes(this.commitTimeFormatted[4]);
    this.commitTimeDate.setSeconds(this.commitTimeFormatted[5]);
    this.startTimeDate.setFullYear(this.startTimeFormatted[0]);
    this.startTimeDate.setMonth(this.startTimeFormatted[1]);
    this.startTimeDate.setDate(this.startTimeFormatted[2]);
    this.startTimeDate.setHours(this.startTimeFormatted[3]);
    this.startTimeDate.setMinutes(this.startTimeFormatted[4]);
    this.startTimeDate.setSeconds(0);
    this.endTimeDate.setFullYear(this.endTimeFormatted[0]);
    this.endTimeDate.setMonth(this.endTimeFormatted[1]);
    this.endTimeDate.setDate(this.endTimeFormatted[2]);
    this.endTimeDate.setHours(this.endTimeFormatted[3]);
    this.endTimeDate.setMinutes(this.endTimeFormatted[4]);
    this.endTimeDate.setSeconds(0);
  }

  async calAbilityToSolveProblem() {
    // 已解決Metrics / 被提出Metrics
    let alreadySolve = 0;
    let beRaise = 0;
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      const response = await this.studentChartService.getReviewFeedback(this.prAssignmentDetail[i].name, this.username).toPromise();
      for (let j = 0; j < response.allRecordDetail.length; j++) {
        if (response.allRecordDetail[j].Detail && response.allRecordDetail[j].latestCompletedRound === 2) {
          // 判斷被提出的問題是否被解決
          // console.log('2R');
          // 第一次審查結果
          // tslint:disable-next-line:max-line-length
          const reviewFeedbackPerStuR1 = await this.studentChartService.getReviewPageDetail(this.username, this.prAssignmentDetail[i].name, response.allRecordDetail[j].id, '1').toPromise();
          // console.log(reviewFeedbackPerStuR1.Detail);
          // console.log(response.allRecordDetail[j].Detail);
          if (reviewFeedbackPerStuR1.Detail) {
            for (let k = 0; k < reviewFeedbackPerStuR1.Detail.length; k++) {
              // 判斷若第一次審查結果須修改第二次審查結果有沒有修改正確
              if (reviewFeedbackPerStuR1.Detail[k].score === 2) {
                beRaise += 1;
                if (response.allRecordDetail[j].Detail[k].score === 1) {
                  alreadySolve += 1;
                }
              }
            }
          } else {
            // 第一輪沒有被審查第二輪才被審查
          }
        } else if (response.allRecordDetail[j].Detail && response.allRecordDetail[j].latestCompletedRound === 1) {
          // 被提出問題但尚未解決
          // console.log('1R');
          // console.log(response.allRecordDetail[j].Detail);
          for (let k = 0; k < response.allRecordDetail[j].Detail.length; k++) {
            if (response.allRecordDetail[j].Detail[k].score === 2) {
              beRaise += 1;
            }
          }
        } else {
          // console.log('未獲得回覆');
        }
      }
    }
    // console.log(beRaise);
    // console.log(alreadySolve);
    // 若都沒有被提出問題
    if (beRaise === 0) {
      this.RadarChartData[0].data[1] = 100;
    } else {
      this.RadarChartData[0].data[1] = (alreadySolve / beRaise) * 100;
    }
  }
}
