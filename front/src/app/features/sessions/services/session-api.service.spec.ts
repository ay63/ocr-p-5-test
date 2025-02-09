import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { Session } from "../interfaces/session.interface";
import { mockDataTestSessions } from 'tests/mockData';

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
      ],
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

  describe('All session', () => {
    it('should get all session', () => {
      service.all().subscribe((sessions) => {
        expect(sessions.length).toBe(2);
        expect(sessions).toEqual(mockDataTestSessions);
      });

      const req = httpMock.expectOne(`${API_PATH}`)
      expect(req.request.method).toBe('GET');
      req.flush(mockDataTestSessions);
    })

    it('should handle error on session get all', () => {
      service.all().subscribe({
        error: (err) => expect(err.status).toBe(404)
      });

      const req = httpMock.expectOne(`${API_PATH}`)
      req.flush(null, { status: 404, statusText: 'Not Found' });
      ;
    })
  })

  describe('delete session', () => {
    it('should session be deleted', () => {
      service.delete(id).subscribe();
      const req = httpMock.expectOne(`${API_PATH}/${id}`)
      expect(req.request.method).toBe('DELETE');
      expect(req.request.body).toEqual(null)
    })

    it('should handle error on session delete', () => {
      service.delete(id).subscribe({
        error: (err) => expect(err.status).toBe(404)
      });

      const req = httpMock.expectOne(`${API_PATH}/${id}`)
      req.flush(null, { status: 404, statusText: 'Not Found' });
    })
  })

  describe('create session', () => {
    it('should session be created', () => {
      service.create(session).subscribe(response => {
        expect(response).toEqual(session)
      });

      const req = httpMock.expectOne(`${API_PATH}`)
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(session)
      req.flush(session);
    })

    it('should handle error on session created', () => {
      service.create(session).subscribe({
        error: (err) =>expect(err.status).toBe(400)
      });

      const req = httpMock.expectOne(`${API_PATH}`)
      req.flush(null, { status: 400, statusText: 'Bad Request' });

    })
  })

  describe('update session', () => {
    it('should session be updated', () => {
      service.update(id, sessionUpdate).subscribe(response => {
        expect(response).toEqual(sessionUpdate);
      });

      const req = httpMock.expectOne(`${API_PATH}/${id}`)
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(sessionUpdate)

      req.flush(sessionUpdate);
    })

    it('should handle error on session update', () => {
      service.update(id, sessionUpdate).subscribe({
        error: (err) => expect(err.status).toBe(400)
      });

      const req = httpMock.expectOne(`${API_PATH}/${id}`)
      req.flush(null, { status: 400, statusText: 'Bad Request' });
      
    })

  })

  describe('participate session', () => {
    it('should participate added to session', () => {
      service.participate(id, id).subscribe();
      const req = httpMock.expectOne(`${API_PATH}/${id}/participate/${id}`);

      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(null)
    })

    it('should handle error on added participate', () => {
      service.participate(id, id).subscribe({
        error: (err) => expect(err.status).toBe(404)
      });

      const req = httpMock.expectOne(`${API_PATH}/${id}/participate/${id}`);
      req.flush(null, { status: 404, statusText: 'Bad Request' });
  
    })

  })

  describe('unparticipate session', () => {
    it('should Unparticipate from the session', () => {
      service.unParticipate(id, id).subscribe();
      const req = httpMock.expectOne(`${API_PATH}/${id}/participate/${id}`);

      expect(req.request.method).toBe('DELETE');
      expect(req.request.body).toEqual(null)
    })

    it('should handle error on Unparticipate', () => {
      service.unParticipate(id, id).subscribe({
        error: (err) => expect(err.status).toBe(404)
      });

      const req = httpMock.expectOne(`${API_PATH}/${id}/participate/${id}`);
      req.flush(null, { status: 404, statusText: 'Bad Request' });
      ;
    })
  })

  describe('detail session', () => {
    it('should get detail session', () => {
      service.detail(id).subscribe(response => {
        expect(response).toEqual(session);
      });
      const req = httpMock.expectOne(`${API_PATH}/${id}`);
      req.flush(session);

      expect(req.request.body).toEqual(null)
      expect(req.request.method).toBe('GET');

    })

    it('should handle error on detail', () => {
      service.detail(id).subscribe({
        error: (err) => expect(err.status).toBe(404)
      });

      const req = httpMock.expectOne(`${API_PATH}/${id}`);
      req.flush(null, { status: 404, statusText: 'Bad Request' });
    })
  })

});
