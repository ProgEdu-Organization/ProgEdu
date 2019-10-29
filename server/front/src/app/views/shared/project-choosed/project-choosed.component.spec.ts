import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectChoosedComponent } from './project-choosed.component';

describe('ProjectChoosedComponent', () => {
  let component: ProjectChoosedComponent;
  let fixture: ComponentFixture<ProjectChoosedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectChoosedComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectChoosedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
