import { Component, OnInit } from '@angular/core';
import * as $ from 'jquery';
import { GroupManagementService } from './group-management.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

const name = 'name';
const projectName = 'projectName';
const leader = 'leader';
const member = 'member';
const projectType = 'projectType';
@Component({
  selector: 'app-group-management',
  templateUrl: './group-management.component.html'
})

export class GroupManagementComponent implements OnInit {
  public users: Array<any> = new Array<any>();
  public group: FormGroup;
  public search;
  constructor(private groupManagementService: GroupManagementService, private fb: FormBuilder) { }

  public projectTypes: Array<any> = ['javac', 'maven', 'android', 'web'];
  ngOnInit() {
    console.log($('#multiple-select'));
    this.getAllUser();

    this.group = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(10)]],
      projectName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(10)]],
      projectType: ['', [Validators.required]],
      leader: ['', [Validators.required, Validators.maxLength(10)]],
      member: [new Array<string>(), Validators.minLength(3)],
    });
    this.onChanges();
    this.groupManagementService.getAllGroup().subscribe(
      response => {
        console.log(response);
      }
    );
  }
  onChanges(): void {
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

    this.group.get(projectType).valueChanges.subscribe(
      val => {
        this.group.get(projectType).value.includes(val) ? this.showIsValidById(projectType) : this.hideIsInvalidById(projectType);
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
      this.groupManagementService.createProject(this.group.get(name).value,
        this.group.get(projectName).value,
        this.group.get(projectType).value,
        this.group.get(leader).value,
        this.group.get(member).value).subscribe(
          (response) => {
            console.log(response);
          },
          error => {

          }
        );
    }
  }
}
