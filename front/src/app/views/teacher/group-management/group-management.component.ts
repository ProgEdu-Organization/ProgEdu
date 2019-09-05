import { Component, OnInit } from '@angular/core';
import * as $ from 'jquery';
import { GroupManagementService } from './group-management.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-group-management',
  templateUrl: './group-management.component.html'
})
export class GroupManagementComponent implements OnInit {
  public users: Array<any> = new Array<any>();
  public group: FormGroup;
  constructor(private groupManagementService: GroupManagementService, private fb: FormBuilder) { }

  ngOnInit() {
    console.log($('#multiple-select'));
    this.getAllUser();

    this.group = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(10)]],
      leader: ['', [Validators.required, Validators.maxLength(10)]],
      member: [new Array<string>(), [Validators.required]],
    });
  }

  async getAllUser() {
    this.groupManagementService.getAllUserData().subscribe(response => {
      this.users = response.Users;
    });
  }

  addGroupMemberByUsername(username: string) {
    if (!this.group.get('member').value.includes(username)) {
      this.group.get('member').value.push(username);
      console.log(this.group.value);
    }
  }

  setLeader(username: string) {
    console.log(username);
    this.group.get('leader').setValue(username);
  }

  removeGroupMemberByUsername(username: string) {
    this.group.get('member').setValue(this.group.get('member').value.filter(item => item !== username));
  }
}
