import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {RouterTestingModule} from '@angular/router/testing';
import {expect} from '@jest/globals';
import {SessionService} from 'src/app/services/session.service';
import {LoginComponent} from './login.component';
import {of, throwError} from "rxjs";
import {LoginRequest} from "../../interfaces/loginRequest.interface";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {
  authTestServiceMock,
  mockTestRouter
} from "../../../../../../tests/mock";
import {mockDataTestSessionInformationNotAdmin} from "../../../../../../tests/mockData";
import {By} from "@angular/platform-browser";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceMock: jest.Mocked<AuthService>;
  let routerMock: jest.Mocked<Router>;

  beforeEach(async () => {

    authServiceMock = authTestServiceMock
    routerMock = mockTestRouter

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        SessionService,
        {provide: AuthService, useValue: authServiceMock},
        {provide: Router, useValue: routerMock},
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule
      ]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect to "/sessions" after successful submit', () => {
    authServiceMock.login.mockReturnValue(of(mockDataTestSessionInformationNotAdmin));

    const loginReq: LoginRequest = {
      email: 'test@example.com',
      password: 'password123',
    }

    component.form.setValue(loginReq);
    component.submit();

    expect(authServiceMock.login).toHaveBeenCalledWith(loginReq);
    expect(routerMock.navigate).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBe(false);
  });

  it('should set onError to true when credential are invalid and display error message', () => {
    authServiceMock.login.mockReturnValue(throwError(() => new Error('Invalid credentials')));

    const loginReq: LoginRequest = {
      email: 'test@example.com',
      password: 'invalid',
    }

    component.form.setValue(loginReq);
    component.submit();

    fixture.detectChanges();
    
    const errorMessage = fixture.debugElement.query(By.css('.error'));

    expect(errorMessage).toBeTruthy();
    expect(errorMessage.nativeElement.textContent).toContain('An error occurred');
    expect(component.onError).toBe(true);
  });

});
