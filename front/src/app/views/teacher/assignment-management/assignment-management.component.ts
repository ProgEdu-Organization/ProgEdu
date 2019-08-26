import { Component, OnInit, ViewChild } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { Router } from '@angular/router';
import { AssignmentManagementService } from './assignment-management.service';

@Component({
  selector: 'app-assignment-management',
  templateUrl: './assignment-management.component.html'
})
export class AssignmentManagementComponent implements OnInit {
  @ViewChild('dangerModal', { static: false }) public dangerModal: ModalDirective;
  projects: Array<any>;
  deleteProjectName: string;
  constructor(private assignmentService: AssignmentManagementService, private router: Router) { }

  ngOnInit() {
    this.getAllProjects();
  }

  getAllProjects() {
    this.assignmentService.getAllProjects().subscribe(response => {
      console.log(response.allProjects);
      this.projects = response.allProjects;

    });
  }
  changeToCreatePage() {
    this.router.navigate(['./dashboard/assignmentManagement/create']);
  }

  changeToEditPage() {
    this.router.navigate(['./dashboard/assignmentManagement/edit']);
  }

  setdeleteProjectName(data) {
    this.deleteProjectName = data;
    console.log(data);
  }

  deleteProject() {
    console.log(this.deleteProjectName);
  }
}
