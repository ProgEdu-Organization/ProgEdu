import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html'
})
export class ChartComponent implements OnInit {
  // line Chart
  public status = {
    success: {
      name: 'success',
      color: '#35a2eb'
    },
    compilerFailure: {
      name: 'compilerFailure',
      color: '#ff6284'
    },
    checkStyleError: {
      name: 'checkStyleError',
      color: '#ffcf57'
    },
    notBuild: {
      name: 'notBuild',
    },
    testFailure: {
      name: 'testFailure',
      color: '#4bc0c0'
    }
  };

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
  public mixedChartLabels: Array<any> = ['HW1', 'HW2', 'HW3', 'HW4', 'HW5', 'HW6'];
  public mixedChartOptions: any = {
    animation: true,
    responsive: true,
  };
  public mixedChartLegend = true;
  public mixedChartType = 'bar';
  // bar Chart ////////////////////////////////////////
  public barChartOptions: any = {
    scaleShowVerticalLines: true,
    responsive: true,
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
    scales: {

    }
  };
  public barChartLabels: string[] = ['HW1', 'HW2', 'HW3', 'HW4', 'HW6', 'HW6', 'HW7'];
  public barChartType = 'bar';
  public barChartLegend = true;

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
  ];
  // bubble Chart ////////////////////////////////////////
  public bubbleChartType = 'bar';
  public bubbleChartData: any[] = [
    { data: [{ x: 4, y: 4, z: 1 }], label: 'HW1', radius: 20, type: 'bubble' }
  ];
  constructor() { }

  ngOnInit() {
  }

  public chartClicked(e: any): void {
    console.log(e);
  }

  public chartHovered(e: any): void {
    console.log(e);
  }

}
