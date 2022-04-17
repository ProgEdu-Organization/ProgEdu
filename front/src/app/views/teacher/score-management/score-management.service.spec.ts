/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ScoreManagementService } from './score-management.service';

describe('Service: ScoreManagement', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ScoreManagementService]
    });
  });

  it('should ...', inject([ScoreManagementService], (service: ScoreManagementService) => {
    expect(service).toBeTruthy();
  }));
});
