import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-line-chart',
  templateUrl: './line-chart.component.html',
  styleUrls: ['./line-chart.component.scss']
})
export class LineChartComponent implements OnInit {


  @Input() lineChartLabels: string[];
  @Input() lineChartData: any[];
  @Input() lineChartLegend: boolean;

  public lineChartOptions = {
    borderJoinStyle: 'miter',
    scaleShowVerticalLines: false,
    responsive: true,
    scales: {
      y: {
        beginAtZero: true,
        min: 0,
        max: 100
      }
    }
  };


  // public lineChartLabels = ['HW1', 'HW2', 'HW3', 'HW4', 'HW5', 'HW6', 'HW7'];
  //public lineChartType = 'line';

  // public lineChartData = [
  //   {
  //     data: [65, 59, 80, 81, 56, 55, 40],
  //     labels: 'Series A',
  //     backgroundColor: 'transparent',
  //     borderColor: '#f9b115',
  //     pointBackgroundColor: '#f9b115',
  //     pointHoverBackgroundColor: '#f9b115',
  //     pointHoverBorderColor: '#f9b115',
  //   },
  //   {
  //     data: [28, 48, 40, 19, 86, 27, 90],
  //     labels: 'Series B',
  //     backgroundColor: 'transparent',
  //     borderColor: '#e55353',
  //     pointBackgroundColor: '#e55353',
  //     pointHoverBackgroundColor: '#e55353',
  //     pointHoverBorderColor: '#e55353'
  //   },
  //   {
  //     data: [15, 15, 15, 15, 15, 15, 15],
  //     labels: 'Series C',
  //     backgroundColor: 'transparent',
  //     borderColor: '#2eb85c',
  //     pointBackgroundColor: '#2eb85c',
  //     pointHoverBackgroundColor: '#2eb85c',
  //     pointHoverBorderColor: '#2eb85c'
  //   },
  //   {
  //     data: [85, 100, 90, 46, 48, 78, 65],
  //     labels: 'Series D',
  //     backgroundColor: 'transparent',
  //     borderColor: '#3399ff',
  //     pointBackgroundColor: '#3399ff',
  //     pointHoverBackgroundColor: '#3399ff',
  //     pointHoverBorderColor: '#3399ff'
  //   }
  // ];

  lineChartType = 'line';

  constructor() { }

  ngOnInit() {
  }

}
