import {TestBed} from '@angular/core/testing';
import {expect} from '@jest/globals';

import {SessionApiService} from './session-api.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Session} from "../interfaces/session.interface";

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;
  const API_PATH = 'api/session';
  const id: string = "1";
  const session: Session =
    {
      "id": 2,
      "name": "one sessions",
      "date": new Date(),
      "teacher_id": 1,
      "description": "session description",
      "users": [1, 2],
      "createdAt": new Date(),
      "updatedAt": new Date()
    }

  const sessionUpdate: Session = {
    name: "session 1",
    date: new Date(),
    teacher_id: 1,
    description: "my description",
    users: []
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should session be delete sessions', () => {
    service.delete(id).subscribe();
    const req = httpMock.expectOne(`${API_PATH}/${id}`)
    expect(req.request.method).toBe('DELETE');
    expect(req.request.body).toEqual(null)
  })


  it('should handle error on session delete', () => {
    let error: any;
    service.delete(id).subscribe({
      error: (e) => error = e
    });

    const req = httpMock.expectOne(`${API_PATH}/${id}`)
    req.flush(null, {status: 404, statusText: 'Bad Request'});
    expect(error.status).toBe(404);
  })

  it('should session be created', () => {
    let result: Session | undefined;
    service.create(session).subscribe(response => {
      result = response;
    });

    const req = httpMock.expectOne(`${API_PATH}`)
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(session)

    req.flush(session);
    expect(result).toEqual(session)

  })

  it('should handle error on session created', () => {
    let error: any;
    service.create(session).subscribe({
      error: (e) => error = e
    });

    const req = httpMock.expectOne(`${API_PATH}`)
    req.flush(null, {status: 400, statusText: 'Bad Request'});
    expect(error.status).toBe(400);
  })

  it('should session be updated', () => {
    let result: Session | undefined;
    service.update(id, sessionUpdate).subscribe(response => {
      result = response;
    });

    const req = httpMock.expectOne(`${API_PATH}/${id}`)
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(sessionUpdate)

    req.flush(sessionUpdate);
    expect(result).toEqual(sessionUpdate);
  })

  it('should handle error on session update', () => {
    let error: any;
    service.update(id, sessionUpdate).subscribe({
      error: (e) => error = e
    });

    const req = httpMock.expectOne(`${API_PATH}/${id}`)
    req.flush(null, {status: 400, statusText: 'Bad Request'});
    expect(error.status).toBe(400);
  })

  it('should participate added to session', () => {
    service.participate(id, id).subscribe();
    const req = httpMock.expectOne(`${API_PATH}/${id}/participate/${id}`);

    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(null)
  })

  it('should handle error on added participate', () => {
    let error: any;
    service.participate(id, id).subscribe({
      error: (e) => error = e
    });

    const req = httpMock.expectOne(`${API_PATH}/${id}/participate/${id}`);
    req.flush(null, {status: 404, statusText: 'Bad Request'});
    expect(error.status).toBe(404);
  })

  it('should Unparticipate from the session', () => {
    service.unParticipate(id, id).subscribe();
    const req = httpMock.expectOne(`${API_PATH}/${id}/participate/${id}`);

    expect(req.request.method).toBe('DELETE');
    expect(req.request.body).toEqual(null)
  })

  it('should handle error on Unparticipate', () => {
    let error: any;
    service.unParticipate(id, id).subscribe({
      error: (e) => error = e
    });

    const req = httpMock.expectOne(`${API_PATH}/${id}/participate/${id}`);
    req.flush(null, {status: 404, statusText: 'Bad Request'});
    expect(error.status).toBe(404);
  })
});
