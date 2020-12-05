import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { Router } from '@angular/router';
import { FormControl, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { CreateAssignmentService } from './create-assignment.service';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { assignmentTypeEnum } from './assignmentTypeEnum';
import { HttpErrorResponse } from '@angular/common/http';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import { SortablejsOptions } from 'ngx-sortablejs';

@Component({
  selector: 'app-create-assignment',
  templateUrl: './create-assignment.component.html',
  styleUrls: ['./create-assignment.component.scss']
})
export class CreateAssignmentComponent implements OnInit, OnDestroy {
  javaTabStatus: { isOpen: boolean } = { isOpen: false };
  androidTabStatus: { isOpen: boolean } = { isOpen: false };
  webTabStatus: { isOpen: boolean } = { isOpen: false };
  disabled: boolean = false;
  isDropup: boolean = true;
  autoClose: boolean = false;
  assignment: FormGroup;
  SERVER_URL: string = environment.SERVER_URL;

  errorResponse: HttpErrorResponse;
  errorTitle: string;

  max: number = 100;
  showWarning: boolean;
  dynamic: number = 60;
  type: string = 'Waiting';
  finalIndex: number;
  orderString: string = 'Compile Failure'
  isShow: boolean = true;
  isDis: boolean = true;
  isNull: boolean = true;

  score = [0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100];
  statusScore = new Map([["Compile Failure", 0]])

  javaStatus = [
    "Unit Test Failure",
    "Coding Style Failure"
  ];

  webStatus = [
    "Coding Style Failure",
    "HTML Failure",
    "CSS Failure",
    "Javascript Failure",
    "Unit Test Failure"
  ];

  appStatus = [
    "Coding Style Failure",
    "Unit Test Failure",
    "E2E Test Failure"
  ];

  order = [];

  normalOptions: SortablejsOptions = {
    group: 'normal-group',
  };

  public Editor = ClassicEditor;
  public editorConfig = {
    placeholder: 'Write the assignment description in here!',
    ckfinder: {
      // Upload the images to the server using the CKFinder QuickUpload command.
      uploadUrl: environment.SERVER_URL + `/webapi/image`
    }
  };

  constructor(private router: Router, private fb: FormBuilder, private createService: CreateAssignmentService) { }
  @ViewChild('myModal', { static: true }) public progressModal: ModalDirective;
  @ViewChild('errorModal', { static: false }) public errorModal: ModalDirective;

  ngOnInit() {
    const now_time = Date.now() - (new Date().getTimezoneOffset() * 60 * 1000);
    this.assignment = this.fb.group({
      name: [undefined, [Validators.required, Validators.pattern('^[a-zA-Z0-9-_]{3,12}')]],
      releaseTime: [new Date(now_time).toISOString().slice(0, 17) + '00', Validators.required],
      deadline: [new Date(now_time).toISOString().slice(0, 17) + '00', Validators.required],
      description: [undefined],
      type: [undefined, Validators.required],
      file: [undefined, Validators.required],
      assOrder: [undefined],
      rememberMe: [true]
    });
    this.onChanges();
  }

  onChanges(): void {
    const name = 'name';
    const releaseTime = 'releaseTime';
    const deadline = 'deadline';
    const type = 'type';
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
    this.assignment.get(type).valueChanges.subscribe(
      () => {
        this.reset();
      }
    );
  }

  isShowOrder(isShow: boolean) {
    this.isShow = isShow;
  }

  isEnabledConfirm() {
    if(!(this.assignment.get('name').valid && this.assignment.get('type').valid)) {
      return true;
    } else {
      return false;
    }
  }

  checkIsFill() {
    let sum = 0;
    for(let i=0; i<this.statusScore.size; i++) {
      sum += Number(this.statusScore[i]);
    }
    if(sum>100) return false;
    else return true;
  }

  selectChangeHandler(status:string, $event) {
    this.statusScore.set(status, $event.target.value);
    console.log(this.statusScore);
  }

  selectedAssignmentType(type: string) {
    if (type !== undefined) {
      this.assignment.get('type').setValue(assignmentTypeEnum[type]);
    }
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
    this.orderString = "Compile Failure";
    this.orderString = this.orderString + ':' + this.statusScore.get("Compile Failure");
    for(let i = 0; i < this.order.length; i++) {
      this.orderString = this.orderString + ', ' + this.order[i] + ':' + this.statusScore.get(this.order[i]);
    }
    console.log(this.orderString);
    this.assignment.get('assOrder').setValue(this.orderString);
    if (this.assignment.dirty && this.assignment.valid) {
      this.progressModal.show();
      this.createService.createAssignment(this.assignment).subscribe(
        (response) => {
          this.router.navigate(['./dashboard/assignmentManagement']);
        },
        error => {
          this.errorResponse = error;
          this.errorTitle = 'Create Assignment Error';
          this.progressModal.hide();
        });
    } else {
      return;
    }
  }

  public confirm() {
    for (this.finalIndex=1;this.finalIndex<this.order.length+1;this.finalIndex++) {
      this.orderString = this.orderString + ', ' + this.order[this.finalIndex-1];
    }
    this.assignment.get('assOrder').setValue(this.orderString);
    this.createService.modifyOrder(this.assignment).subscribe(
      (response) => {
        console.log("Success");
        window.open(environment.SERVER_URL + 
          '/webapi/assignment/getAssignmentFile?fileName=' + this.assignment.value.name);
      },
      error => {
        this.errorResponse = error;
        this.errorTitle = 'Send Order Error';
      });
      this.orderString = 'Compile Failure';
  }

  public reset() {
    this.order = [];
    this.javaStatus = [
      "Unit Test Failure",
      "Coding Style Failure"
    ];
    this.webStatus = [
      "Coding Style Failure",
      "HTML Failure",
      "CSS Failure",
      "Javascript Failure",
      "Unit Test Failure"
    ];
    this.appStatus = [
      "Coding Style Failure",
      "Unit Test Failure",
      "E2E Test Failure"
    ];
  }


  ngOnDestroy() {
    this.javaTabStatus.isOpen = false;
    this.webTabStatus.isOpen = false;
    this.androidTabStatus.isOpen = false;
  }
}
