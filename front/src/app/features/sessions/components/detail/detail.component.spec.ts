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
import {
  mockTestMatSnackBar,
  mockTestRoute, mockTestRouter,
  mockTestSessionApiService,
  mockTestSessionService,
  mockTestTeacherService
} from "../../../../../tests/mock";
import {mockDataTestSession, mockDataTestTeacher} from "../../../../../tests/mockData";

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let mockRoute: jest.Mocked<ActivatedRoute>;
  let mockSessionApiService: jest.Mocked<SessionApiService>;
  let mockTeacherService: jest.Mocked<TeacherService>;
  let mockMatSnackBar: jest.Mocked<MatSnackBar>;
  let mockRouter: jest.Mocked<Router>;

  beforeEach(() => {
    mockRoute = mockTestRoute;
    let mockSessionService = {
      ...mockTestSessionService,
      sessionInformation: {
        ...mockTestSessionService.sessionInformation,
        admin: true,
      },
    };
    mockSessionApiService = mockTestSessionApiService
    mockTeacherService = mockTestTeacherService
    mockMatSnackBar = mockTestMatSnackBar
    mockRouter = mockTestRouter

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
      expect(component.session).toEqual(mockDataTestSession);
      expect(component.teacher).toEqual(mockDataTestTeacher);
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
      const nonParticipatingSession = {...mockDataTestSession, users: [2]};
      mockSessionApiService.detail.mockReturnValueOnce(of(nonParticipatingSession));

      component.ngOnInit();

      expect(component.isParticipate).toBe(false);
    });

    it('should handle teacher details fetch correctly', () => {
      const differentTeacher = {...mockDataTestTeacher, id: 2, name: 'Different Teacher'};
      mockTeacherService.detail.mockReturnValueOnce(of(differentTeacher));

      component.ngOnInit();

      expect(component.teacher).toEqual(differentTeacher);
    });
  });
});
