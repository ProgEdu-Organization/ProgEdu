/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ReviewMetricsManagementService } from './review-metrics-management.service';

describe('Service: ReviewMetricsManagement', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ReviewMetricsManagementService]
    });
  });

  it('should ...', inject([ReviewMetricsManagementService], (service: ReviewMetricsManagementService) => {
    expect(service).toBeTruthy();
  }));
});
