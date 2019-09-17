import { TestBed } from '@angular/core/testing';

import { AssignmentManagementService } from './assignment-management.service';

describe('AssignmentManagementService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AssignmentManagementService = TestBed.get(AssignmentManagementService);
    expect(service).toBeTruthy();
  });
});
