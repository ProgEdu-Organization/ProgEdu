import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChartsModule } from 'ng2-charts';

import { CollapseModule } from 'ngx-bootstrap/collapse';
import { CarouselModule } from 'ngx-bootstrap/carousel';
import { FormsModule } from '@angular/forms';

import { GroupFilterPipe } from './../../pipe/group-filter.pipe';
import { FilterPipe } from './../../pipe/filter.pipe';
import { LineBreaksPipe } from './../../pipe/line-breaks.pipe';

import { AssignmentChooseComponent } from './assignment-choose/assignment-choose.component';
import { ProjectChooseComponent } from './project-choose/project-choose.component';
import { GroupDashboardComponent } from '../teacher/group-dashboard/group-dashboard.component';
import { CommitRecordComponent } from './commit-record/commit-record.component';
import { DashboardStatusComponent } from './dashboard-status/dashboard-status.component';
import { ScreenshotComponent } from './screenshot/screenshot.component';
import { ErrorModalComponent } from './error-modal/error-modal.component';
import { ModalModule } from 'ngx-bootstrap';
import { ChartComponent } from './chart/chart.component';

// Tabs Component
import { TabsModule } from 'ngx-bootstrap/tabs';
import { BarChartComponent } from './chart/bar-chart/bar-chart.component';
import { BubbleChartComponent } from './chart/bubble-chart/bubble-chart.component';
import { MixedChartComponent } from './chart/mixed-chart/mixed-chart.component';

// Pagination Component
import { PaginationModule } from 'ngx-bootstrap/pagination';

// Dropdowns Component
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ChartsModule,
    TabsModule.forRoot(),
    ModalModule.forRoot(),
    CollapseModule.forRoot(),
    CarouselModule.forRoot(),
    PaginationModule.forRoot(),
    BsDropdownModule.forRoot(),
  ],
  declarations: [AssignmentChooseComponent, ProjectChooseComponent, GroupDashboardComponent,
    GroupFilterPipe, FilterPipe, CommitRecordComponent, DashboardStatusComponent, ScreenshotComponent, ErrorModalComponent,
    BarChartComponent, BubbleChartComponent, MixedChartComponent, ChartComponent, LineBreaksPipe],
  exports: [AssignmentChooseComponent, GroupDashboardComponent, ProjectChooseComponent, GroupFilterPipe,
    FilterPipe, DashboardStatusComponent, ScreenshotComponent, ErrorModalComponent, LineBreaksPipe]
})
export class SharedModule { }
