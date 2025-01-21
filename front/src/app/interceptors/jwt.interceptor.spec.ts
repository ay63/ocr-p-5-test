import {TestBed} from '@angular/core/testing';
import {HttpRequest, HttpHandler, HttpEvent, HttpResponse} from '@angular/common/http';
import {of} from 'rxjs';
import {JwtInterceptor} from "./jwt.interceptor";
import {SessionService} from "../services/session.service";
import {expect} from "@jest/globals";

describe('JwtInterceptor', () => {
  let interceptor: JwtInterceptor;
  let sessionService: jest.Mocked<SessionService>;
  let httpHandler: jest.Mocked<HttpHandler>;
  const mockToken: string = 'test-token';

  const mockSessionInformation = {
    admin: false,
    firstName: "",
    id: 0,
    lastName: "",
    type: "",
    username: "",
    token: mockToken
  };

  beforeEach(() => {
    sessionService = {
      isLogged: false,
      sessionInformation: {
        token: mockToken
      }
    } as unknown as jest.Mocked<SessionService>;


    httpHandler = {
      handle: jest.fn()
    } as unknown as jest.Mocked<HttpHandler>;

    TestBed.configureTestingModule({
      providers: [
        JwtInterceptor,
        {provide: SessionService, useValue: sessionService}
      ]
    });

    interceptor = TestBed.inject(JwtInterceptor);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });


  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });

  it('should not add Authorization header when user is not logged in', () => {
    const request = new HttpRequest('GET', '/api/test');
    httpHandler.handle.mockReturnValue(of(new HttpResponse()));
    sessionService.isLogged = false;

    interceptor.intercept(request, httpHandler);

    expect(httpHandler.handle).toHaveBeenCalledWith(request);
    const [modifiedRequest] = httpHandler.handle.mock.lastCall;
    expect(modifiedRequest.headers.has('Authorization')).toBeFalsy();
  });

  it('should add Authorization header with token when user is logged in', () => {
    const request = new HttpRequest('GET', '/api/test');
    httpHandler.handle.mockReturnValue(of(new HttpResponse()));

    sessionService.isLogged = true;
    sessionService.sessionInformation = mockSessionInformation;

    interceptor.intercept(request, httpHandler);

    expect(httpHandler.handle).toHaveBeenCalled();
    const [modifiedRequest] = httpHandler.handle.mock.lastCall;
    expect(modifiedRequest.headers.get('Authorization')).toBe(`Bearer ${mockToken}`);
  });

  it('should correctly clone request with authorization header', () => {
    const request = new HttpRequest('POST', '/api/test', {data: 'test'});

    httpHandler.handle.mockReturnValue(of(new HttpResponse()));

    sessionService.isLogged = true;
    sessionService.sessionInformation = mockSessionInformation

    interceptor.intercept(request, httpHandler);

    const [modifiedRequest] = httpHandler.handle.mock.lastCall ;
    expect(modifiedRequest).not.toBe(request);
    expect(modifiedRequest.body).toEqual({data: 'test'});
    expect(modifiedRequest.method).toBe('POST');
    expect(modifiedRequest.headers.get('Authorization')).toBe(`Bearer ${mockToken}`);
  });
});
