import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// Import Containers
import { DefaultLayoutComponent } from './containers';
import { P404Component } from './views/error/404.component';
import { P500Component } from './views/error/500.component';
import { LoginComponent } from './views/login/login.component';

// import canActive
import { CanActiveTeacherService } from './services/can-active-teacher.service';
import { CanActiveStudentService } from './services/can-active-student.service';


export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: '404',
    component: P404Component,
    data: {
      title: 'Page 404'
    }
  },
  {
    path: '500',
    component: P500Component,
    data: {
      title: 'Page 500'
    }
  },
  {
    path: 'login',
    component: LoginComponent,
    data: {
      title: 'Login Page'
    }
  },
  {
    path: '',
    component: DefaultLayoutComponent,
    data: {
      title: 'Home'
    },
    children: [
      {
        path: 'dashboard',
        canActivate: [CanActiveTeacherService],
        loadChildren: () => import('./views/teacher/teacher.module').then(m => m.TeacherModule)
      },
      {
        path: 'studashboard',
        canActivate: [CanActiveStudentService],
        loadChildren: () => import('./views/student/student.module').then(m => m.StudentModule)
      }
    ]
  },
  { path: '**', component: P404Component }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [CanActiveTeacherService, CanActiveStudentService]
})
export class AppRoutingModule { }
