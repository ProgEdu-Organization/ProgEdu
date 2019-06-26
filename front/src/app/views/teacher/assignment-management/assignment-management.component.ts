import { Component, OnInit } from '@angular/core';
import { HttpService } from '../../../services/http.service'
@Component({
  selector: 'app-assignment-management',
  templateUrl: './assignment-management.component.html',
  styleUrls: ['./assignment-management.component.scss']
})
export class AssignmentManagementComponent implements OnInit {
  projects: Array<any>;
  constructor(private http: HttpService) { }

  ngOnInit() {
    this.getAllProjects();
  }

  async getAllProjects() {
    let response = await this.http.getData('http://140.134.26.77:8080/ProgEdu/webapi/project/getAllProjects');
    this.projects = response.results;
    console.log(this.projects);
  }

  sendEditId(data) {
    console.log(data);
  }

}
