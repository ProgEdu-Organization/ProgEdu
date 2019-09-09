import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StudentRoutingModule } from './student-routing.module';
import { StudashboardComponent } from './studashboard/studashboard.component';
import { FormsModule } from '@angular/forms';
import { ChartsModule } from 'ng2-charts';
import { AssignmentChoosedComponent } from '../assignment-choosed/assignment-choosed.component';
import { CollapseModule } from 'ngx-bootstrap/collapse';
import { CarouselModule } from 'ngx-bootstrap/carousel';


@NgModule({
  imports: [
    FormsModule,
    CommonModule,
    ChartsModule,
    StudentRoutingModule,
    CollapseModule.forRoot(),
    CarouselModule.forRoot(),
  ],
  declarations: [StudashboardComponent, AssignmentChoosedComponent],
})
export class StudentModule { }
