/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { TickService } from './tick.service';

describe('TickService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TickService]
    });
  });

  it('should ...', inject([TickService], (service: TickService) => {
    expect(service).toBeTruthy();
  }));
});
