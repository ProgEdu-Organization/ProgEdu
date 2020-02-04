import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-mixed-chart',
  templateUrl: './mixed-chart.component.html',
  styleUrls: ['./mixed-chart.component.scss']
})
export class MixedChartComponent implements OnInit {
  @Input() mixedChartLabels: Array<String>;
  @Input() mixedChartData: any[];
  public mixedChartOptions: any = {
    animation: {
      duration: 0 // general animation time
    },
    responsive: true,
    scaleShowVerticalLines: true,
    scales: {
    }
  };
  public mixedChartLegend = true;
  public mixedChartType = 'bar';
  /*
    public mixedChartData: Array<any> = [
      {
        data: [65, 59, 80, 81, 56, 55, 40], label: this.status.success.name, fill: false, type: 'line',
      },
      {
        data: [28, 48, 40, 19, 86, 27, 90], label: this.status.compilerFailure.name, fill: false, type: 'line',
      },
      {
        data: [18, 48, 77, 9, 100, 27, 40], label: this.status.checkStyleError.name, fill: false, type: 'line',
      },
      {
        data: [18, 48, 77, 9, 100, 27, 40], label: this.status.notBuild.name, fill: false, type: 'line',
      },
      {
        data: [18, 22, 77, 9, 31, 27, 40], label: this.status.testFailure.name, fill: false, type: 'line',
      },
      { data: [18, 48, 87, 31, 22, 27, 40], label: 'Count', backgroundColor: 'rgba(0, 0, 0, 0.1)' }
    ];
  */

  constructor() { }

  ngOnInit() {
  }

  public chartClicked(e: any): void {
  }

  public chartHovered(e: any): void {
  }

}
