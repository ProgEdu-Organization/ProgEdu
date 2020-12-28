import { TestBed } from '@angular/core/testing';

import { GroupStudashboardService } from './group-studashboard.service';

describe('GroupStudashboardService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: GroupStudashboardService = TestBed.get(GroupStudashboardService);
    expect(service).toBeTruthy();
  });
});
