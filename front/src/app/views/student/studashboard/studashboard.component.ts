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
<<<<<<< HEAD
  public studentDatas: JSON;
=======
  private studentDatas: JSON;
>>>>>>> a12e377a735c6c70a0d3d4766c47a9f5657ea41a
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
<<<<<<< HEAD
      console.log('userID' + this.user.getUsername());
=======
      console.log("userID" + this.user.getUserId());
>>>>>>> a12e377a735c6c70a0d3d4766c47a9f5657ea41a
      this.studentDatas = response.result;
      this.tableHead = this.studentDatas[0].commits;
    });
  }

}
