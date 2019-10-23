import { Component, OnInit, NgModule } from '@angular/core';
import { GroupStudashboardService } from './group-studashboard.service';
import { ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-group-studashboard',
  templateUrl: './group-studashboard.component.html',
  styleUrls: ['./group-studashboard.component.scss']
})
export class GroupStudashboardComponent implements OnInit {
  public username;
  isCollapsed = false;
  public search;
  public groups: Array<any>;
  constructor(private groupStudashboardService: GroupStudashboardService, private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.username = this.activatedRoute.snapshot.queryParamMap.get('username');
    this.groupStudashboardService.getAllCommits(this.username).subscribe(
      (response) => {
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
