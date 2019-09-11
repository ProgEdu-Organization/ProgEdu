import { TestBed } from '@angular/core/testing';

import { CreateAssignmentService } from './create-assignment.service';

describe('CreateAssignmentService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CreateAssignmentService = TestBed.get(CreateAssignmentService);
    expect(service).toBeTruthy();
  });
});
