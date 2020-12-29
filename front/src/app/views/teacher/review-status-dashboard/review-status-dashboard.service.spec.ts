/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ReviewStatusDashboardService } from './review-status-dashboard.service';

describe('Service: ReviewStatusDashboard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ReviewStatusDashboardService]
    });
  });

  it('should ...', inject([ReviewStatusDashboardService], (service: ReviewStatusDashboardService) => {
    expect(service).toBeTruthy();
  }));
});
