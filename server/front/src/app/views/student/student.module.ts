import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StudentRoutingModule } from './student-routing.module';
import { StudashboardComponent } from './studashboard/studashboard.component';
import { FormsModule } from '@angular/forms';
import { ChartsModule } from 'ng2-charts';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  imports: [
    FormsModule,
    CommonModule,
    ChartsModule,
    StudentRoutingModule,
    SharedModule,
  ],
  declarations: [StudashboardComponent],
})
export class StudentModule { }
