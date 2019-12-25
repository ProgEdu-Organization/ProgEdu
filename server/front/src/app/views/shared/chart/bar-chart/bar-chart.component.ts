import { Component, OnInit, Input, OnChanges} from '@angular/core';

@Component({
  selector: 'app-bar-chart',
  templateUrl: './bar-chart.component.html',
  styleUrls: ['./bar-chart.component.scss']
})
export class BarChartComponent implements OnInit, OnChanges {
  @Input() commits: Array<any>;

  public barChartType = 'bar';
  public barChartLegend = true;
  public barChartLabels: string[] = ['HW1', 'HW2', 'HW3', 'HW4', 'HW6', 'HW6', 'HW7'];
  public barChartOptions: any = {
    scaleShowVerticalLines: true,
    responsive: true,
    scales: {
    }
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

  public status = {
    notBuild: {
      name: 'notBuild',
    },
    compilerFailure: {
      name: 'compilerFailure',
      color: '#ff6284'
    },
    checkStyleError: {
      name: 'checkStyleError',
      color: '#ffcf57'
    },
    testFailure: {
      name: 'testFailure',
      color: '#FFFFFF'
    },
    success: {
      name: 'success',
      color: '#35a2eb'
    }
  };

  constructor() { }

  ngOnInit() {
    
  }

  ngOnChanges(): void {
    if(this.commits){
      console.log(this.commits);
      this.barChartLabels = [];
      for(let i=0; i < this.commits.length; i++){
        this.barChartLabels.push(this.commits[i].name);
      }
    }
  }

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
  
  public chartClicked(e: any): void {
    console.log(e);
  }

  public chartHovered(e: any): void {
    console.log(e);
  }


}
