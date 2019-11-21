import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CollapseModule } from 'ngx-bootstrap/collapse';
import { CarouselModule } from 'ngx-bootstrap/carousel';
import { FormsModule } from '@angular/forms';

import { GroupFilterPipe } from './../../pipe/group-filter.pipe';
import { FilterPipe } from './../../pipe/filter.pipe';

import { AssignmentChoosedComponent } from '../shared/assignment-choosed/assignment-choosed.component';
import { ProjectChoosedComponent } from './project-choosed/project-choosed.component';
import { GroupDashboardComponent } from '../teacher/group-dashboard/group-dashboard.component';
import { CommitRecordComponent } from './commit-record/commit-record.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    CollapseModule.forRoot(),
    CarouselModule.forRoot(),
  ],
  declarations: [AssignmentChoosedComponent, ProjectChoosedComponent, GroupDashboardComponent, GroupFilterPipe, FilterPipe, CommitRecordComponent],
  exports: [AssignmentChoosedComponent, GroupDashboardComponent, ProjectChoosedComponent, GroupFilterPipe, FilterPipe]
})
export class SharedModule { }
