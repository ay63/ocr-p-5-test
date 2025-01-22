import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ActivatedRoute} from '@angular/router';
import {Router} from '@angular/router';
import {FormBuilder} from '@angular/forms';
import {MatSnackBar} from '@angular/material/snack-bar';
import {of} from 'rxjs';
import {DetailComponent} from './detail.component';
import {expect} from "@jest/globals";
import {SessionService} from "../../../../services/session.service";
import {SessionApiService} from "../../services/session-api.service";
import {TeacherService} from "../../../../services/teacher.service";
import {Teacher} from "../../../../interfaces/teacher.interface";
import {Session} from "../../interfaces/session.interface";

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let mockRoute: jest.Mocked<ActivatedRoute>;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockSessionApiService: jest.Mocked<SessionApiService>;
  let mockTeacherService: jest.Mocked<TeacherService>;
  let mockMatSnackBar: jest.Mocked<MatSnackBar>;
  let mockRouter: jest.Mocked<Router>;

  const mockSession: Session = {
    id: 1,
    name: 'Test Session',
    description: 'test',
    teacher_id: 1,
    users: [1],
    date: new Date(),
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  const mockTeacher: Teacher = {
    id: 1,
    lastName: "string",
    firstName: "string",
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  beforeEach(() => {
    mockRoute = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('1')
        }
      }
    } as unknown as jest.Mocked<ActivatedRoute>;

    mockSessionService = {
      sessionInformation: {
        id: 1,
        admin: true
      }
    } as unknown as jest.Mocked<SessionService>;

    mockSessionApiService = {
      detail: jest.fn().mockReturnValue(of(mockSession)),
      delete: jest.fn().mockReturnValue(of({})),
      participate: jest.fn().mockReturnValue(of({})),
      unParticipate: jest.fn().mockReturnValue(of({}))
    } as unknown as jest.Mocked<SessionApiService>;

    mockTeacherService = {
      detail: jest.fn().mockReturnValue(of(mockTeacher))
    } as unknown as jest.Mocked<TeacherService>;

    mockMatSnackBar = {
      open: jest.fn()
    } as unknown as jest.Mocked<MatSnackBar>;

    mockRouter = {
      navigate: jest.fn()
    } as unknown as jest.Mocked<Router>;

    TestBed.configureTestingModule({
      declarations: [DetailComponent],
      providers: [
        FormBuilder,
        {provide: ActivatedRoute, useValue: mockRoute},
        {provide: SessionService, useValue: mockSessionService},
        {provide: SessionApiService, useValue: mockSessionApiService},
        {provide: TeacherService, useValue: mockTeacherService},
        {provide: MatSnackBar, useValue: mockMatSnackBar},
        {provide: Router, useValue: mockRouter}
      ]
    });

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('constructor', () => {
    it('should initialize component properties correctly', () => {
      expect(component.sessionId).toBe('1');
      expect(component.isAdmin).toBe(true);
      expect(component.userId).toBe('1');
    });
  });

  describe('ngOnInit', () => {
    it('should fetch session and teacher details', () => {
      component.ngOnInit();

      expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
      expect(mockTeacherService.detail).toHaveBeenCalledWith('1');
      expect(component.session).toEqual(mockSession);
      expect(component.teacher).toEqual(mockTeacher);
      expect(component.isParticipate).toBe(true);
    });
  });

  describe('back', () => {
    it('should call window.history.back', () => {
      const spyHistoryBack = jest.spyOn(window.history, 'back');

      component.back();

      expect(spyHistoryBack).toHaveBeenCalled();
    });
  });

  describe('delete', () => {
    it('should delete session and navigate to sessions page', () => {
      component.delete();

      expect(mockSessionApiService.delete).toHaveBeenCalledWith('1');
      expect(mockMatSnackBar.open).toHaveBeenCalledWith(
        'Session deleted !',
        'Close',
        {duration: 3000}
      );
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });

  describe('participate', () => {
    it('should call participate and refresh session data', () => {
      component.participate();

      expect(mockSessionApiService.participate).toHaveBeenCalledWith('1', '1');
      expect(mockSessionApiService.detail).toHaveBeenCalled();
    });
  });

  describe('unParticipate', () => {
    it('should call unParticipate and refresh session data', () => {
      component.unParticipate();

      expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith('1', '1');
      expect(mockSessionApiService.detail).toHaveBeenCalled();
    });
  });

  describe('fetchSession', () => {
    it('should update session participation status correctly when user is not participating', () => {
      const nonParticipatingSession = {...mockSession, users: [2]};
      mockSessionApiService.detail.mockReturnValueOnce(of(nonParticipatingSession));

      component.ngOnInit();

      expect(component.isParticipate).toBe(false);
    });

    it('should handle teacher details fetch correctly', () => {
      const differentTeacher = {...mockTeacher, id: 2, name: 'Different Teacher'};
      mockTeacherService.detail.mockReturnValueOnce(of(differentTeacher));

      component.ngOnInit();

      expect(component.teacher).toEqual(differentTeacher);
    });
  });
});
