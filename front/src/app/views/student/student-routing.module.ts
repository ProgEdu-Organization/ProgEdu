import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { StudashboardComponent } from './studashboard/studashboard.component';
import { AssignmentChoosedComponent } from '../assignment-choosed/assignment-choosed.component';

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
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})

export class StudentRoutingModule { }
