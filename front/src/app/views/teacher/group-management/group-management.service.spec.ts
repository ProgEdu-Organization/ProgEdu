import { TestBed } from '@angular/core/testing';

import { GroupManagementService } from './group-management.service';

describe('GroupManagementService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: GroupManagementService = TestBed.get(GroupManagementService);
    expect(service).toBeTruthy();
  });
});
