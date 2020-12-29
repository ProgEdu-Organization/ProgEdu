/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ReviewStatusStudashboardService } from './review-status-studashboard.service';

describe('Service: ReviewStatusStudashboard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ReviewStatusStudashboardService]
    });
  });

  it('should ...', inject([ReviewStatusStudashboardService], (service: ReviewStatusStudashboardService) => {
    expect(service).toBeTruthy();
  }));
});
