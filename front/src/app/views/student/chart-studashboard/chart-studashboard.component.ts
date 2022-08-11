import { Component, OnInit, NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { User } from '../../../models/user';
import { JwtService } from '../../../services/jwt.service';
import { NgxLoadingModule, ngxLoadingAnimationTypes } from 'ngx-loading';
import { ChartsModule } from 'ng2-charts';
import { FormsModule } from '@angular/forms';
import { StudentChartService } from './chart-studashboard.service';


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
  //Assignment
  public assignmentAvgScoreTable: Array<any> = new Array<any>();
  public assignmentNameList: Array<any> = new Array<any>();
  public allAssignmentUserScore: Array<any> = new Array<any>();

  //Exam
  public examAvgScoreTable: Array<any> = new Array<any>();
  public examNameList: Array<any> = new Array<any>();
  public allExamUserScore: Array<any> = new Array<any>();
  public userList: Array<any> = new Array<any>();
  public selectedExam: string;
  public selectedExamScore: string;
  public examAvgScore: string;
  public studentExamRank: string;

  //Ranking
  public selectedAssignment: string;
  public usersRankingByAssignmentScore: Array<any> = new Array<any>();
  public peerReviewCommitRecord: Array<any> = new Array<any>();

  //Others
  public username: string;
  public assignmentScoreChartDisplay: boolean = false;
  public examBarChartDisplay: boolean = false;
  public scatterChartDisplay: boolean = false;


  constructor (private studentChartService: StudentChartService, private jwtService?: JwtService) { }

  lineChartLegend = true;

  assignmentScoreChartData = [
    {data: [], label: '班級平均', backgroundColor: 'transparent'},
    {data: [], label: '班級最高', backgroundColor: 'transparent'},
    {data: [], label: '自己', backgroundColor: 'transparent'}
  ];

  public assignmentScatterChartData: Array<any> = []

  examBarChartLabel = ['100', '90~99', '80~89', '70~79', '60~69', '50~59', '40~49', '40以下']
  examBarChartData = [
    {data: [0, 0, 0, 0, 0, 0, 0, 0]},
  ]

  async ngOnInit() {
    this.username = new User(this.jwtService).getUsername();
    await this.initAllScoreChart();
    await this.initParticipationData();
    await this.initFeedbacksAndMetricsData();
  }

  async initAllScoreChart() {
    const avgScoreTable = await this.studentChartService.getAllAvgScore().toPromise();
    for(let i = 0; i < avgScoreTable.length; i++) {
      const userScores = await this.studentChartService.getAllUsersScore(avgScoreTable[i].assignmentName).toPromise();
      //classify data
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
    this.selectedExam = this.examAvgScoreTable[0].assignmentName;
    this.selectedAssignment = this.assignmentAvgScoreTable[0].assignmentName;
    this.updateAssignmentScoreRanking(this.selectedAssignment);

    //add average score data to assignment chart 
    for(let i = 0; i < this.assignmentAvgScoreTable.length; i++) {
      this.assignmentScoreChartData[0].data.push(this.assignmentAvgScoreTable[i].averageScore);
    }

    //add the highest score data and my score to assignment chart
    for(let i = 0; i < this.allAssignmentUserScore.length; i++) {
      let maxScore = 0;
      for(let j = 0; j < this.allAssignmentUserScore[i].length; j++) {
        //the highest score data
        if(parseInt(this.allAssignmentUserScore[i][j].score, 10) > maxScore) {
          maxScore = parseInt(this.allAssignmentUserScore[i][j].score, 10);
        }
        //my score
        if(this.allAssignmentUserScore[i][j].userName === this.username) {
          if(this.allAssignmentUserScore[i][j].score < 0 || this.allAssignmentUserScore[i][j] == undefined) {
            this.assignmentScoreChartData[2].data.push(0);
          } else {
            this.assignmentScoreChartData[2].data.push(this.allAssignmentUserScore[i][j].score);
          }
        }
      }
      this.assignmentScoreChartData[1].data.push(maxScore);
    }
    this.assignmentScoreChartDisplay = true;

    //init assignment scatter chart data
    const allUserResponse = await this.studentChartService.getAllUser().toPromise();
    this.userList = allUserResponse.Users;
    for(let i = 0; i < this.userList.length; i++) {
      this.assignmentScatterChartData.push({
        data: this.getOneUserAssignmentScoreList(this.userList[i].username),
        label: this.userList[i].username,
        backgroundColor: 'transparent',
        borderColor: 'transparent',
        pointBackgroundColor: '#f9b115',
        pointHoverBackgroundColor: '#f9b115',
        pointHoverBorderColor: '#f9b115'
      })
    }
    this.scatterChartDisplay = true;

    //count exam score rank
    this.setExamScoreData(this.selectedExam);
  }

  async initParticipationData() {
    this.peerReviewCommitRecord = await this.studentChartService.getOneUserPeerReviewCommitRecord(this.username).toPromise();
    
  }

  async initFeedbacksAndMetricsData() {

  }

  getOneUserAssignmentScoreList(username: string) {
    const scoreList = []
    for(let i = 0; i < this.allAssignmentUserScore.length; i++) {
      for(let j = 0; j < this.allAssignmentUserScore[i].length; j++) {
        if(this.allAssignmentUserScore[i][j].userName === username) {
          scoreList.push(this.allAssignmentUserScore[i][j].score);
        }
      }
    }
    return scoreList;
  }
  

  setExamScoreData(examName: string) {
    for(let i = 0; i < this.examAvgScoreTable.length; i++) {
      if(this.examAvgScoreTable[i].assignmentName === examName) {
        this.examAvgScore = this.examAvgScoreTable[i].averageScore;
        break;
      }
    }
    for(let i = 0; i < this.examNameList.length; i++) {
      if(this.examNameList[i] === examName) {
        const scoreList = [];
        for(let j = 0; j < this.allExamUserScore[i].length; j++) {
          if(this.allExamUserScore[i][j].userName === this.username) {
            this.selectedExamScore = this.allExamUserScore[i][j].score;
          }
          scoreList.push(parseInt(this.allExamUserScore[i][j].score, 10));
        }
        let examRank = scoreList.length;
        for(let k = 0; k < scoreList.length; k++) {
          if(this.selectedExamScore > scoreList[k]) {
            examRank-=1;
          }
        }
        this.studentExamRank = examRank.toString();
        break;
      }
    }
    this.updateExamDistributeData(examName);
  }

  updateExamDistributeData(examName: string) {
    this.examBarChartDisplay = false;
    this.examBarChartData = [
      {data: [0, 0, 0, 0, 0, 0, 0, 0]},
    ]
    let selectedExamIndex = 0;
    for(let i = 0; i < this.examNameList.length; i++) {
      if(this.examNameList[i] === examName) {
        selectedExamIndex = i;
        break;
      }
    }
    for(let i = 0; i < this.allExamUserScore[selectedExamIndex].length; i++) {
      let scoreRank = Math.floor(this.allExamUserScore[selectedExamIndex][i].score / 10)
      if(scoreRank < 4) {
        this.examBarChartData[0].data[7] += 1;
      } else {
        this.examBarChartData[0].data[10-scoreRank] += 1;
      }
    }
    this.examBarChartDisplay = true;
  }

  getHighestScore(scoreArray: Array<any>) {
    return Math.max.apply(scoreArray)
  }

  updateAssignmentScoreRanking(assignmentName: string) {
    this.selectedAssignment = assignmentName;
    this.usersRankingByAssignmentScore = [];
    let selectedAssignmentIndex = 0;
    for(let i = 0; i < this.assignmentNameList.length; i++) {
      if(this.assignmentNameList[i] === assignmentName) {
        selectedAssignmentIndex = i;
        break;
      }
    }
    //Sort array by score
    const sortedUserScoreList = this.allAssignmentUserScore[selectedAssignmentIndex].sort(
      function(a, b) {
      return b.score - a.score;
    });

    for(let i = 0; i < 5; i++) {
      this.usersRankingByAssignmentScore.push(sortedUserScoreList[i].userName);
    }
  }

}