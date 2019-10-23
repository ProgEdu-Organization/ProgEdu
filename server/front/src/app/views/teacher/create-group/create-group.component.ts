
import { Component, OnInit, ViewChild } from '@angular/core';
import * as $ from 'jquery';
import { CreateGroupService } from './create-group.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ModalDirective } from 'ngx-bootstrap/modal';

const name = 'name';
const projectName = 'projectName';
const leader = 'leader';
const member = 'member';
const projectType = 'projectType';


@Component({
  selector: 'app-create-group',
  templateUrl: './create-group.component.html',
  styleUrls: ['./create-group.component.scss']
})

export class CreateGroupComponent implements OnInit {
  public users: Array<any> = new Array<any>();
  public group: FormGroup;
  public search;
  public exitsGroups;
  errorMsg: string;


  max: number = 100;
  showWarning: boolean;
  dynamic: number = 0;
  type: string = 'Waiting';
  isDeleteProgress = false;

  constructor(private createGroupService: CreateGroupService, private fb: FormBuilder,
    private router: Router) { }

  @ViewChild('myModal', { static: true }) public progressModal: ModalDirective;
  @ViewChild('bsModal', { static: false }) public errorModal: ModalDirective;

  public projectTypes: Array<any> = ['javac', 'maven', 'android', 'web'];
  ngOnInit() {
    this.getAllUser();

    this.group = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(10)]],
      projectName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(10)]],
      projectType: ['', [Validators.required]],
      leader: ['', [Validators.required, Validators.maxLength(10)]],
      member: [new Array(), Validators.minLength(3)],
    });
    this.onChanges();
    this.getAllGroups();
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

  getAllGroups() {
    this.createGroupService.getAllGroup().subscribe(
      response => {
        this.exitsGroups = response;
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
    this.createGroupService.getAllUserData().subscribe(response => {
      this.users = response.Users;
      // reGet the all user data and remove exist in  group merber
      const selectedUsers = this.group.get('member').value;
      for (const i in selectedUsers) {
        if (i) {
          this.users = this.users.filter(item => {
            return item.username !== selectedUsers[i];
          });
        }
      }
    });
  }

  addGroupMemberByUsername(username: string, u_name: string) {
    if (!this.group.get('member').value.includes(username)) {
      this.group.get('member').value.push([username, u_name]);
      this.users = this.users.filter(item => item.username !== username);
    }
  }

  setLeader(username: string) {
    this.group.get('leader').setValue(username);
  }

  removeGroupMemberByUsername(username: string) {
    this.group.get('member').setValue(this.group.get('member').value.filter(item => item !== username));
    this.getAllUser();
  }

  switchToGroupDetail() {
    this.router.navigate(['./dashboard/groupManagement']);
  }

  groupSubmit() {
    if (this.group.dirty && this.group.valid) {
      this.progressModal.show();
      this.createGroupService.createProject(this.group.get(name).value,
        this.group.get(projectName).value,
        this.group.get(projectType).value,
        this.group.get(leader).value[0],
        this.group.get(member).value).subscribe(
          (response) => {
            this.progressModal.hide();
            window.location.reload();
          },
          error => {
            this.errorMsg = error.message;
            this.progressModal.hide();
            this.errorModal.show();
            console.log(error);
          });
    }
  }
}
