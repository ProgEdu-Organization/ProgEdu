import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MixedChartComponent } from './mixed-chart.component';

describe('MixedChartComponent', () => {
  let component: MixedChartComponent;
  let fixture: ComponentFixture<MixedChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MixedChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MixedChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
