import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignmentManagementComponent } from './assignment-management.component';

describe('AssignmentManagementComponent', () => {
  let component: AssignmentManagementComponent;
  let fixture: ComponentFixture<AssignmentManagementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AssignmentManagementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AssignmentManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
