import { TestBed } from '@angular/core/testing';

import { GroupDashboardService } from './group-dashboard.service';

describe('GroupDashboardService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: GroupDashboardService = TestBed.get(GroupDashboardService);
    expect(service).toBeTruthy();
  });
});
