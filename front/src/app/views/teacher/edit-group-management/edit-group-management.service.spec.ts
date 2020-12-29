import { TestBed } from '@angular/core/testing';

import { EditGroupManagementService } from './edit-group-management.service';

describe('EditGroupManagementService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: EditGroupManagementService = TestBed.get(EditGroupManagementService);
    expect(service).toBeTruthy();
  });
});
