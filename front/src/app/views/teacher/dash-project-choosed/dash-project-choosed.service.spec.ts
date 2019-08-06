import { TestBed } from '@angular/core/testing';

import { DashProjectChoosedService } from './dash-project-choosed.service';

describe('DashProjectChoosedService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DashProjectChoosedService = TestBed.get(DashProjectChoosedService);
    expect(service).toBeTruthy();
  });
});
