import {Component, OnInit, NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {User} from '../../../models/user';
import {JwtService} from '../../../services/jwt.service';
import {NgxLoadingModule, ngxLoadingAnimationTypes} from 'ngx-loading';
import {ChartsModule} from 'ng2-charts';
import {FormsModule} from '@angular/forms';
import {StudentChartService} from './chart-studashboard.service';
import {Status} from '../../shared/chart/status';


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
  public peerReviewCommitRecord: Array<any> = new Array<any>();

  // Others
  public username: string;
  public assignmentScoreChartDisplay: boolean = false;
  public examBarChartDisplay: boolean = false;
  public scatterChartDisplay: boolean = false;
  public assignmentMasteryBarChartDisplay: boolean = false;

  // Feedback
  public feedbackMsgTop: Array<any> = [];
  public feedbackMsgLast: Array<any> = [];
  public star: Array<any> = [];
  public starAvg: string;

  // participation
  public participationOfEachUser: Array<any> = [];

  constructor(private studentChartService: StudentChartService, private jwtService?: JwtService) {
  }

  lineChartLegend = true;

  assignmentScoreChartData = [
    {data: [], label: '班級平均', backgroundColor: 'transparent'},
    {data: [], label: '班級最高', backgroundColor: 'transparent'},
    {data: [], label: '自己', backgroundColor: 'transparent'}
  ];

  public assignmentScatterChartData: Array<any> = [];

  examBarChartLabel = ['100', '90~99', '80~89', '70~79', '60~69', '50~59', '40~49', '40以下'];
  examBarChartData = [
    {data: [0, 0, 0, 0, 0, 0, 0, 0]},
  ];

  assignmentMasteryBarChartData = [
    {
      data: [], label: 'Average', fill: false, type: 'line',
      backgroundColor: '#f9b115',
      borderColor: '#f9b115',
      hoverBackgroundColor: '#f9b115',
      hoverBorderColor: '#f9b115',
    },
    {
      data: [], label: 'Max', fill: false, type: 'line',
      backgroundColor: '#e55353',
      borderColor: '#e55353',
      hoverBackgroundColor: '#e55353',
      hoverBorderColor: '#e55353',
    },
    {
      data: [], label: 'Myself',
      backgroundColor: '#3399ff',
      borderColor: '#3399ff'
    },
  ];

  async ngOnInit() {
    this.username = new User(this.jwtService).getUsername();
    await this.initAllCommit();
    await this.initAllScoreChart();
    await this.initPrAssignmentDetail();
    await this.initParticipationData();
    await this.initFeedbacksAndMetricsData();
    await this.getFeedbackScoreAvg();
    await this.getFeedbackDetail();
    await this.setAssignmentMasteryData();
    await this.getParticipationRank();
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
    this.selectedExam = this.examAvgScoreTable[this.examAvgScoreTable.length - 1].assignmentName;
    this.selectedAssignment = this.assignmentAvgScoreTable[0].assignmentName;
    this.updateAssignmentScoreRanking(this.selectedAssignment);

    // add average score data to assignment chart
    for (let i = 0; i < this.assignmentAvgScoreTable.length; i++) {
      this.assignmentScoreChartData[0].data.push(this.assignmentAvgScoreTable[i].averageScore);
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
      this.assignmentScoreChartData[1].data.push(maxScore);
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
        classAssignmentMasteryPassCount: []
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

  async initParticipationData() {
    this.peerReviewCommitRecord = await this.studentChartService.getOneUserPeerReviewCommitRecord(this.username).toPromise();

  }

  async initFeedbacksAndMetricsData() {

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
              // console.log(response.allRecordDetail[j].Detail[k].feedback);
              if (this.feedbackMsgTop.length < 5) {
                this.feedbackMsgTop.push(response.allRecordDetail[j].Detail[k].feedback);
              }
            } else if (response.allRecordDetail[j].Detail[k].score === 2) {
              // console.log('未通過');
              // console.log(response.allRecordDetail[j].Detail[k].feedback);
              if (this.feedbackMsgLast.length < 5) {
                this.feedbackMsgLast.push(response.allRecordDetail[j].Detail[k].feedback);
              }
            } else {
              console.log('Feedback score Error');
            }
            // 都達五則結束迴圈
            if (this.feedbackMsgTop.length === 5 && this.feedbackMsgLast.length === 5) {
              // console.log(this.feedbackMsgTop);
              // console.log(this.feedbackMsgLast);
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
  async getAssignmentMastery () {
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
  }

  async setAssignmentMasteryData () {
    await this.getAssignmentMastery();
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      // console.log(this.prAssignmentDetail[i].myAssignmentMasteryPassCount);
      this.assignmentMasteryBarChartData[0].data.push(this.prAssignmentDetail[i].myAssignmentMasteryPassCount);
    }
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
    for (let i = 0; i < this.allExamUserScore[selectedExamIndex].length; i++) {
      let scoreRank = Math.floor(this.allExamUserScore[selectedExamIndex][i].score / 10);
      if (scoreRank < 4) {
        this.examBarChartData[0].data[7] += 1;
      } else {
        this.examBarChartData[0].data[10 - scoreRank] += 1;
      }
    }
    this.examBarChartDisplay = true;
  }

  getHighestScore(scoreArray: Array<any>) {
    return Math.max.apply(scoreArray);
  }

  updateAssignmentScoreRanking(assignmentName: string) {
    this.selectedAssignment = assignmentName;
    this.usersRankingByAssignmentScore = [];
    let selectedAssignmentIndex = 0;
    for (let i = 0; i < this.assignmentNameList.length; i++) {
      if (this.assignmentNameList[i] === assignmentName) {
        selectedAssignmentIndex = i;
        break;
      }
    }
    // Sort array by score
    const sortedUserScoreList = this.allAssignmentUserScore[selectedAssignmentIndex].sort(
      function (a, b) {
        return b.score - a.score;
      });

    for (let i = 0; i < 5; i++) {
      this.usersRankingByAssignmentScore.push(sortedUserScoreList[i].userName);
    }
  }

  initNeedReviseNameList () {
    for (let i = 0; i < this.prAssignmentDetail.length; i++) {
      console.log(this.prAssignmentDetail[i].classAssignmentMasteryPassCount);
    }
  }

  getParticipationRank () {
    this.initParticipation();
    console.log(this.participationOfEachUser);
    console.log(this.commits);
    this.initNeedReviseNameList();
    // 每一作業
    for (let i = 0; i < this.commits.length; i++) {
      if (this.prAssignmentNameList.includes(this.commits[i].name)) {
        console.log(this.commits[i].name);
      }
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
    let tmp = [];
    for (let i = 0; i < len; i++) {
      let dev = scoreList[i] - avg;
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
      this.examBackgroundColor = '#f9b115';
    } else {
      // 紅色
      this.examBackgroundColor = '#e55353';
    }
  }
}
