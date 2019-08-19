import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DashboardComponent } from './dashboard.component';

const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
    data: {
      title: 'Dashboard'
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule { }

/*
$.ajax({
        url : 'webapi/commits/all',
        type : 'GET',
        async : true,
        cache : true,
        contentType: 'application/json; charset=UTF-8',
        success : function(responseText) {
            var result = JSON.parse(responseText);
            setData(result.result);
        },
        error : function(responseText) {
            console.log("False!");
        }
    }); */
