import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChartsModule } from 'ng2-charts';

import { CollapseModule } from 'ngx-bootstrap/collapse';
import { CarouselModule } from 'ngx-bootstrap/carousel';
import { FormsModule } from '@angular/forms';

import { GroupFilterPipe } from './../../pipe/group-filter.pipe';
import { FilterPipe } from './../../pipe/filter.pipe';

import { AssignmentChoosedComponent } from '../shared/assignment-choosed/assignment-choosed.component';
import { ProjectChoosedComponent } from './project-choosed/project-choosed.component';
import { GroupDashboardComponent } from '../teacher/group-dashboard/group-dashboard.component';
import { CommitRecordComponent } from './commit-record/commit-record.component';
import { DashboardStatusComponent } from './dashboard-status/dashboard-status.component';
import { LineChartComponent } from './chart/line-chart/line-chart.component';
import { ChartComponent } from './chart/chart.component';

// Tabs Component
import { TabsModule } from 'ngx-bootstrap/tabs';
import { BarChartComponent } from './chart/bar-chart/bar-chart.component';
import { BubbleChartComponent } from './chart/bubble-chart/bubble-chart.component';
import { MixedChartComponent } from './chart/mixed-chart/mixed-chart.component';
@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ChartsModule,
    TabsModule.forRoot(),
    CollapseModule.forRoot(),
    CarouselModule.forRoot(),
  ],
  declarations: [AssignmentChoosedComponent, ProjectChoosedComponent, GroupDashboardComponent,
    GroupFilterPipe, FilterPipe, CommitRecordComponent, DashboardStatusComponent, ChartComponent, LineChartComponent, BarChartComponent, BubbleChartComponent, MixedChartComponent],
  exports: [AssignmentChoosedComponent, GroupDashboardComponent, ProjectChoosedComponent, GroupFilterPipe,
    FilterPipe, DashboardStatusComponent, ChartComponent]
})
export class SharedModule { }
