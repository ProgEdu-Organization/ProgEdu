import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-bubble-chart',
  templateUrl: './bubble-chart.component.html',
  styleUrls: ['./bubble-chart.component.scss']
})
export class BubbleChartComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  // bubble Chart ////////////////////////////////////////
  public bubbleChartType = 'bar';
  public bubbleChartData: any[] = [
    { data: [{ x: 4, y: 4, z: 1 }], label: 'HW1', radius: 20, type: 'bubble' }
  ];

  
  public chartClicked(e: any): void {
    console.log(e);
  }

  public chartHovered(e: any): void {
    console.log(e);
  }

}
