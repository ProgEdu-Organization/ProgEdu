import { Component, OnInit } from '@angular/core';
import { ChartService } from './chart.service';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html'
})
export class ChartComponent implements OnInit {
  public commits : Array<any>;
  
  constructor(private chartService: ChartService) { }

  ngOnInit() {

    this.chartService.getAllCommits().subscribe(
      (response) => {
        this.commits = response.allCommitRecord;
      },
      (error)=>{
        console.log('get all assignments error');
      }
    );
  }
}
