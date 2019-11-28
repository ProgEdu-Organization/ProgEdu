import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CollapseModule } from 'ngx-bootstrap/collapse';
import { CarouselModule } from 'ngx-bootstrap/carousel';
import { FormsModule } from '@angular/forms';

import { GroupFilterPipe } from './../../pipe/group-filter.pipe';
import { FilterPipe } from './../../pipe/filter.pipe';


//  Modal Component
import { ModalModule } from 'ngx-bootstrap/modal';

import { AssignmentChoosedComponent } from '../shared/assignment-choosed/assignment-choosed.component';
import { ProjectChoosedComponent } from './project-choosed/project-choosed.component';
import { GroupDashboardComponent } from '../teacher/group-dashboard/group-dashboard.component';
import { CommitRecordComponent } from './commit-record/commit-record.component';
import { DashboardStatusComponent } from './dashboard-status/dashboard-status.component';
import { ScreenshotComponent } from './screenshot/screenshot.component';
import { ErrorModalComponent } from './error-modal/error-modal.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ModalModule.forRoot(),
    CollapseModule.forRoot(),
    CarouselModule.forRoot(),
  ],
  declarations: [AssignmentChoosedComponent, ProjectChoosedComponent, GroupDashboardComponent,
    GroupFilterPipe, FilterPipe, CommitRecordComponent, DashboardStatusComponent, ScreenshotComponent, ErrorModalComponent],
  exports: [AssignmentChoosedComponent, GroupDashboardComponent, ProjectChoosedComponent, GroupFilterPipe,
    FilterPipe, DashboardStatusComponent, ScreenshotComponent, ErrorModalComponent]
})
export class SharedModule { }
