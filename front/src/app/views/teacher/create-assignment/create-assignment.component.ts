import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { Router } from '@angular/router';
import { FormControl, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { CreateAssignmentService } from './create-assignment.service';
import { ModalDirective } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-create-assignment',
  templateUrl: './create-assignment.component.html'
})
export class CreateAssignmentComponent implements OnInit, OnDestroy {
  javaTabStatus: { isOpen: boolean } = { isOpen: false };
  androidTabStatus: { isOpen: boolean } = { isOpen: false };
  webTabStatus: { isOpen: boolean } = { isOpen: false };
  disabled: boolean = false;
  isDropup: boolean = true;
  autoClose: boolean = false;
  assignment: FormGroup;
  items: string[] = [
    'The first choice!',
    'And another choice for you.',
    'but wait! A third!'
  ];

  errorMsg: string;
  SERVER_URL: string = environment.SERVER_URL;

  max: number = 100;
  showWarning: boolean;
  dynamic: number = 60;
  type: string = 'Waiting';

  stacked: any[] = [];

  timer: any = null;
  buttonCaption: string = 'Start';
  constructor(private router: Router, private fb: FormBuilder, private createService: CreateAssignmentService) { }
  @ViewChild('myModal', { static: true }) public progressModal: ModalDirective;
  @ViewChild('dangerModal', { static: false }) public errorModal: ModalDirective;


  ngOnInit() {
    this.assignment = this.fb.group({
      name: [undefined, [Validators.required, Validators.maxLength(10)]],
      releaseTime: [undefined, Validators.required],
      deadline: [undefined, Validators.required],
      readMe: [undefined, Validators.required],
      type: [undefined, Validators.required],
      file: [undefined, Validators.required],
    });
    this.onChanges();
  }

  onChanges(): void {
    const name = 'name';
    const releaseTime = 'releaseTime';
    const deadline = 'deadline';
    const readMe = 'readMe';
    this.assignment.get(name).valueChanges.subscribe(
      () => {
        this.assignment.get(name).valid ? this.showIsValidById(name) : this.hideIsInvalidById(name);
      }
    );

    this.assignment.get(releaseTime).valueChanges.subscribe(
      val => {
        val.length !== 0 ? this.showIsValidById(releaseTime) : this.hideIsInvalidById(releaseTime);
      }
    );
    this.assignment.get(deadline).valueChanges.subscribe(
      val => {
        val.length !== 0 ? this.showIsValidById(deadline) : this.hideIsInvalidById(deadline);
      }
    );

    this.assignment.get(readMe).valueChanges.subscribe(
      val => {
        val.length !== 0 ? this.showIsValidById(readMe) : this.hideIsInvalidById(readMe);
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

  changeToAssignmentPage() {
    this.router.navigate(['./dashboard/assignmentManagement']);
  }

  fileListener($event) {
    this.assignment.controls['file'].setValue($event.target.files[0]);
  }


  public submit() {
    console.log(this.assignment.value);
    if (this.assignment.dirty && this.assignment.valid) {
      this.progressModal.show();
      this.createService.createAssignment(this.assignment).subscribe(
        (response) => {
          this.router.navigate(['./dashboard/assignmentManagement']);
        },
        error => {
          this.errorMsg = error.message;
          this.progressModal.hide();
          this.errorModal.show();
          console.log(error);
        });
    } else {
      return;
    }
  }


  ngOnDestroy() {
    this.javaTabStatus.isOpen = false;
    this.webTabStatus.isOpen = false;
    this.androidTabStatus.isOpen = false;
  }
}
