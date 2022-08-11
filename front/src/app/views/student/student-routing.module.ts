import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { StudashboardComponent } from './studashboard/studashboard.component';
import { SharedModule } from '../shared/shared.module';
import { AssignmentChooseComponent } from '../shared/assignment-choose/assignment-choose.component';
import { ProjectChooseComponent } from '../shared/project-choose/project-choose.component';
import { GroupStudashboardComponent } from './group-studashboard/group-studashboard.component';
import { ReviewStudashboardComponent } from './review-studashboard/review-studashboard.component';
import { ReviewStatusStudashboardComponent } from './review-status-studashboard/review-status-studashboard.component';
import { ReviewAssignmentChooseComponent } from '../shared/review-assignment-choose/review-assignment-choose.component';
import { ReviewStatusAssignmentChooseComponent } from './review-status-assignment-choose/review-status-assignment-choose.component';
import { ReviewRoundStudashboardComponent } from './review-round-studashboard/review-round-studashboard.component';
import { StudentChartComponent } from './chart-studashboard/chart-studashboard.component';

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
    path: 'reviewAssignmentChoose',
    component: ReviewAssignmentChooseComponent,
    data: {
      title: 'Assignment Choose'
    }
  },
  {
    path: 'reviewStatusAssignmentChoose',
    component: ReviewStatusAssignmentChooseComponent,
    data: {
      title: 'Review Assignment Choose'
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
    path: 'peerReviewDashboard',
    component: ReviewStatusStudashboardComponent,
    data: {
      title: 'Peer Review Status Dashboard'
    }
  },
  {
    path: 'peerReviewRoundDashboard',
    component: ReviewRoundStudashboardComponent,
    data: {
      title: 'Peer Review Round Dashboard'
    }
  },
  {
    path: 'reviewDashboard',
    component: ReviewStudashboardComponent,
    data: {
      title: 'Peer Review Dashboard'
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
    path: 'studentChartDashboard',
    component: StudentChartComponent,
    data: {
      title: 'Student Chart'
    }
  }

];

@NgModule({
  imports: [RouterModule.forChild(routes), SharedModule],
  exports: [RouterModule]
})

export class StudentRoutingModule { }
