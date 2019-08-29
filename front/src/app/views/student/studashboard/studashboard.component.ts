import { Component, OnInit } from '@angular/core';
import { StudashboardService } from './studashboard.service';
import { JwtService } from '../../../services/jwt.service';
import { User } from '../../../models/user';
@Component({
  selector: 'app-studashboard',
  templateUrl: './studashboard.component.html'
})
export class StudashboardComponent implements OnInit {
  public tableHead: Array<any> = new Array<any>();
  public studentCommitRecord: JSON;
  public username: string;
  constructor(private studashboardService: StudashboardService, private jwtService?: JwtService) { }
  async ngOnInit() {
    this.username = new User(this.jwtService).getUsername();
    await this.getAllAssignments();
    await this.getStudentCommitRecords();
  }

  async getAllAssignments() {
    this.studashboardService.getAllAssignments().subscribe(response => {
      console.log(response.allAssignments);
      this.tableHead = response.allAssignments;
    });
  }

  async getStudentCommitRecords() {
    // clear student array
    this.studashboardService.getStudentCommitRecord(this.username).subscribe(response => {
      console.log(response);
      this.studentCommitRecord = response;
    });
  }

}
