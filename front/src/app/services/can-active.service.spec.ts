import { TestBed } from '@angular/core/testing';

import { CanActiveService } from './can-active.service';

describe('CanActiveService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CanActiveService = TestBed.get(CanActiveService);
    expect(service).toBeTruthy();
  });
});
