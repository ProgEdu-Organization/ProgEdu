import { Component, OnInit, ViewChild } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { Router } from '@angular/router';
import { AssignmentManagementService } from './assignment-management.service';

@Component({
  selector: 'app-assignment-management',
  templateUrl: './assignment-management.component.html'
})
export class AssignmentManagementComponent implements OnInit {
  @ViewChild('dangerModal', { static: false }) public deleteModal: ModalDirective;
  assignments: Array<any>;
  assignmentName: string;
  constructor(private assignmentService: AssignmentManagementService, private router: Router) { }

  ngOnInit() {
    this.getAllAssignments();
  }

  getAllAssignments() {
    this.assignmentService.getAllAssignments().subscribe(response => {
      console.log(response);
      this.assignments = response.allAssignments;

    });
  }

  deleteAssignment() {
    this.assignmentService.deleteAssignment(this.assignmentName).subscribe(
      response => {
        console.log(response);
        this.deleteModal.hide();
      },
      error => {
        console.log(error);
      });
  }

  changeToCreatePage() {
    this.router.navigate(['./dashboard/assignmentManagement/create']);
  }

  changeToEditPage() {
    this.router.navigate(['./dashboard/assignmentManagement/edit']);
  }

  setSelectAssignment(data) {
    this.assignmentName = data;
    console.log(data);
  }
}
