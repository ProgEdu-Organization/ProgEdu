/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ReviewRoundStudashboardService } from './review-round-studashboard.service';

describe('Service: ReviewRoundStudashboard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ReviewRoundStudashboardService]
    });
  });

  it('should ...', inject([ReviewRoundStudashboardService], (service: ReviewRoundStudashboardService) => {
    expect(service).toBeTruthy();
  }));
});