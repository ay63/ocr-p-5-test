import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ListComponent } from './list.component';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { RouterTestingModule } from '@angular/router/testing';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FlexLayoutModule } from '@angular/flex-layout';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';
import { expect } from "@jest/globals";
import {
  mockTestSessionApiService,
  mockTestSessionService,
} from "../../../../../../tests/mock";
import {
  mockDataTestSessionInformationIsAdmin,
  mockDataTestSessionInformationNotAdmin,
  mockDataTestSessions
} from "../../../../../../tests/mockData";
import { fakeSessionDescription } from "../../../../../../tests/fakerData";

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockSessionApiService: jest.Mocked<SessionApiService>;


  beforeEach(async () => {
    mockSessionService = mockTestSessionService
    mockSessionApiService = mockTestSessionApiService

    mockSessionApiService.all = jest.fn().mockReturnValue(of(mockDataTestSessions));

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        MatCardModule,
        MatButtonModule,
        MatIconModule,
        FlexLayoutModule
      ],
      declarations: [ListComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
  });

  describe('Session List Rendering', () => {
    it('should create the component', () => {
      component = fixture.componentInstance;
      fixture.detectChanges();
      expect(component).toBeTruthy();
    });

    it('should render all sessions', () => {
      mockSessionService.sessionInformation = mockDataTestSessionInformationNotAdmin;

      component = fixture.componentInstance;
      fixture.detectChanges();

      const sessionCards = fixture.debugElement.queryAll(By.css('mat-card.item'));
      expect(sessionCards.length).toBe(2);
    });

    it('should display correct session details', () => {
      mockSessionService.sessionInformation = mockDataTestSessionInformationNotAdmin;
      component = fixture.componentInstance;
      fixture.detectChanges();

      const sessionTitles = fixture.debugElement.queryAll(By.css('mat-card-title'));
      const sessionDescriptions = fixture.debugElement.queryAll(By.css('mat-card-content p'));

      expect(sessionTitles[0].nativeElement.textContent).toContain('Session available');
      expect(sessionDescriptions[0].nativeElement.textContent).toContain(fakeSessionDescription);
    });
  });

  describe('Button Visibility', () => {
    it('should show create and edit buttons for admin user', () => {
      mockSessionService.sessionInformation = mockDataTestSessionInformationIsAdmin;
      fixture = TestBed.createComponent(ListComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();

      const createButton = fixture.debugElement.query(By.css('button[data-cy="create-session"]'));
      const editButtons = fixture.debugElement.queryAll(By.css('button[data-cy^="edit-session-"]'));

      expect(createButton).toBeTruthy();
      expect(editButtons.length).toBe(2);
    });

    it('should hide create and edit buttons for regular user', () => {
      mockSessionService.sessionInformation = mockDataTestSessionInformationNotAdmin;
      fixture = TestBed.createComponent(ListComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();

      const createButton = fixture.debugElement.query(By.css('button[data-cy="create-session"]'));
      const editButtons = fixture.debugElement.queryAll(By.css('button[data-cy^="edit-session-"]'));

      expect(createButton).toBeFalsy();
      expect(editButtons.length).toBe(0);
    });

    it('should have detail buttons for all users', () => {
      mockSessionService.sessionInformation = mockDataTestSessionInformationNotAdmin;
      fixture = TestBed.createComponent(ListComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();

      const detailButtons = fixture.debugElement.queryAll(By.css('button[data-cy^="detail-session-"]'));
      expect(detailButtons.length).toBe(2);
    });
  });
});
