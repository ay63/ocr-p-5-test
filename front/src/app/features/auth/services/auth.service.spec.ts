import {TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {AuthService} from './auth.service';
import {LoginRequest} from "../interfaces/loginRequest.interface";
import {RegisterRequest} from "../interfaces/registerRequest.interface";
import {SessionInformation} from "../../../interfaces/sessionInformation.interface";
import {expect} from '@jest/globals';
import {mockDataTestSessionInformationNotAdmin} from "../../../../../tests/mockData";

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  const API_PATH = 'api/auth';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('AuthService register', () => {
    it('should send POST request to register endpoint', () => {

      const registerRequest: RegisterRequest = {
        firstName: 'testUserFirstName',
        lastName: 'testUserLastname',
        password: 'testPassword',
        email: 'test@example.com'
      };

      service.register(registerRequest).subscribe();

      const req = httpMock.expectOne(`${API_PATH}/register`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(registerRequest);

      req.flush(null);
    });

    it('should handle registration error', () => {
      const registerRequest: RegisterRequest = {
        firstName: 'testUserFirstName',
        lastName: 'testUserLastname',
        password: 'testPassword',
        email: 'test@example.com'
      };
      const errorMessage = 'Registration failed';

      let error: any;
      service.register(registerRequest).subscribe({
        error: (e) => error = e
      });

      const req = httpMock.expectOne(`${API_PATH}/register`);
      req.flush(errorMessage, {status: 400, statusText: 'Bad Request'});

      expect(error.status).toBe(400);
    });
  });

  describe('AuthService login', () => {
    it('should send POST request to login endpoint and return session information', () => {
      const loginRequest: LoginRequest = {
        email: 'testUser',
        password: 'testPassword'
      };

      service.login(loginRequest).subscribe(response => {
        expect(response).toEqual(mockDataTestSessionInformationNotAdmin);
      });

      const req = httpMock.expectOne(`${API_PATH}/login`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(loginRequest);

      req.flush(mockDataTestSessionInformationNotAdmin);
    
    });

    it('should handle login error', () => {
      const loginRequest: LoginRequest = {
        email: 'testUser',
        password: 'wrongPassword'
      };
      const errorMessage = 'Invalid credentials';

      let error: any;
      service.login(loginRequest).subscribe({
        error: (e) => error = e
      });

      const req = httpMock.expectOne(`${API_PATH}/login`);
      req.flush(errorMessage, {status: 401, statusText: 'Unauthorized'});

      expect(error.status).toBe(401);
    });
  });
});
