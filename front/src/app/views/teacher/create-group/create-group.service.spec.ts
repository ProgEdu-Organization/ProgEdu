import { TestBed } from '@angular/core/testing';

import { CreateGroupService } from './create-group.service';

describe('CreateGroupService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CreateGroupService = TestBed.get(CreateGroupService);
    expect(service).toBeTruthy();
  });
});
