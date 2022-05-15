import { ReviewStatusDashboardComponent } from './review-status-dashboard/review-status-dashboard.component';
import { ReviewRoundDashboardComponent } from './review-round-dashboard/review-round-dashboard.component';
import { ReviewDashboardComponent } from './review-dashboard/review-dashboard.component';
import { HttpClientModule } from '@angular/common/http';
import { ReviewMetricsManagementComponent } from './review-metrics-management/review-metrics-management.component';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { ButtonsModule } from 'ngx-bootstrap/buttons';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './dashboard/dashboard.component';
import { TeacherRoutingModule } from './teacher-routing.module';
import { AssignmentManagementComponent } from './assignment-management/assignment-management.component';

import { AlertModule } from 'ngx-bootstrap/alert';
import { CreateAssignmentComponent } from './create-assignment/create-assignment.component';

//  Modal Component
import { ModalModule } from 'ngx-bootstrap/modal';
// Tabs Component
import { TabsModule } from 'ngx-bootstrap/tabs';
import { StudentManagementComponent } from './student-management/student-management.component';
import { GroupManagementComponent } from './group-management/group-management.component';
// Carousel Component
import { ProgressbarModule } from 'ngx-bootstrap/progressbar';

import { CollapseModule } from 'ngx-bootstrap/collapse';
import { CarouselModule } from 'ngx-bootstrap/carousel';

import { SharedModule } from '../shared/shared.module';
import { EditGroupManagementComponent } from './edit-group-management/edit-group-management.component';
import { CreateGroupComponent } from './create-group/create-group.component';

import { CKEditorModule } from '@ckeditor/ckeditor5-angular';
import { SortablejsModule } from 'ngx-sortablejs';
import { ScoreManagementComponent } from './score-management/score-management.component';
import { EditScoreManagementComponent } from './edit-score-management/edit-score-management.component';

@NgModule({
    imports: [
        FormsModule,
        CommonModule,
        BsDropdownModule,
        TeacherRoutingModule,
        ReactiveFormsModule,
        ProgressbarModule.forRoot(),
        ButtonsModule.forRoot(),
        AlertModule.forRoot(),
        ModalModule.forRoot(),
        CollapseModule,
        CarouselModule,
        SharedModule,
        TabsModule.forRoot(),
        CKEditorModule,
        SortablejsModule.forRoot({}),
        HttpClientModule
    ],
    declarations: [
        DashboardComponent,
        AssignmentManagementComponent,
        CreateAssignmentComponent,
        StudentManagementComponent,
        GroupManagementComponent,
        GroupManagementComponent,
        EditGroupManagementComponent,
        CreateGroupComponent,
        ReviewMetricsManagementComponent,
        ReviewDashboardComponent,
        ReviewStatusDashboardComponent,
        ReviewRoundDashboardComponent,
        ScoreManagementComponent,
        EditScoreManagementComponent
    ]
})
export class TeacherModule { }
