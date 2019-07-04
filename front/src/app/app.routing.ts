import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// Import Containers
import { DefaultLayoutComponent } from './containers';
import { P404Component } from './views/error/404.component';
import { P500Component } from './views/error/500.component';
import { LoginComponent } from './views/login/login.component';
import { CanActiveService } from './services/can-active.service'

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
    data: {
      title: 'Home'
    },
    canActivate: [CanActiveService],
    children: [
      {
        path: 'dashboard',
        component: DefaultLayoutComponent,
        loadChildren: () => import('./views/teacher/teacher.module').then(m => m.TeacherModule)
      },
      {
        path: 'studashboard',
        component: DefaultLayoutComponent,
        loadChildren: () => import('./views/student/student.module').then(m => m.StudentModule)
      }
    ]
  },
  { path: '**', component: P404Component }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

  ngOnInit() {
    console.log(routes);
  }
}
