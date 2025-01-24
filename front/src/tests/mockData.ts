import {User} from "../app/interfaces/user.interface";
import {SessionInformation} from "../app/interfaces/sessionInformation.interface";
import {Session} from "../app/features/sessions/interfaces/session.interface";
import {Teacher} from "../app/interfaces/teacher.interface";

export const mockDataTestUserNotAdmin: User = {
  id: 1,
  email: 'john.doe@fake.com',
  lastName: 'DOE',
  firstName: 'John',
  admin: false,
  password: 'password',
  createdAt: new Date(),
  updatedAt: new Date(),
};

export const mockDataTestUserIsAdmin: User = {
  id: 1,
  email: 'john.admin@fake.com',
  lastName: 'Admin',
  firstName: 'John',
  admin: true,
  password: 'password',
  createdAt: new Date(),
  updatedAt: new Date(),
};

export const mockDataTestSessionInformationNotAdmin: SessionInformation = {
  id: 1,
  lastName: 'DOE',
  firstName: 'John',
  admin: false,
  token: "fakeToken",
  type: "Bearer",
  username: "name"
};

export const mockDataTestSessionInformationIsAdmin: SessionInformation = {
  id: 1,
  lastName: 'DOE',
  firstName: 'John',
  admin: true,
  token: "fakeToken",
  type: "Bearer",
  username: "name"
};


export const mockDataTestSessionInformation = {
  admin: false,
  firstName: "john",
  id: 0,
  lastName: "doe",
  type: "Bearer",
  username: "joe",
  token: "fakeToken",
};

export const mockDataTestSessions: Session[] = [
  {
    id: 2,
    name: 'Yoga Morning',
    date: new Date('2024-01-15'),
    description: 'Morning yoga session',
    users: [1],
    teacher_id: 1
  },
  {
    id: 2,
    name: 'Pilates Evening',
    date: new Date('2024-01-20'),
    description: 'Evening pilates class',
    users: [1],
    teacher_id: 1
  }
];


export const mockDataTestSession: Session = {
  id: 1,
  name: 'Test Session',
  description: 'test',
  teacher_id: 1,
  users: [1],
  date: new Date(),
  createdAt: new Date(),
  updatedAt: new Date(),
};

export const mockDataTestTeacher: Teacher = {
  id: 1,
  lastName: "doe",
  firstName: "john",
  createdAt: new Date(),
  updatedAt: new Date(),
};
