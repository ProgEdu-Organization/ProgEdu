/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ReviewCommitRecordService } from './review-commit-record.service';

describe('Service: ReviewCommitRecord', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ReviewCommitRecordService]
    });
  });

  it('should ...', inject([ReviewCommitRecordService], (service: ReviewCommitRecordService) => {
    expect(service).toBeTruthy();
  }));
});
