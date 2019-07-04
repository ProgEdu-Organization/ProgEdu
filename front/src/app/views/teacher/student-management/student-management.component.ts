import { Component, OnInit } from '@angular/core';
import { HttpService } from '../../../services/http.service'
@Component({
  selector: 'app-student-management',
  templateUrl: './student-management.component.html',
  styleUrls: ['./student-management.component.scss']
})
export class StudentManagementComponent implements OnInit {

  public users: Array<any> = new Array<any>();
  constructor(private httpService: HttpService) { }
  async ngOnInit() {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    await this.getAllUserData();
  }
  async getAllUserData() {
    const navURL = "http://140.134.26.77:8080/ProgEdu/webapi/user/getUsers";
    //clear student array

    let responese = await this.httpService.getData(navURL);
    this.users = responese.results;
    console.log(this.users);
  }
}
