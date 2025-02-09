import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { Teacher } from "../interfaces/teacher.interface";
import { mockDataTestListTeachers } from "../../../tests/mockData";

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;
  let id = "1"
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
    service.all().subscribe(response => {
      expect(response).toEqual(mockDataTestListTeachers)
    });

    const req = httpMock.expectOne(`${API_PATH}`)
    expect(req.request.method).toBe('GET');
    req.flush(mockDataTestListTeachers)
  })

  it('should handle error on get teacher', () => {
    let error: any;
    service.all().subscribe({
      error: (e) => error = e
    });

    const req = httpMock.expectOne(`${API_PATH}`)
    req.flush(null, { status: 400, statusText: 'Bad Request' });
    expect(error.status).toBe(400);
  })

  it('should get detail teacher by id', () => {

    service.detail("2").subscribe(response => {
      expect(response).toEqual(mockDataTestListTeachers[1])
    });
    const req = httpMock.expectOne(`${API_PATH}/2`)

    expect(req.request.method).toBe('GET');
    req.flush(mockDataTestListTeachers[1])

  })

  it('should handle error on get detail teacher by id', () => {
    let error: any;
    service.detail(id).subscribe({
      error: (e) => error = e
    });

    const req = httpMock.expectOne(`${API_PATH}/${id}`)
    req.flush(null, { status: 400, statusText: 'Bad Request' });
    expect(error.status).toBe(400);
  })
});
