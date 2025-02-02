import {of} from 'rxjs';
import {jest} from '@jest/globals';
import {ActivatedRoute, Router} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
import {SessionService} from "../src/app/services/session.service";
import {UserService} from "../src/app/services/user.service";
import {AuthService} from "../src/app/features/auth/services/auth.service";
import {SessionApiService} from "../src/app/features/sessions/services/session-api.service";
import {TeacherService} from "../src/app/services/teacher.service";
import {mockDataTestSession, mockDataTestTeacher, mockDataTestUserNotAdmin} from "./mockData";

export const mockTestRouter: jest.Mocked<Router> = {
  navigate: jest.fn(),
  get url(): string {
    return '';
  }
} as unknown  as jest.Mocked<Router>;

export const mockTestSessionService: jest.Mocked<SessionService> = {
    sessionInformation: {id: 1, admin: false},
    logOut: jest.fn(),
} as unknown as jest.Mocked<SessionService>;

export const mockTestMatSnackBar: jest.Mocked<MatSnackBar> = {
    open: jest.fn(),
} as unknown as jest.Mocked<MatSnackBar>;

export const mockTestUserService: jest.Mocked<UserService> = {
    getById: jest.fn().mockReturnValue(of(mockDataTestUserNotAdmin)),
    delete: jest.fn().mockReturnValue(of(undefined)),
} as unknown as jest.Mocked<UserService>;

export const authTestServiceMock = {
    login: jest.fn(),
    register: jest.fn(),
} as unknown as jest.Mocked<AuthService>;

export const mockTestRoute = {
    snapshot: {
        paramMap: new Map().set('id', '1')
    }
} as unknown as jest.Mocked<ActivatedRoute>;

export const mockTestSessionApiService = {
    detail: jest.fn().mockReturnValue(of(mockDataTestSession)),
    delete: jest.fn().mockReturnValue(of({})),
    participate: jest.fn().mockReturnValue(of({})),
    unParticipate: jest.fn().mockReturnValue(of({})),
    update: jest.fn().mockReturnValue(of({})),
    all: jest.fn(),
    create: jest.fn().mockReturnValue(of({})),
} as unknown as jest.Mocked<SessionApiService>;

export const mockTestTeacherService = {
    detail: jest.fn().mockReturnValue(of(mockDataTestTeacher)),
    all: jest.fn()
} as unknown as jest.Mocked<TeacherService>;
