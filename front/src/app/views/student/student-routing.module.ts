import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { StudashboardComponent } from './studashboard/studashboard.component';
import { SharedModule } from '../shared/shared.module';
import { AssignmentChoosedComponent } from '../shared/assignment-choosed/assignment-choosed.component';

const routes: Routes = [
  {
    path: '',
    component: StudashboardComponent,
    data: {
      title: 'Student Dashboard'
    }
  },
  {
    path: 'assignmentChoosed',
    component: AssignmentChoosedComponent,
    data: {
      title: 'Assignment Choose'
    }
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes), SharedModule],
  exports: [RouterModule]
})

export class StudentRoutingModule { }
