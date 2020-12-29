import { Component, OnInit, Input, OnChanges} from '@angular/core';

@Component({
  selector: 'app-bar-chart',
  templateUrl: './bar-chart.component.html',
  styleUrls: ['./bar-chart.component.scss']
})
export class BarChartComponent implements OnInit, OnChanges {
  @Input() barChartLabels: string[];
  @Input() barChartData: any[];
  public barChartType = 'bar';
  public barChartLegend = true;
  public barChartOptions: any = {
    scaleShowVerticalLines: true,
    responsive: true,
    scales: {
    },
    /*
    scales: {
      xAxes: [{
        type: 'time',
        time: {
          unit: 'month'
        }
      }],

    }
    */
  };

  /*
  public barChartData: any[] = [
    {
      data: [18, 48, 87, 31, 22, 27, 40], label: this.status.compilerFailure.name
    },
    {
      data: [28, 48, 40, 19, 86, 27, 90], label: this.status.success.name
    },
    {
      data: [65, 59, 80, 81, 56, 55, 40], label: this.status.checkStyleError.name
    },
    {
      data: [28, 48, 40, 19, 86, 27, 90], label: this.status.notBuild.name
    },
    {
      data: [65, 59, 80, 81, 56, 55, 40], label: this.status.testFailure.name
    },
  ];*/

  constructor() { }

  ngOnInit() {
    
  }

  ngOnChanges(): void {
  }
  
  public chartClicked(e: any): void {
   
  }

  public chartHovered(e: any): void {
    
  }


}
