import { Component, OnInit, ViewChild, SystemJsNgModuleLoader } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { Router } from '@angular/router';
import { AssignmentManagementService } from './assignment-management.service';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-assignment-management',
  templateUrl: './assignment-management.component.html'
})
export class AssignmentManagementComponent implements OnInit {
  @ViewChild('editModal', { static: true }) public editModal: ModalDirective;
  @ViewChild('deleteModal', { static: true }) public deleteModal: ModalDirective;
  assignments: Array<any>;
  assignmentName: string;
  assignmentForm: FormGroup;

  max: number = 100;
  showWarning: boolean;
  dynamic: number = 0;
  type: string = 'Waiting';
  isDeleteProgress = false;

  constructor(private assignmentService: AssignmentManagementService, private router: Router, private fb: FormBuilder) { }

  ngOnInit() {
    this.getAllAssignments();
    const now_time = Date.now() - (new Date().getTimezoneOffset() * 60 * 1000);
    this.assignmentForm = this.fb.group({
      name: [''],
      releaseTime: [, Validators.required],
      deadline: [, Validators.required],
      description: ['', Validators.required],
      file: [],
      rememberMe: [true]
    });

    this.onChange();
  }

  onChange() {
    const releaseTime = 'releaseTime';
    const deadline = 'deadline';
    const description = 'description';

    this.assignmentForm.get(releaseTime).valueChanges.subscribe(
      val => {
        val.length !== 0 ? this.showIsValidById(releaseTime) : this.hideIsInvalidById(releaseTime);
      }
    );
    this.assignmentForm.get(deadline).valueChanges.subscribe(
      val => {
        val.length !== 0 ? this.showIsValidById(deadline) : this.hideIsInvalidById(deadline);
      }
    );

    this.assignmentForm.get(description).valueChanges.subscribe(
      val => {
        val.length !== 0 ? this.showIsValidById(description) : this.hideIsInvalidById(description);
      }
    );
  }

  updateTestCase($event) {
    if ($event.target.files) {
      this.assignmentForm.get('file').setValue($event.target.files[0]);
    }
  }

  getAllAssignments() {
    this.assignmentService.getAllAssignments().subscribe(response => {
      this.assignments = response.allAssignments;

    });
  }

  getUTCAdjustTime(time: any): Date {
    const timeOffset = (new Date().getTimezoneOffset() * 60 * 1000);
    const assigenmentTime = new Date(time).getTime();

    return new Date(assigenmentTime - timeOffset);
  }

  deleteAssignment() {
    this.isDeleteProgress = true;

    this.assignmentService.deleteAssignment(this.assignmentName).subscribe(
      response => {
        this.deleteModal.hide();
        this.getAllAssignments();
        this.isDeleteProgress = false;
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

  showIsValidById(id: string) {
    $('#' + id).addClass('is-valid');
    $('#' + id).removeClass('is-invalid');
  }

  hideIsInvalidById(id: string) {
    $('#' + id).removeClass('is-valid');
    $('#' + id).addClass('is-invalid');
  }

  setSelectAssignment(assignment: any) {
    if (assignment) {
      this.assignmentName = assignment.name;
      this.assignmentForm.get('description').setValue(assignment.description);
      this.assignmentForm.get('releaseTime').setValue(this.getUTCAdjustTime(assignment.releaseTime).toISOString().slice(0, 19));
      this.assignmentForm.get('deadline').setValue(this.getUTCAdjustTime(assignment.deadline).toISOString().slice(0, 19));
    }
  }

  editAssignment() {

    if (this.assignmentForm.valid) {
      this.assignmentForm.get('name').setValue(this.assignmentName);
      this.assignmentService.editAssignment(this.assignmentForm).subscribe(
        response => {
          this.editModal.hide();
          this.getAllAssignments();
        },
        errpr => {

        });
    }
  }
}
