import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardStatusComponent } from './dashboard-status.component';

describe('DashboardStatusComponent', () => {
  let component: DashboardStatusComponent;
  let fixture: ComponentFixture<DashboardStatusComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DashboardStatusComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
