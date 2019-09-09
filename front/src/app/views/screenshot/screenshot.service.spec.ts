import { TestBed } from '@angular/core/testing';

import { ScreenshotService } from './screenshot.service';

describe('ScreenshotService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ScreenshotService = TestBed.get(ScreenshotService);
    expect(service).toBeTruthy();
  });
});
