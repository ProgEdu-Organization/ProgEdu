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
  @ViewChild('dangerModal', { static: false }) public deleteModal: ModalDirective;
  @ViewChild('bsModal', { static: false }) public editModal: ModalDirective;
  assignments: Array<any>;
  assignmentName: string;
  assignmentForm: FormGroup;

  constructor(private assignmentService: AssignmentManagementService, private router: Router, private fb: FormBuilder) { }

  ngOnInit() {
    this.getAllAssignments();

    this.assignmentForm = this.fb.group({
      name: [''],
      releaseTime: [new Date().toISOString().slice(0, 19), Validators.required],
      deadline: [new Date().toISOString().slice(0, 19), Validators.required],
      readMe: ['', Validators.required],
      file: [],
      rememberMe: [true]
    });

    this.onChange();
  }

  onChange() {
    const releaseTime = 'releaseTime';
    const deadline = 'deadline';
    const readMe = 'readMe';

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

    this.assignmentForm.get(readMe).valueChanges.subscribe(
      val => {
        val.length !== 0 ? this.showIsValidById(readMe) : this.hideIsInvalidById(readMe);
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

  showIsValidById(id: string) {
    $('#' + id).addClass('is-valid');
    $('#' + id).removeClass('is-invalid');
  }

  hideIsInvalidById(id: string) {
    $('#' + id).removeClass('is-valid');
    $('#' + id).addClass('is-invalid');
  }

  setSelectAssignment(data) {
    this.assignmentName = data;
    console.log(data);
  }

  editAssignment() {

    if (this.assignmentForm.dirty && this.assignmentForm.valid) {
      this.assignmentForm.get('name').setValue(this.assignmentName);
      this.assignmentService.editAssignment(this.assignmentForm).subscribe(
        response => {
          console.log(response);
        },
        errpr => {

        });
    }
  }
}
