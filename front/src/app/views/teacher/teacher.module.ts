import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ChartsModule } from 'ng2-charts';
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
import { CarouselModule } from 'ngx-bootstrap/carousel';
import { ChartComponent } from './chart/chart.component';
import { FilterPipe } from '../../pipe/filter.pipe';

import { ProgressbarModule } from 'ngx-bootstrap/progressbar';

import { CollapseModule } from 'ngx-bootstrap/collapse';
import { GroupDashboardComponent } from './group-dashboard/group-dashboard.component';

import { AssignmentChoosedComponent } from '../assignment-choosed/assignment-choosed.component';
@NgModule({
    imports: [
        FormsModule,
        CommonModule,
        ChartsModule,
        BsDropdownModule,
        TeacherRoutingModule,
        ReactiveFormsModule,
        ProgressbarModule.forRoot(),
        ButtonsModule.forRoot(),
        AlertModule.forRoot(),
        ModalModule.forRoot(),
        TabsModule.forRoot(),
        CarouselModule.forRoot(),
        CollapseModule.forRoot(),
    ],
    declarations: [
        DashboardComponent,
        AssignmentChoosedComponent,
        AssignmentManagementComponent,
        CreateAssignmentComponent,
        StudentManagementComponent,
        GroupManagementComponent,
        ChartComponent,
        FilterPipe,
        GroupDashboardComponent]
})
export class TeacherModule { }
