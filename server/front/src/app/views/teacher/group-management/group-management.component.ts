import { Component, OnInit, ViewChild } from '@angular/core';
import * as $ from 'jquery';
import { GroupManagementService } from './group-management.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ModalDirective } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-group-management',
  templateUrl: './group-management.component.html'
})

export class GroupManagementComponent implements OnInit {
  @ViewChild('deleteModal', { static: true }) public deleteModal: ModalDirective;

  public exitsGroups;
  public selectedGroupName;
  max: number = 100;
  showWarning: boolean;
  dynamic: number = 0;
  type: string = 'Waiting';
  isDeleteProgress = false;

  constructor(private groupManagementService: GroupManagementService, private router: Router) { }
  ngOnInit() {
    console.log($('#multiple-select'));
    this.getAllGroups();
  }

  getAllGroups() {
    this.groupManagementService.getAllGroup().subscribe(
      response => {
        this.exitsGroups = response;
      }
    );
  }

  setSelectedGroup(groupName: any) {
    if (groupName) {
      console.log(groupName);
      this.selectedGroupName = groupName;
    }
  }

  deleteGroup() {
    this.groupManagementService.deleteGroup(this.selectedGroupName).subscribe(
      (response) => {
        this.deleteModal.hide();
        this.getAllGroups();
        this.isDeleteProgress = false;
      }
    );
  }
}
