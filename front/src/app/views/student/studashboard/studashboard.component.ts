import { Component, OnInit } from '@angular/core';
import { StudashboardService } from './studashboard.service';
import { JwtService } from '../../../services/jwt.service';
import { User } from '../../../models/user';
@Component({
  selector: 'app-studashboard',
  templateUrl: './studashboard.component.html'
})
export class StudashboardComponent implements OnInit {
  public data: Array<any> = new Array<any>();
  public tableHead: Array<any> = new Array<any>();
  public tableData: Array<any> = new Array<any>();
  private studentDatas: JSON;
  public user: User;
  constructor(private studashboardService: StudashboardService, private jwtService?: JwtService) { }
  async ngOnInit() {
    // Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    // Add 'implements OnInit' to the class.
    await this.getAllStudentData();
  }

  async getAllStudentData() {
    // clear student array
    this.studashboardService.getAllStudentData().subscribe(response => {
      console.log(response);
      this.user = new User(this.jwtService);
      console.log('userID' + this.user.getUsername());
      this.studentDatas = response.result;
      this.tableHead = this.studentDatas[0].commits;
    });
  }

}
