import { TestBed } from '@angular/core/testing';

import { StudentManagementService } from './student-management.service';

describe('StudentManagementService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: StudentManagementService = TestBed.get(StudentManagementService);
    expect(service).toBeTruthy();
  });
});
