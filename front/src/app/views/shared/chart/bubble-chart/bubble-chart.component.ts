import { Component, OnInit, Input, OnChanges } from '@angular/core';

@Component({
  selector: 'app-bubble-chart',
  templateUrl: './bubble-chart.component.html',
  styleUrls: ['./bubble-chart.component.scss']
})
export class BubbleChartComponent implements OnInit {

  @Input() bubbleChartData: Array<any>;
  @Input() bubbleChartLabels: string[];
  @Input() bubbleChartLegend: boolean = false;
  @Input() title: string = '';
  constructor() { }

  ngOnInit() {
  }
  // bubble Chart ////////////////////////////////////////
  /*
  public bubbleChartData: any[] = [
    { data: [{ x: 4, y: 4, z: 1 }], label: 'HW1', radius: 20, type: 'bubble' }
  ];

  public bubbleChartLabels: string[] = ['2006', '2007', '2008', '2009', '2010', '2011', '2012'];
  */


  public bubbleChartType = 'bar';
  public bubbleChartOptions: any = {
    scaleShowVerticalLines: false,
    responsive: true,
    scales: {
      yAxes: [{
        ticks: {
          steps: 1,
          stepValue: 1,
          max: 24,
          min: 1
        }
      }]
    }
  };

  public chartClicked(e: any): void {
  }

  public chartHovered(e: any): void {
  }

}
