import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AssignmentManagementComponent } from './assignment-management/assignment-management.component';
import { CreateAssignmentComponent } from './create-assignment/create-assignment.component';
import { StudentManagementComponent } from './student-management/student-management.component';
import { GroupManagementComponent } from './group-management/group-management.component';
import { GroupDashboardComponent } from './group-dashboard/group-dashboard.component';
import { SharedModule } from '../shared/shared.module';

import { ChartComponent } from '../shared/chart/chart.component';
import { AssignmentChooseComponent } from '../shared/assignment-choose/assignment-choose.component';
import { ProjectChooseComponent } from '../shared/project-choose/project-choose.component';
import { EditGroupManagementComponent } from './edit-group-management/edit-group-management.component';
import { CreateGroupComponent } from './create-group/create-group.component';
import { ReviewMetricsManagementComponent } from './review-metrics-management/review-metrics-management.component';
import { ReviewStatusDashboardComponent } from './review-status-dashboard/review-status-dashboard.component';
import { ReviewDashboardComponent } from './review-dashboard/review-dashboard.component';
import { ReviewAssignmentChooseComponent } from '../shared/review-assignment-choose/review-assignment-choose.component';
import { ReviewRoundDashboardComponent } from './review-round-dashboard/review-round-dashboard.component';
import { ScoreManagementComponent } from './score-management/score-management.component';
import { EditScoreManagementComponent } from './edit-score-management/edit-score-management.component';

const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
    data: {
      title: 'Dashboard'
    },
  },
  {
    path: 'assignmentChoose',
    component: AssignmentChooseComponent,
    data: {
      title: 'Assignment Choose'
    }
  },
  {
    path: 'reviewAssignmentChoose',
    component: ReviewAssignmentChooseComponent,
    data: {
      title: 'AssignmentChoose'
    }
  },
  {
    path: 'projectChoose',
    component: ProjectChooseComponent,
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
    path: 'reviewMetricsManagement',
    component: ReviewMetricsManagementComponent,
    data: {
      title: 'Review Metrics Management'
    }
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
  },
  {
    path: 'reviewDashboard',
    component: ReviewDashboardComponent,
    data: {
      title: 'Review Dashboard'
    }
  },
  {
    path: 'reviewStatusDashboard',
    component: ReviewStatusDashboardComponent,
    data: {
      title: 'Review Status Dashboard'
    }
  },
  {
    path: 'reviewRoundDashboard',
    component: ReviewRoundDashboardComponent,
    data: {
      title: 'Review Round Dashboard'
    }
  },
  {
    path: 'scoreManagement',
    data: {
      title: 'Score Management'
    },
    children: [
      {
        path: '',
        component: ScoreManagementComponent,
      },
      {
        path: 'edit',
        component: EditScoreManagementComponent,
        data: {
          title: 'Edit Score'
        }
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes), SharedModule],
  exports: [RouterModule]
})
export class TeacherRoutingModule { }
