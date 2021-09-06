/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ReviewRoundDashboardService } from './review-round-dashboard.service';

describe('Service: ReviewStatusDashboard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ReviewRoundDashboardService]
    });
  });

  it('should ...', inject([ReviewRoundDashboardService], (service: ReviewRoundDashboardService) => {
    expect(service).toBeTruthy();
  }));
});