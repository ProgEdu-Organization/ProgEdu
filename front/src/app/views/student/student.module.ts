import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StudentRoutingModule } from './student-routing.module';
import { StudashboardComponent } from './studashboard/studashboard.component';
import { FormsModule } from '@angular/forms';
import { ChartsModule } from 'ng2-charts';
import { ProjectChoosedComponent } from './project-choosed/project-choosed.component';
import { CarouselModule } from 'ngx-bootstrap/carousel';
@NgModule({
  imports: [
    FormsModule,
    CommonModule,
    ChartsModule,
    StudentRoutingModule,
    CarouselModule.forRoot()
  ],
  declarations: [StudashboardComponent, ProjectChoosedComponent],
})
export class StudentModule { }
