import { TestBed } from '@angular/core/testing';

import { StudentChartService } from './chart-studashboard.service';

describe('ChartService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: StudentChartService = TestBed.get(StudentChartService);
    expect(service).toBeTruthy();
  });
});
