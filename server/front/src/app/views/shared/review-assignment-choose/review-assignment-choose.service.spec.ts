/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ReviewAssignmentChooseService } from './review-assignment-choose.service';

describe('Service: ReviewAssignmentChoose', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ReviewAssignmentChooseService]
    });
  });

  it('should ...', inject([ReviewAssignmentChooseService], (service: ReviewAssignmentChooseService) => {
    expect(service).toBeTruthy();
  }));
});
