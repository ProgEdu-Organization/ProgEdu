import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AssignmentManagementComponent } from './assignment-management/assignment-management.component';
import { CreateAssignmentComponent } from './create-assignment/create-assignment.component';
import { StudentManagementComponent } from './student-management/student-management.component';
import { GroupManagementComponent } from './group-management/group-management.component';
import { ChartComponent } from './chart/chart.component';
import { GroupDashboardComponent } from './group-dashboard/group-dashboard.component';
import { SharedModule } from '../shared/shared.module';
import { AssignmentChoosedComponent } from '../shared/assignment-choose/assignment-choose.component';
import { ProjectChoosedComponent } from '../shared/project-choose/project-choose.component';
import { EditGroupManagementComponent } from './edit-group-management/edit-group-management.component';
import { CreateGroupComponent } from './create-group/create-group.component';
const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
    data: {
      title: 'Dashboard'
    },
  },
  {
    path: 'assignmentChoosed',
    component: AssignmentChoosedComponent,
    data: {
      title: 'Assignment Choosed'
    }
  },
  {
    path: 'projectChoosed',
    component: ProjectChoosedComponent,
    data: {
      title: 'Project Choosed'
    }
  },
  {
    path: 'assignmentManagement',
    data: {
      title: 'Assignment'
    },
    children: [
      {
        path: '', component: AssignmentManagementComponent,
        data: {
          title: 'Assignment Management'
        }
      },
      {
        path: 'create',
        component: CreateAssignmentComponent,
        data: {
          title: 'Create Assignment'
        },
      },
    ]
  },
  {
    path: 'studentManagement',
    component: StudentManagementComponent,
    data: {
      title: 'Student Management'
    }
  },
  {
    path: 'groupManagement',
    data: {
      title: 'Group Management'
    },
    children: [
      {
        path: '',
        component: GroupManagementComponent,
      },
      {
        path: 'edit',
        component: EditGroupManagementComponent,
        data: {
          title: 'Edit Group'
        }
      },
      {
        path: 'create',
        component: CreateGroupComponent,
        data: {
          title: 'Create Group'
        }
      }
    ]
  },
  {
    path: 'chart',
    component: ChartComponent,
    data: {
      title: 'Chart'
    }
  },
  {
    path: 'groupDashboard',
    component: GroupDashboardComponent,
    data: {
      title: 'Group Dashboard'
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes), SharedModule],
  exports: [RouterModule]
})
export class TeacherRoutingModule { }
