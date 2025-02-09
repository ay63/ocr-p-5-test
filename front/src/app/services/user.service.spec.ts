import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { User } from "../interfaces/user.interface";
import { mockDataTestUserNotAdmin } from "../../../tests/mockData";

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;
  let id: string = "1";
  const API_PATH = 'api/user'

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get user by id', () => {
    service.getById(id).subscribe(response => {
      expect(response).toEqual(mockDataTestUserNotAdmin)
    });
    const req = httpMock.expectOne(`${API_PATH}/${id}`)
    expect(req.request.method).toBe('GET');
    
    req.flush(mockDataTestUserNotAdmin)
  })

  it('should handle error on get user by id', () => {
    let error: any;
    service.getById(id).subscribe({
      error: (e) => error = e
    });

    const req = httpMock.expectOne(`${API_PATH}/${id}`)
    req.flush(null, { status: 400, statusText: 'Bad Request' });
    expect(error.status).toBe(400);
  })

  it('should delete user by id', () => {
    service.delete(id).subscribe(response => {
      expect(response).toEqual(mockDataTestUserNotAdmin)
    });
    const req = httpMock.expectOne(`${API_PATH}/${id}`)
    expect(req.request.method).toBe('DELETE');

    req.flush(mockDataTestUserNotAdmin)
  })

  it('should handle error on delete user by id', () => {
    let error: any;
    service.delete(id).subscribe({
      error: (e) => error = e
    });

    const req = httpMock.expectOne(`${API_PATH}/${id}`)
    req.flush(null, { status: 400, statusText: 'Bad Request' });
    expect(error.status).toBe(400);
  })

});
