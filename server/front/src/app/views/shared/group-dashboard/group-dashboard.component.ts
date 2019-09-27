import { Component, OnInit, NgModule } from '@angular/core';

@Component({
  selector: 'app-group-dashboard',
  templateUrl: './group-dashboard.component.html',
  styleUrls: ['./group-dashboard.component.scss']
})
export class GroupDashboardComponent implements OnInit {
  isCollapsed = false;
  public search;
  constructor() { }

  ngOnInit() {
  }

}
