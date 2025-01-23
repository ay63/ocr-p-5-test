import {ComponentFixture, TestBed} from '@angular/core/testing';
import {Router} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
import {of} from 'rxjs';
import {MeComponent} from './me.component';
import {SessionService} from "../../services/session.service";
import {UserService} from "../../services/user.service";
import {User} from "../../interfaces/user.interface";
import {expect} from "@jest/globals";
import {DatePipe} from "@angular/common";

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockRouter: jest.Mocked<Router>;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockMatSnackBar: jest.Mocked<MatSnackBar>;
  let mockUserService: jest.Mocked<UserService>;
  let compiled: HTMLElement;

  const mockUser: User = {
    id: 1,
    email: "john.doe@fake.com",
    lastName: "DOE",
    firstName: "John",
    admin: false,
    password: "password",
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  beforeEach(() => {
    mockRouter = {
      navigate: jest.fn()
    } as unknown as jest.Mocked<Router>;

    mockSessionService = {
      sessionInformation: {id: 1},
      logOut: jest.fn()
    } as unknown as jest.Mocked<SessionService>;

    mockMatSnackBar = {
      open: jest.fn()
    } as unknown as jest.Mocked<MatSnackBar>;

    mockUserService = {
      getById: jest.fn().mockReturnValue(of(mockUser)),
      delete: jest.fn().mockReturnValue(of(undefined))
    } as unknown as jest.Mocked<UserService>;

    TestBed.configureTestingModule({
      declarations: [MeComponent],
      providers: [
        {provide: Router, useValue: mockRouter},
        {provide: SessionService, useValue: mockSessionService},
        {provide: MatSnackBar, useValue: mockMatSnackBar},
        {provide: UserService, useValue: mockUserService}
      ]
    });

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    compiled = fixture.nativeElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should fetch user data on init', () => {
      component.ngOnInit();

      expect(mockUserService.getById).toHaveBeenCalledWith('1');
      expect(component.user).toEqual(mockUser);
    });
  });

  describe('data user', () => {
    it('should display user information correctly', () => {
      expect(compiled.querySelector('h1')?.textContent).toContain('User information');
      expect(compiled.querySelector('[data-cy="name"]')?.textContent).toContain('Name: John DOE');
      expect(compiled.querySelector('[data-cy="email"]')?.textContent).toContain('Email: john.doe@fake.com');
      expect(compiled.querySelector('[data-cy="deleteBtn"]')).toBeTruthy();
    });

    it('should format dates correctly', () => {
      mockUserService.getById.mockReturnValue(of(mockUser));
      fixture.detectChanges();

      const datePipe = new DatePipe('en-US');
      const createdAt = datePipe.transform(mockUser.createdAt, 'longDate');
      const updatedAt = datePipe.transform(mockUser.updatedAt, 'longDate');

      expect(compiled.querySelector('[data-cy="created-at"]')?.textContent).toContain(`Create at: ${createdAt}`);
      expect(compiled.querySelector('[data-cy="updated-at"]')?.textContent).toContain(`Last update: ${updatedAt}`);
    });
  })

  describe('Admin user detail', () => {
    it('should display admin message if user is admin', () => {
      mockUser.admin = true;
      mockUserService.getById.mockReturnValue(of(mockUser));
      component.ngOnInit();
      fixture.detectChanges();
      expect(compiled.querySelector('[data-cy="deleteBtn"]')).toBeFalsy();
    });

    it('should display admin message if user is admin', () => {
      mockUser.admin = true;
      mockUserService.getById.mockReturnValue(of(mockUser));

      component.ngOnInit();
      fixture.detectChanges();

      expect(compiled.querySelector('[data-cy="admin-message"]')?.textContent).toContain('You are admin');
    });

  })

  describe('back', () => {
    it('should call window.history.back', () => {
      const spyHistoryBack = jest.spyOn(window.history, 'back');
      component.back();
      expect(spyHistoryBack).toHaveBeenCalled();
    });
  });

  describe('delete', () => {
    it('should delete user account', () => {
      component.delete();

      expect(mockUserService.delete).toHaveBeenCalledWith('1');
      expect(mockMatSnackBar.open).toHaveBeenCalledWith(
        'Your account has been deleted !',
        'Close',
        {duration: 3000}
      );
      expect(mockSessionService.logOut).toHaveBeenCalled();
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
    });
  });
});
