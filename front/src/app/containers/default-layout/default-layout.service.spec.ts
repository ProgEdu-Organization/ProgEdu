import { TestBed } from '@angular/core/testing';

import { DefaultLayoutService } from './default-layout.service';

describe('DefaultLayoutService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DefaultLayoutService = TestBed.get(DefaultLayoutService);
    expect(service).toBeTruthy();
  });
});
