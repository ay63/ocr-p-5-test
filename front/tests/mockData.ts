import {User} from "../src/app/interfaces/user.interface";
import {SessionInformation} from "../src/app/interfaces/sessionInformation.interface";
import {Session} from "../src/app/features/sessions/interfaces/session.interface";
import {Teacher} from "../src/app/interfaces/teacher.interface";
import * as fakeData from "./fakerData";
import {fakeSecondSessionDescription, fakeSecondSessionName, fakeTeacherFirstName, fakeToken} from "./fakerData";

export const mockDataTestUserNotAdmin: User = {
  id: 1,
  email: fakeData.fakeEmail,
  lastName: fakeData.fakeLastName,
  firstName: fakeData.fakeFirstName,
  admin: false,
  password: fakeData.fakeValidPassword,
  createdAt: new Date(),
  updatedAt: new Date(),
};

export const mockDataTestUserIsAdmin: User = {
  id: 1,
  email: fakeData.fakeAdminEmail,
  lastName: fakeData.fakeAdminLastName,
  firstName: fakeData.fakeAdminFirstName,
  admin: true,
  password: fakeData.fakeValidPassword,
  createdAt: new Date(),
  updatedAt: new Date(),
};

export const mockDataTestSessionInformationNotAdmin: SessionInformation = {
  id: 1,
  lastName: fakeData.fakeLastName,
  firstName: fakeData.fakeFirstName,
  admin: false,
  token: fakeData.fakeToken,
  type: "Bearer",
  username: fakeData.fakeEmail
};

export const mockDataTestSessionInformationIsAdmin: SessionInformation = {
  id: 1,
  lastName: fakeData.fakeAdminLastName,
  firstName: fakeData.fakeAdminFirstName,
  admin: true,
  token: fakeData.fakeToken,
  type: "Bearer",
  username: fakeData.fakeAdminEmail
};

export const mockDataTestSessions: Session[] = [
  {
    id: 1,
    name: fakeData.fakeSessionName,
    date: new Date('2024-01-15'),
    description: fakeData.fakeSessionDescription,
    users: [1],
    teacher_id: 1
  },
  {
    id: 2,
    name: fakeData.fakeSecondSessionName,
    date: new Date('2024-01-20'),
    description:  fakeData.fakeSecondSessionDescription,
    users: [1],
    teacher_id: 1
  }
];

export const mockDataTestSession: Session = {
  id: 1,
  name: fakeData.fakeSessionName,
  description:  fakeData.fakeSessionDescription,
  teacher_id: 1,
  users: [1],
  date: new Date(),
  createdAt: new Date(),
  updatedAt: new Date(),
};

export const mockDataTestTeacher: Teacher = {
  id: 1,
  lastName: fakeData.fakeTeacherLastName,
  firstName: fakeData.fakeTeacherFirstName,
  createdAt: new Date(),
  updatedAt: new Date(),
};

export const mockDataTestListTeachers: Teacher[] = [
  {
    id: 1,
    lastName: fakeData.fakeTeacherLastName,
    firstName: fakeData.fakeTeacherFirstName,
    createdAt: new Date(),
    updatedAt: new Date(),
  },
  {
    id: 2,
    lastName: fakeData.fakeTeacherLastName,
    firstName: fakeData.fakeTeacherFirstName,
    createdAt: new Date(),
    updatedAt: new Date(),
  }
]
