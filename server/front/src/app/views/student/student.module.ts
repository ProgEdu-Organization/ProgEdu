import { ReviewAssignmentChooseComponent } from './review-assignment-choose/review-assignment-choose.component';
import { ReviewCommitRecordComponent } from './review-commit-record/review-commit-record.component';
import { ReviewStudashboardComponent } from './review-studashboard/review-studashboard.component';
import { ReviewStatusStudashboardComponent } from './review-status-studashboard/review-status-studashboard.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModalModule } from 'ngx-bootstrap/modal';

import { StudentRoutingModule } from './student-routing.module';
import { StudashboardComponent } from './studashboard/studashboard.component';
import { FormsModule } from '@angular/forms';
import { ChartsModule } from 'ng2-charts';
import { SharedModule } from '../shared/shared.module';
import { GroupStudashboardComponent } from './group-studashboard/group-studashboard.component';
import { ReviewStatusAssignmentChooseComponent } from './review-status-assignment-choose/review-status-assignment-choose.component';

@NgModule({
  imports: [
    FormsModule,
    CommonModule,
    ChartsModule,
    StudentRoutingModule,
    ModalModule.forRoot(),
    SharedModule,
  ],
  declarations: [
    StudashboardComponent,
    GroupStudashboardComponent,
    ReviewStudashboardComponent,
    ReviewStatusStudashboardComponent,
    ReviewCommitRecordComponent,
    ReviewAssignmentChooseComponent,
    ReviewStatusAssignmentChooseComponent
  ],
})
export class StudentModule { }
