import {HttpClientModule} from '@angular/common/http';
import {TestBed} from '@angular/core/testing';
import {expect} from '@jest/globals';

import {TeacherService} from './teacher.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Teacher} from "../interfaces/teacher.interface";

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;
  let id = "1"
  let teachers: Teacher[] = [
    {
      id: 1,
      lastName: "lastName",
      firstName: "firstName",
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ]

  const API_PATH = 'api/teacher'
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get teacher', () => {
    let result: Teacher[] = teachers
    service.all().subscribe(response => {
      result = response;
    });
    const req = httpMock.expectOne(`${API_PATH}`)

    expect(req.request.method).toBe('GET');
    expect(result).toEqual(teachers)
  })


  it('should handle error on get teacher', () => {
    let error: any;
    service.all().subscribe({
      error: (e) => error = e
    });

    const req = httpMock.expectOne(`${API_PATH}`)
    req.flush(null, {status: 400, statusText: 'Bad Request'});
    expect(error.status).toBe(400);
  })


  it('should get detail teacher by id', () => {
    let result: Teacher = teachers[0]

    service.detail(id).subscribe(response => {
      result = response;
    });
    const req = httpMock.expectOne(`${API_PATH}/${id}`)

    expect(req.request.method).toBe('GET');
    expect(result).toEqual(teachers[0])
  })

  it('should handle error on get detail teacher by id', () => {
    let error: any;
    service.detail(id).subscribe({
      error: (e) => error = e
    });

    const req = httpMock.expectOne(`${API_PATH}/${id}`)
    req.flush(null, {status: 400, statusText: 'Bad Request'});
    expect(error.status).toBe(400);
  })


});
