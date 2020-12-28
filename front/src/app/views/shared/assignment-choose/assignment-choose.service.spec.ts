import { TestBed } from '@angular/core/testing';

import { AssignmentChoosedService } from './assignment-choose.service';

describe('AssignmentChooseService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AssignmentChoosedService = TestBed.get(AssignmentChoosedService);
    expect(service).toBeTruthy();
  });
});
