/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ReviewStatusAssignmentChooseService } from './review-status-assignment-choose.service';

describe('Service: ReviewStatusAssignmentChoose', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ReviewStatusAssignmentChooseService]
    });
  });

  it('should ...', inject([ReviewStatusAssignmentChooseService], (service: ReviewStatusAssignmentChooseService) => {
    expect(service).toBeTruthy();
  }));
});
