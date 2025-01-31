import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { of } from 'rxjs';
import { MeComponent } from './me.component';
import { SessionService } from "../../services/session.service";
import { UserService } from "../../services/user.service";
import { expect } from "@jest/globals";
import { DatePipe } from "@angular/common";
import {
  mockTestMatSnackBar,
  mockTestRouter,
  mockTestSessionService,
  mockTestUserService
} from "../../../../tests/mock";
import {mockDataTestUserIsAdmin, mockDataTestUserNotAdmin} from "../../../../tests/mockData";

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockRouter: jest.Mocked<Router>;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockMatSnackBar: jest.Mocked<MatSnackBar>;
  let mockUserService: jest.Mocked<UserService>;
  let compiled: HTMLElement;

  beforeEach(async () => {
    mockRouter = mockTestRouter;
    mockSessionService = mockTestSessionService;
    mockMatSnackBar = mockTestMatSnackBar;
    mockUserService = mockTestUserService;

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      providers: [
        { provide: Router, useValue: mockRouter },
        { provide: SessionService, useValue: mockSessionService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: UserService, useValue: mockUserService }
      ]
    }).compileComponents();
    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    compiled = fixture.nativeElement;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should fetch user data on init', () => {
      component.ngOnInit();
      expect(mockUserService.getById).toHaveBeenCalledWith('1');
      expect(component.user).toEqual(mockDataTestUserNotAdmin);
    });
  });

  describe('Data user', () => {
    it('should display user information correctly', () => {
      expect(compiled.querySelector('h1')?.textContent).toContain('User information');
      expect(compiled.querySelector('[data-cy="name"]')?.textContent).toContain(`Name: ${mockDataTestUserNotAdmin.firstName} ${mockDataTestUserNotAdmin.lastName}`);
      expect(compiled.querySelector('[data-cy="email"]')?.textContent).toContain(`Email: ${mockDataTestUserNotAdmin.email}`);
      expect(compiled.querySelector('[data-cy="deleteBtn"]')).toBeTruthy();
    });

    it('should display dates correctly', () => {
      mockUserService.getById.mockReturnValue(of(mockDataTestUserNotAdmin));
      fixture.detectChanges();

      const datePipe = new DatePipe('en-US');
      const createdAt = datePipe.transform(mockDataTestUserNotAdmin.createdAt, 'longDate');
      const updatedAt = datePipe.transform(mockDataTestUserNotAdmin.updatedAt, 'longDate');

      expect(compiled.querySelector('[data-cy="created-at"]')?.textContent).toContain(`Create at: ${createdAt}`);
      expect(compiled.querySelector('[data-cy="updated-at"]')?.textContent).toContain(`Last update: ${updatedAt}`);
    });
  });

  describe('Admin user detail', () => {
    it('should not display delete button for admin user', () => {
      mockUserService.getById.mockReturnValue(of(mockDataTestUserIsAdmin));
      component.ngOnInit();
      fixture.detectChanges();
      expect(compiled.querySelector('[data-cy="deleteBtn"]')).toBeFalsy();
    });

    it('should display admin message for admin user', () => {
      mockUserService.getById.mockReturnValue(of(mockDataTestUserIsAdmin));

      component.ngOnInit();
      fixture.detectChanges();

      expect(compiled.querySelector('[data-cy="admin-message"]')?.textContent).toContain('You are admin');
    });
  });

  describe('Go back action', () => {
    it('should call window.history.back', () => {
      const spyHistoryBack = jest.spyOn(window.history, 'back');
      component.back();
      expect(spyHistoryBack).toHaveBeenCalled();
    });
  });

  describe('Delete action', () => {
    it('should delete user account', () => {
      component.delete();

      expect(mockUserService.delete).toHaveBeenCalledWith('1');
      expect(mockMatSnackBar.open).toHaveBeenCalledWith(
        'Your account has been deleted !',
        'Close',
        { duration: 3000 }
      );
      expect(mockSessionService.logOut).toHaveBeenCalled();
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
    });
  });

});
