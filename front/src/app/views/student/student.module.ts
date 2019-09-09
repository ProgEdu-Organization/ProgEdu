import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StudentRoutingModule } from './student-routing.module';
import { StudashboardComponent } from './studashboard/studashboard.component';
import { FormsModule } from '@angular/forms';
import { ChartsModule } from 'ng2-charts';
import { AssignmentChoosedComponent } from '../assignment-choosed/assignment-choosed.component';



@NgModule({
  imports: [
    FormsModule,
    CommonModule,
    ChartsModule,
    StudentRoutingModule,
  ],
  declarations: [StudashboardComponent, AssignmentChoosedComponent],
})
export class StudentModule { }
