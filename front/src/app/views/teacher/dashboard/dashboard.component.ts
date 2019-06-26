import { Component, OnInit, NgModule } from '@angular/core';
import { HttpService } from '../../../services/http.service'
import { allStudentDatas } from '../../../containers/default-layout/_nav'

@Component({
  templateUrl: 'dashboard.component.html'
})

export class DashboardComponent implements OnInit {
  public allStudentDatas = allStudentDatas;
  public data: Array<any> = new Array<any>();
  public tableHead: Array<any> = new Array<any>();
  public tableData: Array<any> = new Array<any>();
  private studentDatas: JSON;
  constructor(private httpService: HttpService) { }
  async ngOnInit() {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    await this.getAllStudentData();
  }
  async getAllStudentData() {
    const navURL = "http://140.134.26.77:8080/ProgEdu/webapi/commits/all";
    //clear student array

    let response = await this.httpService.getData(navURL);
    this.studentDatas = response.result;
    this.tableHead = this.studentDatas[0].commits;
  }
}
