import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpService } from '../../../services/http.service';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { Router } from '@angular/router';

@Component({
  selector: 'app-assignment-management',
  templateUrl: './assignment-management.component.html',
  styleUrls: ['./assignment-management.component.scss']
})
export class AssignmentManagementComponent implements OnInit {
  @ViewChild('dangerModal', { static: false }) public dangerModal: ModalDirective;
  projects: Array<any>;
  deleteProjectName: string;
  constructor(private http: HttpService, private router: Router) { }

  ngOnInit() {
    this.getAllProjects();
  }

  async getAllProjects() {
    const response = await this.http.getData('http://140.134.26.77:8080/ProgEdu/webapi/project/getAllProjects');
    this.projects = response.results;
    console.log(this.projects);
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
