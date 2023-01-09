import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-radar-chart',
  templateUrl: './radar-chart.component.html',
  styleUrls: ['./radar-chart.component.scss']
})
export class RadarChartComponent implements OnInit {

  // public radarChartLabels = ['Q1', 'Q2', 'Q3', 'Q4'];
  // public radarChartData = [
  //   {data: [120, 130, 180, 70], label: '2017'},
  //   {data: [90, 150, 200, 45], label: '2018'}
  // ];
  // public radarChartType = 'radar';

  @Input() radarChartLabels: string[];
  @Input() radarChartData: any[];
  public radarChartType = 'radar';

  public radarChartOptions = {
    scale: {
      r: {
        beginAtZero: true,
        min: 0,
        max: 100,
      },
      ticks: {
        suggestedMin: 0,
        suggestedMax: 100,
        stepSize: 20
      }
    }
  };

  constructor() { }

  ngOnInit() {
  }

}
