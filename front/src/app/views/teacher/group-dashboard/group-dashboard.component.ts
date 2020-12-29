import { Component, OnInit, NgModule } from '@angular/core';
import { GroupDashboardService } from './group-dashboard.service';

@Component({
  selector: 'app-group-dashboard',
  templateUrl: './group-dashboard.component.html',
  styleUrls: ['./group-dashboard.component.scss']
})
export class GroupDashboardComponent implements OnInit {
  isCollapsed = false;
  public search;
  public groups: Array<any>;
  constructor(private groupDashboardService: GroupDashboardService) { }

  ngOnInit() {
    this.groupDashboardService.getAllCommits().subscribe(
      response => {
        this.groups = response;
      }
    );
  }

  isNA(commitRecord: any) {
    if (commitRecord.length === 0) {
      return true;
    }
    return false;
  }

}
