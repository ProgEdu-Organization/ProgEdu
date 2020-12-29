import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CommitRecordComponent } from './commit-record.component';

describe('CommitRecordComponent', () => {
  let component: CommitRecordComponent;
  let fixture: ComponentFixture<CommitRecordComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CommitRecordComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CommitRecordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
