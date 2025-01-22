import { TestBed, ComponentFixture } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { expect } from '@jest/globals';
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
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });


  it('should create', () => {
    expect(component).toBeTruthy();
  });

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
});
