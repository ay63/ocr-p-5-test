import {TestBed, ComponentFixture} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {of, throwError} from 'rxjs';
import {RegisterComponent} from './register.component';
import {AuthService} from '../../services/auth.service';
import {expect} from '@jest/globals';
import {RegisterRequest} from "../../interfaces/registerRequest.interface";

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceMock: any;
  let routerMock: any;

  beforeEach(async () => {
    authServiceMock = {
      register: jest.fn(),
    };

    routerMock = {
      navigate: jest.fn(),
    };

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [ReactiveFormsModule],
      providers: [
        {provide: AuthService, useValue: authServiceMock},
        {provide: Router, useValue: routerMock},
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  describe('register', () => {
    it('should redirect to /login after successful submit', () => {
      authServiceMock.register.mockReturnValue(of(undefined));
      const formValue: RegisterRequest = {
        email: 'test@example.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'password123',
      }

      component.form.setValue(formValue);
      component.submit();

      expect(authServiceMock.register).toHaveBeenCalledWith(formValue);
      expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
    });

    it('should set onError to true if an error occurs during submit', () => {
      authServiceMock.register.mockReturnValue(throwError(() => new Error()));
      component.form.setValue({
        email: 'test@example.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'password123',
      });

      component.submit();
      expect(component.onError).toBe(true);
    });
  })

  describe('form validation', () => {
    it('should require all fields', () => {
      expect(component.form.get('email')?.hasError('required')).toBeTruthy();
      expect(component.form.get('firstName')?.hasError('required')).toBeTruthy();
      expect(component.form.get('lastName')?.hasError('required')).toBeTruthy();
      expect(component.form.get('password')?.hasError('required')).toBeTruthy();
    });

    it('should validate email format', () => {
      component.form.get('email')?.setValue('invalid-email');
      expect(component.form.get('email')?.hasError('email')).toBeTruthy();

      component.form.get('email')?.setValue('valid@example.com');
      expect(component.form.get('email')?.hasError('email')).toBeFalsy();
    });

    it('should validate firstName and lastName length', () => {
      const shortName = 'a';
      const longName = 'a'.repeat(21);

      component.form.get('firstName')?.setValue(shortName);
      expect(component.form.get('firstName')?.hasError('minlength')).toBeTruthy();

      component.form.get('firstName')?.setValue(longName);
      expect(component.form.get('firstName')?.hasError('maxlength')).toBeTruthy();

      component.form.get('lastName')?.setValue(shortName);
      expect(component.form.get('lastName')?.hasError('minlength')).toBeTruthy();

      component.form.get('lastName')?.setValue(longName);
      expect(component.form.get('lastName')?.hasError('maxlength')).toBeTruthy();
    });

    it('should validate password length', () => {
      const shortPassword = 'a';
      const longPassword = 'a'.repeat(41);

      component.form.get('password')?.setValue(shortPassword);
      expect(component.form.get('password')?.hasError('minlength')).toBeTruthy();

      component.form.get('password')?.setValue(longPassword);
      expect(component.form.get('password')?.hasError('maxlength')).toBeTruthy();
    });
  });
});
