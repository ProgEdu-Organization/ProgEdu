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
      data: [], label: Status.compilerFailure.name
    },
    {
      data: [], label: Status.success.name
    },
    {
      data: [], label: Status.checkStyleError.name
    },
    {
      data: [], label: Status.notBuild.name
    },
    {
      data: [], label: Status.testFailure.name
    },
  ];

  public mixedChartData: Array<any> = [
    {
      data: [], label: Status.success.name, fill: false, type: 'line',
    },
    {
      data: [], label: Status.compilerFailure.name, fill: false, type: 'line',
    },
    {
      data: [], label: Status.checkStyleError.name, fill: false, type: 'line',
    },
    {
      data: [], label: Status.notBuild.name, fill: false, type: 'line',
    },
    {
      data: [], label: Status.testFailure.name, fill: false, type: 'line',
    },
    { data: [], label: 'Count' }
  ];

  constructor(private chartService: ChartService) { }

  ngOnInit() {

    this.chartService.getAllCommits().subscribe(
      (response) => {
        console.log(response);
        this.commits = response.allCommitRecord;
        for(let i=0; i < this.commits.length; i++){
          this.labels.push(this.commits[i].name);

          let statusCount = this.computeStatus(this.commits[i].commits);
          for(let j = 0; j < this.barChartData.length; j++){
            this.barChartData[j].data.push(statusCount[j]);
            this.mixedChartData[j].data.push(statusCount[j]);
          }
          this.mixedChartData[this.mixedChartData.length - 1].data.push(this.commits[i].commits.length);
        }
        console.log(this.mixedChartData);
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
        if(Status.compilerFailure.status.includes(commits[i].status)){
          statusCount[0] = statusCount[0] + 1;
        } else if (Status.success.status.includes(commits[i].status)){
          statusCount[1] = statusCount[1] + 1;
        } else if (Status.checkStyleError.status.includes(commits[i].status)){
          statusCount[2] = statusCount[2] + 1;
        } else if (Status.notBuild.status.includes(commits[i].status)){
          statusCount[3] = statusCount[3] + 1;
        } else if (Status.testFailure.status.includes(commits[i].status)){
          statusCount[4] = statusCount[4] + 1;
        }
      };
    }
    return statusCount;
  }
}
