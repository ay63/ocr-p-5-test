import {ComponentFixture, TestBed} from '@angular/core/testing';
import {FormBuilder, ReactiveFormsModule} from '@angular/forms';
import {MatSnackBar} from '@angular/material/snack-bar';
import {ActivatedRoute, Router} from '@angular/router';
import {of} from 'rxjs';
import {SessionService} from '../../../../services/session.service';
import {TeacherService} from '../../../../services/teacher.service';
import {SessionApiService} from '../../services/session-api.service';
import {FormComponent} from './form.component';
import {expect} from "@jest/globals";

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let mockRouter: jest.Mocked<Router>;
  let mockRoute: jest.Mocked<ActivatedRoute>;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockSessionApiService: jest.Mocked<SessionApiService>;
  let mockTeacherService: jest.Mocked<TeacherService>;
  let mockMatSnackBar: jest.Mocked<MatSnackBar>;

  beforeEach(async () => {
    mockRouter = {
      navigate: jest.fn(),
      get url() {
        return '/sessions/create'
      }
    } as any;

    mockRoute = {
      snapshot: {
        paramMap: new Map().set('id', '1')
      }
    } as unknown as jest.Mocked<ActivatedRoute>;

    mockSessionService = {
      sessionInformation: {
        admin: false,
        id: 1
      }
    } as unknown as jest.Mocked<SessionService>;

    mockSessionApiService = {
      detail: jest.fn().mockReturnValue(of({
        id: '1',
        name: 'Test Session',
        date: '2024-01-21',
        teacher_id: '1',
        description: 'Test Description'
      })),
      create: jest.fn().mockReturnValue(of({})),
      update: jest.fn().mockReturnValue(of({}))
    } as unknown as jest.Mocked<SessionApiService>;

    mockTeacherService = {
      all: jest.fn().mockReturnValue(of([]))
    } as unknown as jest.Mocked<TeacherService>;

    mockMatSnackBar = {
      open: jest.fn()
    } as unknown as jest.Mocked<MatSnackBar>;

    await TestBed.configureTestingModule({
      declarations: [FormComponent],
      imports: [ReactiveFormsModule],
      providers: [
        FormBuilder,
        {provide: Router, useValue: mockRouter},
        {provide: ActivatedRoute, useValue: mockRoute},
        {provide: SessionService, useValue: mockSessionService},
        {provide: SessionApiService, useValue: mockSessionApiService},
        {provide: TeacherService, useValue: mockTeacherService},
        {provide: MatSnackBar, useValue: mockMatSnackBar}
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should redirect if user is admin', () => {
      mockSessionService.sessionInformation = {
        firstName: "",
        id: 0,
        lastName: "",
        token: "",
        type: "",
        username: "",
        admin: true
      };

      component.ngOnInit();

      expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    });

    it('should not redirect if user is not admin', () => {
      mockSessionService.sessionInformation = {
        firstName: "",
        id: 0,
        lastName: "",
        token: "",
        type: "",
        username: "",
        admin: false
      };

      component.ngOnInit();

      expect(mockRouter.navigate).not.toHaveBeenCalled();
    });

    it('should initialize form for create mode', () => {
      jest.spyOn(mockRouter, 'url', 'get').mockReturnValue('/sessions/create');

      component.ngOnInit();

      expect(component.onUpdate).toBe(false);
      expect(mockSessionApiService.detail).not.toHaveBeenCalled();
      expect(component.sessionForm).toBeDefined();
      expect(component.sessionForm?.get('name')?.value).toBe('');
    });

    it('should initialize form for update mode', () => {
      jest.spyOn(mockRouter, 'url', 'get').mockReturnValue('/sessions/update/1');

      component.ngOnInit();
      fixture.detectChanges();

      expect(component.onUpdate).toBe(true);
      expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
    });
  });

  describe('submit', () => {
    beforeEach(() => {
      component.ngOnInit();
      fixture.detectChanges();
    });

    it('should create new session', () => {
      const testSession = {
        name: 'New Session',
        date: '2024-01-21',
        teacher_id: '1',
        description: 'Test Description'
      };
      component.sessionForm?.setValue(testSession);

      component.submit();

      expect(mockSessionApiService.create).toHaveBeenCalledWith(testSession);
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session created !', 'Close', {duration: 3000});
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });

    it('should update existing session', () => {
      jest.spyOn(mockRouter, 'url', 'get').mockReturnValue('/sessions/update/1');
      component.ngOnInit();
      fixture.detectChanges();

      const testSession = {
        name: 'Updated Session',
        date: '2024-01-21',
        teacher_id: '1',
        description: 'Updated Description'
      };
      component.sessionForm?.setValue(testSession);

      component.submit();

      expect(mockSessionApiService.update).toHaveBeenCalledWith('1', testSession);
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', {duration: 3000});
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });

  describe('form validation', () => {
    beforeEach(() => {
      component.ngOnInit();
      fixture.detectChanges();
    });

    it('should require all fields', () => {
      expect(component.sessionForm?.get('name')?.hasError('required')).toBeTruthy();
      expect(component.sessionForm?.get('date')?.hasError('required')).toBeTruthy();
      expect(component.sessionForm?.get('teacher_id')?.hasError('required')).toBeTruthy();
      expect(component.sessionForm?.get('description')?.hasError('required')).toBeTruthy();
    });

    it('should validate description max length', () => {
      const longDescription = 'a'.repeat(2001);
      component.sessionForm?.get('description')?.setValue(longDescription);
      expect(component.sessionForm?.get('description')?.hasError('maxlength')).toBeTruthy();
      const errors = component.sessionForm?.get('description')?.errors;

      expect(errors?.['maxlength']?.requiredLength).toBe(2000);
      expect(errors?.['maxlength']?.actualLength).toBe(2001);
    });
  });
});
