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
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(10)]],
      projectName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(10)]],
      leader: ['', [Validators.required, Validators.maxLength(10)]],
      member: [new Array<string>(), Validators.minLength(3)],
    });
    this.onChanges();
  }
  onChanges(): void {
    const name = 'name';
    const projectName = 'projectName';
    const leader = 'leader';
    const member = 'member';
    this.group.get(name).valueChanges.subscribe(
      () => {
        this.group.get(name).valid ? this.showIsValidById(name) : this.hideIsInvalidById(name);
      }
    );
    this.group.get(projectName).valueChanges.subscribe(
      () => {
        this.group.get(projectName).valid ? this.showIsValidById(projectName) : this.hideIsInvalidById(projectName);
      }
    );

    this.group.get(leader).valueChanges.subscribe(
      val => {
        this.group.get(member).value.includes(val) ? this.showIsValidById(leader) : this.hideIsInvalidById(leader);
      }
    );
  }

  showIsValidById(id: string) {
    $('#' + id).addClass('is-valid');
    $('#' + id).removeClass('is-invalid');
  }

  hideIsInvalidById(id: string) {
    $('#' + id).removeClass('is-valid');
    $('#' + id).addClass('is-invalid');
  }

  getAllUser() {
    this.groupManagementService.getAllUserData().subscribe(response => {
      this.users = response.Users;
      // reGet the all user data and remove exist in  group merber
      const selectedUsers = this.group.get('member').value;
      for (const i in selectedUsers) {
        if (i) {
          console.log(i);
          this.users = this.users.filter(item => {
            return item.username !== selectedUsers[i];
          });
        }
      }
    });
  }

  addGroupMemberByUsername(username: string) {
    if (!this.group.get('member').value.includes(username)) {
      this.group.get('member').value.push(username);
      this.users = this.users.filter(item => item.username !== username);
    }
    console.log(this.group.valid);
  }

  setLeader(username: string) {
    this.group.get('leader').setValue(username);
  }

  removeGroupMemberByUsername(username: string) {
    this.group.get('member').setValue(this.group.get('member').value.filter(item => item !== username));
    this.getAllUser();
  }

  groupSubmit() {
    if (this.group.dirty && this.group.valid) {
      console.log('test');
    }
  }
}
