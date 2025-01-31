import {TestBed} from '@angular/core/testing';
import {expect} from '@jest/globals';

import {SessionService} from './session.service';
import {mockDataTestSessionInformationNotAdmin} from "../../../tests/mockData";

describe('SessionService', () => {
  let service: SessionService;
  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });


  it('should sessionInformation must be defined and user login', () => {
    service.logIn(mockDataTestSessionInformationNotAdmin)

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBeTruthy();
    });

    expect(service.sessionInformation).toBeDefined();
  })


  it('should sessionInformation must be undefined and user logout', () => {
    service.logOut()

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBeFalsy();
    });

    expect(service.sessionInformation).toBeUndefined();
  })
});
