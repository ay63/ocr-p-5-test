import {TestBed} from '@angular/core/testing';
import {expect} from '@jest/globals';

import {SessionService} from './session.service';
import {SessionInformation} from "../interfaces/sessionInformation.interface";

describe('SessionService', () => {
  let service: SessionService;

  const sessionInformation: SessionInformation = {
    token: "token",
    type: "Bearer",
    id: 1,
    username: "username",
    firstName: "firstName",
    lastName: "lastName",
    admin: false,
  }


  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });


  it('should log in user', () => {

    service.logIn(sessionInformation)
    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBeTruthy();
    });

    expect(sessionInformation).toEqual(sessionInformation);
  })


  it('should log out user', () => {
    service.logOut()

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBeFalsy();
    });

    expect(service.sessionInformation).toBeUndefined();
  })


});
