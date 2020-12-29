import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditGroupManagementComponent } from './edit-group-management.component';

describe('EditGroupManagementComponent', () => {
  let component: EditGroupManagementComponent;
  let fixture: ComponentFixture<EditGroupManagementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditGroupManagementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditGroupManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
