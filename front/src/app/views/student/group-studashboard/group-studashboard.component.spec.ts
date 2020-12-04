import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupStudashboardComponent } from './group-studashboard.component';

describe('GroupStudashboardComponent', () => {
  let component: GroupStudashboardComponent;
  let fixture: ComponentFixture<GroupStudashboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GroupStudashboardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupStudashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
