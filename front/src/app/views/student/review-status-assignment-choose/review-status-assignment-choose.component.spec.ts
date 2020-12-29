/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { ReviewStatusAssignmentChooseComponent } from './review-status-assignment-choose.component';

describe('ReviewStatusAssignmentChooseComponent', () => {
  let component: ReviewStatusAssignmentChooseComponent;
  let fixture: ComponentFixture<ReviewStatusAssignmentChooseComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReviewStatusAssignmentChooseComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewStatusAssignmentChooseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
