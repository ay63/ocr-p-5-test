import {TestBed} from '@angular/core/testing';
import {Router} from '@angular/router';
import {SessionService} from "../services/session.service";
import {AuthGuard} from "./auth.guard";
import {expect} from "@jest/globals";
import {UnauthGuard} from "./unauth.guard";
import { mockTestRouter } from 'tests/mock';

describe('UnAuthGuard', () => {
  let unAuthGuard: UnauthGuard;
  let routerMock: jest.Mocked<Router>;
  let sessionServiceMock: SessionService;

  beforeEach(() => {
    routerMock = mockTestRouter;

    sessionServiceMock = {
      isLogged: false
    } as jest.Mocked<SessionService>;

    TestBed.configureTestingModule({
      providers: [
        AuthGuard,
        {provide: Router, useValue: routerMock},
        {provide: SessionService, useValue: sessionServiceMock}
      ]
    });

    unAuthGuard = TestBed.inject(UnauthGuard);
  });

  afterEach(() => {
    jest.clearAllMocks()
  })

  it('should navigate to "/" and return false if the user is not logged in', () => {
    sessionServiceMock.isLogged = true;
    const result = unAuthGuard.canActivate();
    expect(result).toBe(false);
    expect(routerMock.navigate).toHaveBeenCalledWith(['']);
  });

  it('should return true if the user is not logged in', () => {
    sessionServiceMock.isLogged = false;
    const result = unAuthGuard.canActivate();
    expect(result).toBe(true);
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });


});
