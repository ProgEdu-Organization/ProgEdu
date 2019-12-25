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
  constructor(private chartService: ChartService) { }

  ngOnInit() {

    this.chartService.getAllCommits().subscribe(
      (response) => {
        console.log(response);
        this.commits = response.allCommitRecord;
        for(let i=0; i < this.commits.length; i++){
          this.labels.push(this.commits[i].name);
        }
        this.computeStatus(this.commits[0].commits);
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
        } else if (Status.compilerFailure.status.includes(commits[i].status)){
          statusCount[1] = statusCount[1] + 1;
        } else if (Status.compilerFailure.status.includes(commits[i].status)){
          statusCount[2]++;
        } else if (Status.compilerFailure.status.includes(commits[i].status)){
          statusCount[3]++;
        } else if (Status.compilerFailure.status.includes(commits[i].status)){
          statusCount[4]++;
        }
      };
    }
    console.log(statusCount);
  }
}
