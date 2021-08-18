import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { Router } from '@angular/router';
import { FormControl, FormGroup, FormArray, FormBuilder, Validators } from '@angular/forms';
import { CreateAssignmentService } from './create-assignment.service';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { assignmentTypeEnum } from './assignmentTypeEnum';
import { HttpErrorResponse } from '@angular/common/http';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import { SortablejsOptions } from 'ngx-sortablejs';
import { assignmentGradingEnum } from './assignmentGradingEnum.enum';
import { Category, Assessment } from './../review-metrics-management/Category';
import { stringify } from '@angular/compiler/src/util';

@Component({
  selector: 'app-create-assignment',
  templateUrl: './create-assignment.component.html',
  styleUrls: ['./create-assignment.component.scss']
})
export class CreateAssignmentComponent implements OnInit, OnDestroy {
  javaTabStatus: { isOpen: boolean } = { isOpen: false };
  androidTabStatus: { isOpen: boolean } = { isOpen: false };
  webTabStatus: { isOpen: boolean } = { isOpen: false };
  autoAssignmentStatus: { isOpen: boolean } = { isOpen: true };
  peerReviewStatus: { isOpen: boolean } = { isOpen: false };
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
  orderString: string;
  showAssessment: boolean = true;

  reviewMetricsNums = [0, 1, 2];
  reviewRoundTime = [{startTime: '', endTime:'', reviewStartTime:'', reviewEndTime:''}];
  assessments: Assessment[][] = [[], [], []];
  categories: Category[];
  onSelectedCategory: number[] = [0, 1, 2];
  onSelectedMetrics: number[] = [0, 0, 0];

  statusScore = new Map([["Compile Failure", "0"]])

  javaStatus = [
    "Unit Test Failure",
    "Coding Style Failure"
  ];

  webStatus = [
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
      uploadUrl: environment.SERVER_URL + `/webapi/image` // Todo 這即將捨棄
    }
  };

  constructor(private router: Router, private fb: FormBuilder, private createService: CreateAssignmentService) { }
  @ViewChild('myModal', { static: true }) public progressModal: ModalDirective;
  @ViewChild('errorModal', { static: false }) public errorModal: ModalDirective;

  ngOnInit() {
    const now_time = Date.now() - (new Date().getTimezoneOffset() * 60 * 1000);
    this.assignment = this.fb.group({
      name: [undefined, [Validators.required, Validators.pattern('^[a-zA-Z0-9-_]{3,10}')]],
      releaseTime: [new Date(now_time).toISOString().slice(0, 17) + '00', Validators.required],
      deadline: [new Date(now_time).toISOString().slice(0, 17) + '00', Validators.required],
      reviewReleaseTime: [new Date(now_time).toISOString().slice(0, 17) + '00', Validators.required],
      reviewDeadline: [new Date(now_time).toISOString().slice(0, 17) + '00', Validators.required],
      reviewTime: this.fb.array(this.reviewRoundTime.map(round => this.fb.group({
        startTime: [new Date(now_time).toISOString().slice(0, 17) + '00', Validators.required],
        endTime: [new Date(now_time).toISOString().slice(0, 17) + '00', Validators.required],
        reviewStartTime: [new Date(now_time).toISOString().slice(0, 17) + '00', Validators.required],
        reviewEndTime: [new Date(now_time).toISOString().slice(0, 17) + '00', Validators.required]
      }))),
      commitRecordCount: [0, [Validators.pattern('^[0-9]{1,3}'), Validators.required]],
      description: [undefined, Validators.required],
      type: [undefined, Validators.required],
      file: [undefined, Validators.required],
      method: [assignmentGradingEnum['Auto']],
      assOrder: [undefined],
      rememberMe: [true]
    });
    this.onChanges();
    this.createService.getAllCategory().subscribe(
      response => {
        this.categories = response['allCategory'];
        this.getAllMetrics();
      }
    );
  }

  setCategory(selectedReviews: number, selectedCategory: number): void {
    this.onSelectedCategory[selectedReviews] = selectedCategory;
  }
  setMetrics(selectedReviews: number, selectedMetrics: number): void {
    this.onSelectedMetrics[selectedReviews] = selectedMetrics;
  }
  onChanges(): void {
    const name = 'name';
    const releaseTime = 'releaseTime';
    const deadline = 'deadline';
    const description = 'description';
    const reviewReleaseTime = 'reviewReleaseTime';
    const reviewDeadline = 'reviewDeadline';
    const commitRecordCount = 'commitRecordCount';
    const type = 'type';
    this.assignment.get(name).valueChanges.subscribe(
      () => {
        this.assignment.get(name).valid ? this.showIsValidById(name) : this.showIsInvalidById(name);
      }
    );

    this.assignment.get(releaseTime).valueChanges.subscribe(
      val => {
        val.length !== 0 ? this.showIsValidById(releaseTime) : this.showIsInvalidById(releaseTime);
        this.assignmentTimeCheck();
      }
    );
    this.assignment.get(deadline).valueChanges.subscribe(
      val => {
        val.length !== 0 ? this.showIsValidById(deadline) : this.showIsInvalidById(deadline);
        this.assignmentTimeCheck();
      }
    );
    this.assignment.get(type).valueChanges.subscribe(
      () => {
        this.reset();
        if (this.assignment.get(type).value == 'javac') {
          this.setShowAssessment(false);
        } else {
          this.setShowAssessment(true);
        }
      }
    );

    this.assignment.get(description).valueChanges.subscribe(
      val => {
        val.length !== 0 ? this.showIsValidById(description) : this.showIsInvalidById(description);
      }
    );
    // Peer Review Options
    this.assignment.get(reviewReleaseTime).valueChanges.subscribe(
      val => {
        val.length !== 0 ? this.showIsValidById(reviewReleaseTime) : this.showIsInvalidById(reviewReleaseTime);
        this.assignmentTimeCheck();
      }
    );
    this.assignment.get(reviewDeadline).valueChanges.subscribe(
      val => {
        val.length !== 0 ? this.showIsValidById(reviewDeadline) : this.showIsInvalidById(reviewDeadline);
        this.assignmentTimeCheck();
      }
    );
    this.assignment.get(commitRecordCount).valueChanges.subscribe(
      val => {
        val.length !== 0 ? this.showIsValidById(commitRecordCount) : this.showIsInvalidById(commitRecordCount);
      }
    );
    for(let i = 0; i < this.roundTimeArray.length; i++) {
      let startTime_id = i.toString() + '_startTime';
      let endTime_id = i.toString() + '_endTime';
      let reviewStartTime_id = i.toString() + '_reviewStartTime';
      let reviewEndTime_id = i.toString() + '_reviewEndTime';
      this.roundTimeArray[i].get('startTime').valueChanges.subscribe(
        val => {
          val.length !== 0 ? this.showIsValidById(startTime_id) : this.showIsInvalidById(startTime_id);
          this.reviewTimeCheck();
        }
      )
      this.roundTimeArray[i].get('endTime').valueChanges.subscribe(
        val => {
          val.length !== 0 ? this.showIsValidById(endTime_id) : this.showIsInvalidById(endTime_id);
          this.reviewTimeCheck();
        }
      )
      this.roundTimeArray[i].get('reviewStartTime').valueChanges.subscribe(
        val => {
          val.length !== 0 ? this.showIsValidById(reviewStartTime_id) : this.showIsInvalidById(reviewStartTime_id);
          this.reviewTimeCheck();
        }
      )
      this.roundTimeArray[i].get('reviewEndTime').valueChanges.subscribe(
        val => {
          val.length !== 0 ? this.showIsValidById(reviewEndTime_id) : this.showIsInvalidById(reviewEndTime_id);
          this.reviewTimeCheck();
        }
      )
    }
    console.log(this.assignment.get('reviewTime').value);
  }

  get roundTimeArray() {
    return (<FormArray>this.assignment.get('reviewTime')).controls;
  }

  setShowAssessment(showAssessment: boolean) {
    this.showAssessment = showAssessment;
  }

  getScoreOptions(status:string) {
    let max = 100;
    let sum = 0;
    let options : string[] = [];
    sum += Number(this.statusScore.get("Compile Failure"));
    if (this.order.length !== 0) {
      for (let i = 0; i < this.order.length; i++) {
        if (this.statusScore.get(this.order[i]) !== undefined) {
          sum += Number(this.statusScore.get(this.order[i]));
        }
      }
    }
    if (this.statusScore.get(status) !== undefined) {
      sum -= Number(this.statusScore.get(status));
    }
    max = max - sum;
    for (let i = 0; i <= 100; i++) {
      if (i <= max) {
        options.push(String(i));
      }
    }
    return options;
  }

  selectChangeHandler(status:string, $event) {
    this.statusScore.set(status, $event.target.value);
  }

  selectedAssignmentGradingMethod(method: string) {
    if (method !== undefined) {
      this.assignment.get('method').setValue(assignmentGradingEnum[method]);
      if (assignmentGradingEnum[method] === assignmentGradingEnum.Auto) {
        this.autoAssignmentStatus.isOpen = true;
        this.peerReviewStatus.isOpen = false;
        const now_time = Date.now() - (new Date().getTimezoneOffset() * 60 * 1000);
        // ReSet Peer Review Form Control
        this.assignment.patchValue({ commitRecordCount: 0 });
        this.assignment.patchValue({ reviewReleaseTime: new Date(now_time).toISOString().slice(0, 17) + '00' });
        this.assignment.patchValue({ reviewDeadline: new Date(now_time).toISOString().slice(0, 17) + '00' });
      } else {
        this.autoAssignmentStatus.isOpen = false;
        this.peerReviewStatus.isOpen = true;
      }
    }
  }

  addRoundNums() {
    const now_time = Date.now() - (new Date().getTimezoneOffset() * 60 * 1000);
    (<FormArray>this.assignment.get('reviewTime')).push(
      this.fb.group({
        startTime: new Date(now_time).toISOString().slice(0, 17) + '00', 
        endTime: new Date(now_time).toISOString().slice(0, 17) + '00',
        reviewStartTime: new Date(now_time).toISOString().slice(0, 17) + '00',
        reviewEndTime: new Date(now_time).toISOString().slice(0, 17) + '00'
      })
    );
    console.log(this.assignment.get('reviewTime').value);
  }

  removeRound(index: number) {
    (<FormArray>this.assignment.get('reviewTime')).removeAt(index);
    console.log(this.assignment.get('reviewTime').value);
  }

  addReviewMetrics() {
    this.reviewMetricsNums = Array(this.reviewMetricsNums.length + 1).fill(0).map((x, i) => i);
  }

  removeReviewMetrics(index: number) {
    for (index; index < this.reviewMetricsNums.length - 1; index++) {
      this.onSelectedCategory[index] = this.onSelectedCategory[index + 1];
      this.onSelectedMetrics[index] = this.onSelectedMetrics[index + 1];
    }
    this.onSelectedCategory.splice(this.reviewMetricsNums.length - 1, 1);
    this.onSelectedMetrics.splice(this.reviewMetricsNums.length - 1, 1);
    this.reviewMetricsNums.splice(this.reviewMetricsNums.length - 1, 1);
    this.reviewMetricsNums = Array(this.reviewMetricsNums.length).fill(0).map((x, i) => i);
  }

  getAllMetrics() {
    for (const category of this.categories) {
      this.createService.getMetrics(category).subscribe(
        response => {
          category.allMetrics = response['allMetrics'];
        }
      );
    }
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

  showIsInvalidById(id: string) {
    $('#' + id).removeClass('is-valid');
    $('#' + id).addClass('is-invalid');
  }

  changeToAssignmentPage() {
    this.router.navigate(['./dashboard/assignmentManagement']);
  }

  fileListener($event) {
    this.assignment.controls['file'].setValue($event.target.files[0]);
  }

  assignmentTimeCheck() {
    const releaseTime = 'releaseTime';
    const deadline = 'deadline';
    const reviewReleaseTime = 'reviewReleaseTime';
    const reviewDeadline = 'reviewDeadline';
    // deadline should be after release time
    if (Date.parse(this.assignment.get(deadline).value) < Date.parse(this.assignment.get(releaseTime).value)) {
      this.showIsInvalidById(deadline);
    }
    // review start time should be after deadline
    if (Date.parse(this.assignment.get(reviewReleaseTime).value) < Date.parse(this.assignment.get(deadline).value)) {
      this.showIsInvalidById(reviewReleaseTime);
    }
    // review end time should be after review start time
    if (Date.parse(this.assignment.get(reviewDeadline).value) < Date.parse(this.assignment.get(reviewReleaseTime).value)) {
      this.showIsInvalidById(reviewDeadline);
    }
  }

  reviewTimeCheck() {
    for(let i = 0; i < this.roundTimeArray.length; i++) {
      if (i != 0 && Date.parse(this.roundTimeArray[i].get('startTime').value) < Date.parse(this.roundTimeArray[i - 1].get('reviewEndTime').value)) {
        this.showIsInvalidById(i.toString() + '_startTime');
      }
      if (Date.parse(this.roundTimeArray[i].get('endTime').value) < Date.parse(this.roundTimeArray[i].get('startTime').value)) {
        this.showIsInvalidById(i.toString() + '_endTime');
      }
      if (Date.parse(this.roundTimeArray[i].get('reviewStartTime').value) < Date.parse(this.roundTimeArray[i].get('endTime').value)) {
        this.showIsInvalidById(i.toString() + '_reviewStartTime');
      }
      if (Date.parse(this.roundTimeArray[i].get('reviewEndTime').value) < Date.parse(this.roundTimeArray[i].get('reviewStartTime').value)) {
        this.showIsInvalidById(i.toString() + '_reviewEndTime');
      }
    }
  }

  public submit() {
    this.orderString = "";
    if (this.assignment.get('type').value == 'maven') {
      this.orderString = "Compile Failure:" + this.statusScore.get("Compile Failure");
    }
    for (let i = 0; i < this.order.length; i++) {
      if (this.statusScore.get(this.order[i]) == undefined) {
        this.orderString = this.orderString + ', ' + this.order[i] + ':0';
      } else {
        this.orderString = this.orderString + ', ' + this.order[i] + ':' + this.statusScore.get(this.order[i]);
      }
    }
    this.assignment.get('assOrder').setValue(this.orderString);
    if (this.assignment.dirty && this.assignment.valid) {
      this.progressModal.show();
      if (!this.peerReviewStatus.isOpen) {
        if (this.assignment.get('type').value == 'maven') {
          this.createService.createAssignmentWithOrder(this.assignment).subscribe(
            (response) => {
              this.router.navigate(['./dashboard/assignmentManagement']);
            },
            error => {
              this.errorResponse = error;
              this.errorTitle = 'Create Assignment Error';
              this.progressModal.hide();
            });
        } else {
          this.createService.createAssignment(this.assignment).subscribe(
            (response) => {
              this.router.navigate(['./dashboard/assignmentManagement']);
            },
            error => {
              this.errorResponse = error;
              this.errorTitle = 'Create Assignment Error';
              this.progressModal.hide();
            });
        }
      } else {
        const selectedMetrics = [];
        for (let i = 0; i < this.reviewMetricsNums.length; i++) {
          selectedMetrics[i] = this.categories[this.onSelectedCategory[i]].allMetrics[this.onSelectedMetrics[i]].id;
        }
        this.createService.createPeerReviewAssignment(this.assignment, selectedMetrics).subscribe(
          (response) => {
            this.router.navigate(['./dashboard/assignmentManagement']);
          },
          error => {
            this.errorResponse = error;
            this.errorTitle = 'Create Assignment Error';
            this.progressModal.hide();
          });
      }
    } else {
      return;
    }
  }

  public confirm() {
    if (this.assignment.get('type').value == 'maven') {
      this.orderString = 'Compile Failure';
    }
    for (let i = 0; i < this.order.length; i++) {
      this.orderString = this.orderString + ', ' + this.order[i];
    }
    this.assignment.get('assOrder').setValue(this.orderString);
    if (this.assignment.get('name').invalid) {
      window.open(environment.SERVER_URL + '/resources/MvnQuickStart.zip');
    } else {
      this.createService.modifyOrder(this.assignment).subscribe(
        (response) => {
          console.log("Success");
          window.open(this.createService.getAssignmentFile(this.assignment.value.name));
        },
        error => {
          this.errorResponse = error;
          this.errorTitle = 'Send Order Error';
        });
        this.orderString = '';
    }
  }

  public reset() {
    this.order = [];
    this.javaStatus = [
      "Unit Test Failure",
      "Coding Style Failure"
    ];
    this.webStatus = [
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
