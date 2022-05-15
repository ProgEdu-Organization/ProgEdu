import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditScoreManagementComponent } from './edit-score-management.component';

describe('EditScoreManagementComponent', () => {
  let component: EditScoreManagementComponent;
  let fixture: ComponentFixture<EditScoreManagementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditScoreManagementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditScoreManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});