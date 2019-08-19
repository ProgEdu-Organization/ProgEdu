import { TestBed } from '@angular/core/testing';

import { CanActiveTeacherService } from './can-active-teacher.service';

describe('CanActiveTeacherService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CanActiveTeacherService = TestBed.get(CanActiveTeacherService);
    expect(service).toBeTruthy();
  });
});
