import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { Router } from '@angular/router';
import { FormControl, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { CreateAssignmentService } from './create-assignment.service';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { assignmentTypeEnum } from './assignmentTypeEnum';
import { HttpErrorResponse } from '@angular/common/http';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import { assignmentGradingEnum } from './assignmentGradingEnum.enum';
import { Category, Assessment } from './../review-metrics-management/Category';

@Component({
  selector: 'app-create-assignment',
  templateUrl: './create-assignment.component.html'
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

  reviewMetricsNums = [0, 1, 2];
  assessments: Assessment[][] = [[], [], []];
  categories: Category[];
  onSelectedCategory: number[] = [0, 1, 2];
  onSelectedMetrics: number[] = [0, 0, 0];

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
      name: [undefined, [Validators.required, Validators.pattern('^[a-zA-Z0-9-_]{3,10}')]],
      releaseTime: [new Date(now_time).toISOString().slice(0, 17) + '00', Validators.required],
      deadline: [new Date(now_time).toISOString().slice(0, 17) + '00', Validators.required],
      reviewReleaseTime: [new Date(now_time).toISOString().slice(0, 17) + '00', Validators.required],
      reviewDeadline: [new Date(now_time).toISOString().slice(0, 17) + '00', Validators.required],
      commitRecordCount: [0, [Validators.pattern('^[0-9]{1,3}'), Validators.required]],
      description: [undefined, Validators.required],
      type: [undefined, Validators.required],
      file: [undefined, Validators.required],
      method: [assignmentGradingEnum['Auto']],
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

    this.assignment.get(description).valueChanges.subscribe(
      val => {
        val.length !== 0 ? this.showIsValidById(description) : this.hideIsInvalidById(description);
      }
    );
    // Peer Review Options
    this.assignment.get(reviewReleaseTime).valueChanges.subscribe(
      val => {
        val.length !== 0 ? this.showIsValidById(reviewReleaseTime) : this.hideIsInvalidById(reviewReleaseTime);
        if (Date.parse(val) < Date.parse(this.assignment.get(deadline).value)) {
          this.hideIsInvalidById(reviewReleaseTime);
        }
      }
    );
    this.assignment.get(reviewDeadline).valueChanges.subscribe(
      val => {
        val.length !== 0 ? this.showIsValidById(reviewDeadline) : this.hideIsInvalidById(reviewDeadline);
      }
    );
    this.assignment.get(commitRecordCount).valueChanges.subscribe(
      val => {
        val.length !== 0 ? this.showIsValidById(commitRecordCount) : this.hideIsInvalidById(commitRecordCount);
      }
    );
  }

  selectedAssignmentGradingMethod(method: string) {
    if (method !== undefined) {
      this.assignment.get('method').setValue(assignmentGradingEnum[method]);
      if (assignmentGradingEnum[method] === assignmentGradingEnum.Auto) {
        this.autoAssignmentStatus.isOpen = true;
        this.peerReviewStatus.isOpen = false;
        const now_time = Date.now() - (new Date().getTimezoneOffset() * 60 * 1000);
        // ReSet Peer Review Form Control
        this.assignment.patchValue({commitRecordCount: 0});
        this.assignment.patchValue({reviewReleaseTime: new Date(now_time).toISOString().slice(0, 17) + '00'});
        this.assignment.patchValue({reviewDeadline: new Date(now_time).toISOString().slice(0, 17) + '00'});
      } else {
        this.autoAssignmentStatus.isOpen = false;
        this.peerReviewStatus.isOpen = true;
      }
    }
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
    if (this.assignment.dirty && this.assignment.valid) {
      this.progressModal.show();
      if (!this.peerReviewStatus.isOpen) {
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
        const selectedMetrics = [];
        for (let i = 0 ; i < this.reviewMetricsNums.length ; i++ ) {
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


  ngOnDestroy() {
    this.javaTabStatus.isOpen = false;
    this.webTabStatus.isOpen = false;
    this.androidTabStatus.isOpen = false;
  }

  
}
