import { TestBed } from '@angular/core/testing';

import { StudashboardService } from './studashboard.service';

describe('StudashboardService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: StudashboardService = TestBed.get(StudashboardService);
    expect(service).toBeTruthy();
  });
});
