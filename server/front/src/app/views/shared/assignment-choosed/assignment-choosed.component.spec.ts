import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignmentChoosedComponent } from './assignment-choosed.component';

describe('AssignmentChoosedComponent', () => {
  let component: AssignmentChoosedComponent;
  let fixture: ComponentFixture<AssignmentChoosedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AssignmentChoosedComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AssignmentChoosedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
