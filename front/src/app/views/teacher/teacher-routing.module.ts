import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DashboardComponent } from './dashboard/dashboard.component';
import { DashProjectChoosedComponent } from './dash-project-choosed/dash-project-choosed.component'
import { AssignmentManagementComponent } from './assignment-management/assignment-management.component'
const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
    data: {
      title: 'Dashboard'
    }
  },
  {
    path: 'dashprojectChoosed',
    component: DashProjectChoosedComponent,
    data: {
      title: 'Project'
    }
  },
  {
    path: 'assignmentManagement',
    component: AssignmentManagementComponent,
    data: {
      title: 'Assignment Management'
    },
    children: [
      {
        path: 'create',
        component: DashProjectChoosedComponent,
        data: {
          title: 'Create Project'
        }
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TeacherRoutingModule { }
