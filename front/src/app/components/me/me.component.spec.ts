import {ComponentFixture, TestBed} from '@angular/core/testing';
import {Router} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
import {of} from 'rxjs';
import {MeComponent} from './me.component';
import {SessionService} from "../../services/session.service";
import {UserService} from "../../services/user.service";
import {User} from "../../interfaces/user.interface";
import {expect} from "@jest/globals";

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockRouter: jest.Mocked<Router>;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockMatSnackBar: jest.Mocked<MatSnackBar>;
  let mockUserService: jest.Mocked<UserService>;

  const mockUser: User = {
    id: 1,
    email: "string",
    lastName: "string",
    firstName: "string",
    admin: false,
    password: "string",
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

  describe('back', () => {
    it('should call window.history.back', () => {
      const spyHistoryBack = jest.spyOn(window.history, 'back');
      component.back();
      expect(spyHistoryBack).toHaveBeenCalled();
    });
  });

  describe('delete', () => {
    it('should delete user account and handle success properly', () => {
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
