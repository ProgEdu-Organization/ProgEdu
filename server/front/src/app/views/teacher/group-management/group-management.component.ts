import { Component, OnInit, ViewChild } from '@angular/core';
import * as $ from 'jquery';
import { GroupManagementService } from './group-management.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { HttpErrorResponse } from '@angular/common/http';

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

  errorResponse: HttpErrorResponse;
  errorTitle: string;

  constructor(private groupManagementService: GroupManagementService, private router: Router) { }
  ngOnInit() {
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
      this.selectedGroupName = groupName;
    }
  }

  deleteGroup() {
    this.isDeleteProgress = true;
    this.groupManagementService.deleteGroup(this.selectedGroupName).subscribe(
      (response) => {
        this.deleteModal.hide();
        this.getAllGroups();
        this.isDeleteProgress = false;
      },
      (error) => {
        this.deleteModal.hide();
        this.errorResponse = error;
        this.errorTitle = 'Delete Group Error';
      }
    );
  }
}
