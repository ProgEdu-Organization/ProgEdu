/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ReviewDashboardService } from './review-dashboard.service';

describe('Service: ReviewDashboard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ReviewDashboardService]
    });
  });

  it('should ...', inject([ReviewDashboardService], (service: ReviewDashboardService) => {
    expect(service).toBeTruthy();
  }));
});
