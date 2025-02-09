import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { SessionService } from "./services/session.service";
import { Router } from "@angular/router";
import { of } from "rxjs";
import { mockTestRouter, mockTestSessionApiService, mockTestSessionService } from 'tests/mock';


describe('AppComponent', () => {
  let fixture;
  let app: AppComponent;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockRouter: jest.Mocked<Router>;
  beforeEach(async () => {

    mockSessionService = mockTestSessionService;
    mockTestSessionService.$isLogged = jest.fn().mockReturnValue(of(true))

    mockRouter = mockTestRouter;

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter },
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    app = fixture.componentInstance;
  });

  it('should create the app', () => {
    expect(app).toBeTruthy();
  });

  it('should logout', () => {
    app.logout();
    expect(mockSessionService.logOut).toBeCalled();
    expect(mockRouter.navigate).toBeCalledWith(['']);
  })

});
