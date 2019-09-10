import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AssignmentChoosedComponent } from '../shared/assignment-choosed/assignment-choosed.component';
import { CollapseModule } from 'ngx-bootstrap/collapse';
import { CarouselModule } from 'ngx-bootstrap/carousel';



@NgModule({
  declarations: [AssignmentChoosedComponent],
  imports: [
    CommonModule,
    CollapseModule.forRoot(),
    CarouselModule.forRoot()
  ],
  exports: [AssignmentChoosedComponent]
})
export class SharedModule { }
