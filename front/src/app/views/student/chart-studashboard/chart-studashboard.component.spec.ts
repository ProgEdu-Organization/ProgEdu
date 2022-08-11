import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentChartComponent } from './chart-studashboard.component';

describe('ChartComponent', () => {
  let component: StudentChartComponent;
  let fixture: ComponentFixture<StudentChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudentChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudentChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});