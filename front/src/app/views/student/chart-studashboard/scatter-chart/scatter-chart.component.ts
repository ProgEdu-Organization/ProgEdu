import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-scatter-chart',
  templateUrl: './scatter-chart.component.html',
  styleUrls: ['./scatter-chart.component.scss']
})
export class ScatterChartComponent implements OnInit {

  @Input() scatterChartLabels: string[];
  @Input() scatterChartData: any[];
  @Input() scatterChartLegend: boolean;
  public scatterChartOptions = {
    scaleShowVerticalLines: false,
    responsive: true
  };

  // public scatterChartLabels = ['HW1', 'HW2', 'HW3', 'HW4', 'HW5', 'HW6', 'HW7'];
  public scatterChartType = 'line';
  // public scatterChartLegend = false;

  // public scatterChartData = [
  //   {
  //     data: [65, 59, 80, 81, 56, 55, 40],
  //     labels: 'Series A',
  //     backgroundColor: 'transparent',
  //     borderColor: 'transparent',
  //     pointBackgroundColor: '#f9b115',
  //     pointHoverBackgroundColor: '#f9b115',
  //     pointHoverBorderColor: '#f9b115'
  //   },
  //   {
  //     data: [28, 48, 40, 19, 86, 27, 90],
  //     labels: 'Series B',
  //     backgroundColor: 'transparent',
  //     borderColor: 'transparent',
  //     pointBackgroundColor: '#f9b115',
  //     pointHoverBackgroundColor: '#f9b115',
  //     pointHoverBorderColor: '#f9b115'
  //   },
  //   {
  //     data: [15, 15, 15, 15, 15, 15, 15],
  //     labels: 'Series C',
  //     backgroundColor: 'transparent',
  //     borderColor: 'transparent',
  //     pointBackgroundColor: '#f9b115',
  //     pointHoverBackgroundColor: '#f9b115',
  //     pointHoverBorderColor: '#f9b115'
  //   },
  //   {
  //     data: [85, 100, 90, 46, 48, 78, 65],
  //     labels: 'Series D',
  //     backgroundColor: 'transparent',
  //     borderColor: 'transparent',
  //     pointBackgroundColor: '#f9b115',
  //     pointHoverBackgroundColor: '#f9b115',
  //     pointHoverBorderColor: '#f9b115'
  //   }
  // ];

  constructor() {
  }

  ngOnInit() {
  }

}
