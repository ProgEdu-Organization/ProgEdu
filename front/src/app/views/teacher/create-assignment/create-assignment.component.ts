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

@Component({
  selector: 'app-create-assignment',
  templateUrl: './create-assignment.component.html',
  styleUrls: ['./create-assignment.component.scss']
})
export class CreateAssignmentComponent implements OnInit, OnDestroy {
  javaTabStatus: { isOpen: boolean } = { isOpen: false };
  androidTabStatus: { isOpen: boolean } = { isOpen: false };
  webTabStatus: { isOpen: boolean } = { isOpen: false };
  pythonTabStatus: { isOpen: boolean } = { isOpen: false };
  autoAssignmentStatus: { isOpen: boolean } = { isOpen: true };
  peerReviewStatus: { isOpen: boolean } = { isOpen: false };
  disabled: boolean = false;
  isDropup: boolean = true;
  autoClose: boolean = false;
  assignment: FormGroup;

  NEW_SERVER_URL: string = environment.NEW_SERVER_URL;

  errorResponse: HttpErrorResponse;
  errorTitle: string;

  max: number = 100;
  showWarning: boolean;
  dynamic: number = 60;
  type: string = 'Waiting';
  finalIndex: number;
  showAssessment: boolean = true;

  reviewMetricsNums = [0, 1, 2];
  reviewRoundTime = [{startTime: '', endTime:'', reviewStartTime:'', reviewEndTime:''}];
  assessments: Assessment[][] = [[], [], []];
  categories: Category[];
  onSelectedCategory: number[] = [0, 1, 2];
  onSelectedMetrics: number[] = [0, 0, 0];

  javaStatusScore = new Map([["Compile Failure", "0"]]);
  webStatusScore = new Map();
  appStatusScore = new Map([["Compile Failure", "0"]]);
  pyStatusScore = new Map([["Compile Failure", "0"]]);

  javaStatus = [
    "Unit Test Failure",
    "Coding Style Failure"
  ];

  webStatus = [
    "HTML Failure",
    "CSS Failure",
    "JavaScript Failure",
    "Unit Test Failure"
  ];

  appStatus = [
    "Coding Style Failure",
    "Unit Test Failure",
    "UI Test Failure"
  ];

  pyStatus = [
    "Coding Style Failure",
    "Unit Test Failure"
  ]

  javaOrder = [];
  webOrder = [];
  appOrder = [];
  pyOrder = [];

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
    const reviewTime = 'reviewTime';
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
    this.assignment.get(commitRecordCount).valueChanges.subscribe(
      val => {
        val.length !== 0 ? this.showIsValidById(commitRecordCount) : this.showIsInvalidById(commitRecordCount);
      }
    );
    this.assignment.get(reviewTime).valueChanges.subscribe(
      val => {
        val.length !== 0? this.reviewTimeCheck():this.reviewTimeInValid();
      }
    );
  }

  get roundTimeArray() {
    return (<FormArray>this.assignment.get('reviewTime')).controls;
  }

  setShowAssessment(showAssessment: boolean) {
    this.showAssessment = showAssessment;
  }

  getScoreOptions(order: string[], statusScore: Map<string, string>, status: string) {
    let max = 100;
    let sum = 0;
    let options : string[] = [];
    let tempOrder = order;
    let tempStatusScore = statusScore;

    if (statusScore == this.javaStatusScore || statusScore == this.appStatusScore 
      || statusScore == this.pyStatusScore) {
      sum += Number(statusScore.get('Compile Failure'));
    }

    if (tempOrder.length !== 0) {
      for (let i = 0; i < tempOrder.length; i++) {
        if (tempStatusScore.get(tempOrder[i]) !== undefined) {
          sum += Number(tempStatusScore.get(tempOrder[i]));
        }
      }
    }
    if (tempStatusScore.get(status) !== undefined) {
      sum -= Number(tempStatusScore.get(status));
    }
    max = max - sum;
    for (let i = 0; i <= 100; i++) {
      if (i <= max) {
        options.push(String(i));
      }
    }
    return options;
  }

  selectChangeHandler(type:string, status:string, $event) {
    if (type == 'maven') {
      this.javaStatusScore.set(status, $event.target.value);
      console.log(this.javaStatusScore);
    } else if (type == 'web') {
      this.webStatusScore.set(status, $event.target.value);
      console.log(this.webStatusScore);
    } else if (type == 'android') {
      this.appStatusScore.set(status, $event.target.value);
      console.log(this.appStatusScore);
    } else if (type == 'python') {
      this.pyStatusScore.set(status, $event.target.value);
    }
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
        while(this.roundTimeArray.length > 1) {
          this.roundTimeArray.pop();
        }
        this.assignment.patchValue({
          reviewTime:[{
            startTime: new Date(now_time).toISOString().slice(0, 17) + '00',
            endTime: new Date(now_time).toISOString().slice(0, 17) + '00',
            reviewStartTime: new Date(now_time).toISOString().slice(0, 17) + '00',
            reviewEndTime: new Date(now_time).toISOString().slice(0, 17) + '00',
          }]
        });
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
    this.reviewTimeCheck();
  }

  removeRound(index: number) {
    (<FormArray>this.assignment.get('reviewTime')).removeAt(index);
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

    // deadline should be after release time
    if (Date.parse(this.assignment.get(deadline).value) < Date.parse(this.assignment.get(releaseTime).value)) {
      this.showIsInvalidById(deadline);
    }
  }

  reviewTimeCheck() {
    for(let round = 0; round < this.roundTimeArray.length; round++) {
      this.showIsValidById(round.toString() + '_startTime');
      this.showIsValidById(round.toString() + '_endTime');
      this.showIsValidById(round.toString() + '_reviewStartTime');
      this.showIsValidById(round.toString() + '_reviewEndTime');

      // auto assessment release time should be after previous review end time
      if (round > 0 && Date.parse(this.roundTimeArray[round].get('startTime').value) < Date.parse(this.roundTimeArray[round - 1].get('reviewEndTime').value)) {
        this.showIsInvalidById(round.toString() + '_startTime');
      }
      // auto assessment deadline should be after auto assessment release time
      if (Date.parse(this.roundTimeArray[round].get('endTime').value) < Date.parse(this.roundTimeArray[round].get('startTime').value)) {
        this.showIsInvalidById(round.toString() + '_endTime');
      }
      // review start time should be after deadline
      if (Date.parse(this.roundTimeArray[round].get('reviewStartTime').value) < Date.parse(this.roundTimeArray[round].get('endTime').value)) {
        this.showIsInvalidById(round.toString() + '_reviewStartTime');
      }
      // review end time should be after review start time
      if (Date.parse(this.roundTimeArray[round].get('reviewEndTime').value) < Date.parse(this.roundTimeArray[round].get('reviewStartTime').value)) {
        this.showIsInvalidById(round.toString() + '_reviewEndTime');
      }
    }
  }

  reviewTimeInValid() {
    for(let round = 0; round < this.roundTimeArray.length; round++) {
      this.showIsInvalidById(round.toString() + '_startTime');
      this.showIsInvalidById(round.toString() + '_endTime');
      this.showIsInvalidById(round.toString() + '_reviewStartTime');
      this.showIsInvalidById(round.toString() + '_reviewEndTime');
    }
  }

  public submit() {
    let tempOrder: string[] = [];
    let tempStatusScore: Map<string, string>;
    let orderString = "";
    if (this.assignment.get('type').value == 'maven') {
      tempOrder = this.javaOrder;
      tempStatusScore = this.javaStatusScore;
      orderString = "Compile Failure:" + this.javaStatusScore.get("Compile Failure") + ", ";
    } else if (this.assignment.get('type').value == 'web') {
      tempOrder = this.webOrder;
      tempStatusScore = this.webStatusScore;
    } else if (this.assignment.get('type').value == 'android') {
      tempOrder = this.appOrder;
      tempStatusScore = this.appStatusScore;
      orderString = "Compile Failure:" + this.appStatusScore.get("Compile Failure") + ", ";
    } else if (this.assignment.get('type').value == 'python') {
      tempOrder = this.pyOrder;
      tempStatusScore = this.pyStatusScore;
      orderString = "Compile Failure:" + this.pyStatusScore.get("Compile Failure") + ", ";
    }
    for (let i = 0; i < tempOrder.length; i++) {
      if (tempStatusScore.get(tempOrder[i]) == undefined) {
        orderString = orderString + tempOrder[i] + ':0';
      } else {
        orderString = orderString + tempOrder[i] + ':' + tempStatusScore.get(tempOrder[i]);
      }
      if (i < tempOrder.length - 1) {
        orderString = orderString + ', ';
      }
    }
    this.assignment.get('assOrder').setValue(orderString);

    if (this.assignment.dirty && this.assignment.valid) {
      this.progressModal.show();
      if (!this.peerReviewStatus.isOpen) {
        if (this.assignment.get('type').value == 'maven' || this.assignment.get('type').value == 'web'
            || this.assignment.get('type').value == 'android' || this.assignment.get('type').value == 'python') {
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
          this.assignment.get('assOrder').setValue('');
          this.createService.createAssignmentWithOrder(this.assignment).subscribe(
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

  public confirm(order: string[]) {
    let tempOrder = order;
    let orderString: string = "";
    if (this.assignment.get('type').value == 'maven') {
      orderString = 'Compile Failure' + ', ';
    }
    for (let i = 0; i < tempOrder.length; i++) {
      orderString = orderString + tempOrder[i];
      if (i < tempOrder.length - 1) {
        orderString = orderString + ', '
      }
    }
    this.assignment.get('assOrder').setValue(orderString);
    if (this.assignment.get('name').invalid) {
      window.open(environment.NEW_SERVER_URL + '/assignment/getMvnAssignmentFile');
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
    }
  }

  ngOnDestroy() {
    this.javaTabStatus.isOpen = false;
    this.webTabStatus.isOpen = false;
    this.androidTabStatus.isOpen = false;
    this.pythonTabStatus.isOpen = false;
  }


}
