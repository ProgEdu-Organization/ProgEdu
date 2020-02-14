import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { StudashboardComponent } from './studashboard/studashboard.component';
import { SharedModule } from '../shared/shared.module';
import { AssignmentChooseComponent } from '../shared/assignment-choose/assignment-choose.component';
import { ProjectChooseComponent } from '../shared/project-choose/project-choose.component';
import { GroupStudashboardComponent } from './group-studashboard/group-studashboard.component';
const routes: Routes = [
  {
    path: '',
    component: StudashboardComponent,
    data: {
      title: 'Student Dashboard'
    }
  },
  {
    path: 'assignmentChoose',
    component: AssignmentChooseComponent,
    data: {
      title: 'Assignment Choose'
    }
  },
  {
    path: 'groupStuDashboard',
    component: GroupStudashboardComponent,
    data: {
      title: 'Project Dashboard'
    }
  },
  {
    path: 'projectChoose',
    component: ProjectChooseComponent,
    data: {
      title: 'Project Choosed'
    }
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes), SharedModule],
  exports: [RouterModule]
})

export class StudentRoutingModule { }
