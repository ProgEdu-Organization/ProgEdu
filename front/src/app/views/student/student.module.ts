import { ReviewStudashboardComponent } from './review-studashboard/review-studashboard.component';
import { ReviewStatusStudashboardComponent } from './review-status-studashboard/review-status-studashboard.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModalModule } from 'ngx-bootstrap/modal';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { StudentRoutingModule } from './student-routing.module';
import { StudashboardComponent } from './studashboard/studashboard.component';
import { FormsModule } from '@angular/forms';
import { ChartsModule } from 'ng2-charts';
import { SharedModule } from '../shared/shared.module';
import { GroupStudashboardComponent } from './group-studashboard/group-studashboard.component';
import { ReviewStatusAssignmentChooseComponent } from './review-status-assignment-choose/review-status-assignment-choose.component';
import { ReviewRoundStudashboardComponent } from './review-round-studashboard/review-round-studashboard.component';
import { StudentChartComponent } from './chart-studashboard/chart-studashboard.component';
import { LineChartComponent } from './chart-studashboard/line-chart/line-chart.component';
import { BarChartComponent } from './chart-studashboard/bar-chart/bar-chart.component';
import { ScatterChartComponent } from './chart-studashboard/scatter-chart/scatter-chart.component';
import { MixedChartComponent } from './chart-studashboard/mixed-chart/mixed-chart.component';
import { RadarChartComponent } from './chart-studashboard/radar-chart/radar-chart.component';

@NgModule({
  imports: [
    FormsModule,
    CommonModule,
    ChartsModule,
    StudentRoutingModule,
    ModalModule.forRoot(),
    BsDropdownModule,
    SharedModule,
  ],
  declarations: [
    StudashboardComponent,
    GroupStudashboardComponent,
    ReviewStudashboardComponent,
    ReviewStatusStudashboardComponent,
    ReviewRoundStudashboardComponent,
    ReviewStatusAssignmentChooseComponent,
    StudentChartComponent,
    LineChartComponent,
    BarChartComponent,
    ScatterChartComponent,
    MixedChartComponent,
    RadarChartComponent
  ],
})
export class StudentModule { }
