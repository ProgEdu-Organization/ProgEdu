/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ReviewStudashboardService } from './review-studashboard.service';

describe('Service: ReviewStudashboard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ReviewStudashboardService]
    });
  });

  it('should ...', inject([ReviewStudashboardService], (service: ReviewStudashboardService) => {
    expect(service).toBeTruthy();
  }));
});
