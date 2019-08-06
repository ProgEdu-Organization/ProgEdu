import { Component, OnInit } from '@angular/core';
import * as $ from 'jquery';

@Component({
  selector: 'app-group-management',
  templateUrl: './group-management.component.html'
})
export class GroupManagementComponent implements OnInit {

  constructor() { }

  ngOnInit() {
    console.log($('#multiple-select'));
  }

  selectedMenber($event) {
    console.log($event);
    console.log($('#multiple-select').val());
  }
}
