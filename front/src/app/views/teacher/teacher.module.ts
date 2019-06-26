import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ChartsModule } from 'ng2-charts';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { ButtonsModule } from 'ngx-bootstrap/buttons';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './dashboard/dashboard.component';
import { TeacherRoutingModule } from './teacher-routing.module';
import { FilterPipe } from '../../pipe/filter.pipe';
import { DashProjectChoosedComponent } from './dash-project-choosed/dash-project-choosed.component';
import { AssignmentManagementComponent } from './assignment-management/assignment-management.component'

@NgModule({
  imports: [
    FormsModule,
    CommonModule,
    ChartsModule,
    BsDropdownModule,
    TeacherRoutingModule,
    ButtonsModule.forRoot()
  ],
  declarations: [DashboardComponent, FilterPipe, DashProjectChoosedComponent, AssignmentManagementComponent]
})
export class TeacherModule { }