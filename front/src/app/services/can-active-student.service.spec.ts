import { TestBed } from '@angular/core/testing';

import { CanActiveStudentService } from './can-active-student.service';

describe('CanActiveStudentService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CanActiveStudentService = TestBed.get(CanActiveStudentService);
    expect(service).toBeTruthy();
  });
});
