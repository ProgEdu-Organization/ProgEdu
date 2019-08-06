import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashProjectChoosedComponent } from './dash-project-choosed.component';

describe('DashProjectChoosedComponent', () => {
  let component: DashProjectChoosedComponent;
  let fixture: ComponentFixture<DashProjectChoosedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DashProjectChoosedComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashProjectChoosedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
