import { Component, OnInit, NgModule } from '@angular/core';
import { HttpService } from '../../services/http.service'
import { allStudentDatas } from '../../containers/default-layout/_nav'
import { FilterPipe } from '../../pipe/filter.pipe'

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
    this.getTableHead();
  }
  getAllStudentData() {
    const navURL = "http://140.134.26.77:8081/ProgEdu/webapi/commits/all";
    //clear student array

    this.httpService.getData(navURL).subscribe(
      (response: any) => {
        this.studentDatas = response.result;
        console.log(this.studentDatas[2]);
        this.tableHead = this.studentDatas[0].commits;
        console.log(this.tableHead);
      },
    );
  }

  getTableHead() {
    //this.tableHead = this.studentDatas[0].commits;
    console.log(this.studentDatas);
  }
}
