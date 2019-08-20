import { TestBed } from '@angular/core/testing';

import { ProjectChoosedService } from './project-choosed.service';

describe('ProjectChoosedService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ProjectChoosedService = TestBed.get(ProjectChoosedService);
    expect(service).toBeTruthy();
  });
});
