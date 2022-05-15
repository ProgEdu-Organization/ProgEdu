import { TestBed } from '@angular/core/testing';

import { EditScoreManagementService } from './edit-score-management.service';

describe('EditScoreManagementService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: EditScoreManagementService = TestBed.get(EditScoreManagementService);
    expect(service).toBeTruthy();
  });
});
