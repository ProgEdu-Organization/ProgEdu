import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProjectChoosedService } from './project-choosed.service';
@Component({
  selector: 'app-project-choosed',
  templateUrl: './project-choosed.component.html',
  styleUrls: ['./project-choosed.component.scss']
})
export class ProjectChoosedComponent implements OnInit {
  public groupName;
  constructor(private activeRoute: ActivatedRoute, private projectService: ProjectChoosedService) { }

  ngOnInit() {
    this.groupName = this.activeRoute.snapshot.queryParamMap.get('groupName');
    console.log(this.groupName);
    this.getGroupCommits();
  }

  getGroupCommits() {
    this.projectService.getGroupCommits().subscribe(
      (resopnse) => {
        console.log(resopnse);
      }
    );
  }

}
