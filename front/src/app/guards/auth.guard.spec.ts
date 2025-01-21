import {TestBed} from '@angular/core/testing';
import {Router} from '@angular/router';
import {SessionService} from "../services/session.service";
import {AuthGuard} from "./auth.guard";
import {expect} from "@jest/globals";

describe('AuthGuard', () => {
  let authGuard: AuthGuard;
  let routerMock: jest.Mocked<Router>;
  let sessionServiceMock: SessionService;

  beforeEach(() => {
    routerMock = {
      navigate: jest.fn()
    } as unknown as jest.Mocked<Router>;

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

    sessionServiceMock = TestBed.inject(SessionService);
    authGuard = TestBed.inject(AuthGuard);
  });

  it('should return true if the user is logged in', () => {
    sessionServiceMock.isLogged = true;

    const result = authGuard.canActivate();

    expect(result).toBe(true);
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });

  it('should navigate to "login" and return false if the user is not logged in', () => {
    sessionServiceMock.isLogged = false;

    const result = authGuard.canActivate();

    expect(result).toBe(false);
    expect(routerMock.navigate).toHaveBeenCalledWith(['login']);
  });
});
