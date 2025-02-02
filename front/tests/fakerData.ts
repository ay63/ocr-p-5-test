import {faker} from "@faker-js/faker";

// Standard User
export const fakeFirstName=  faker.person.firstName();
export const fakeLastName=  faker.person.lastName().toUpperCase();

export const fakeEmail: string = faker.internet.email({
  firstName:fakeFirstName,
  lastName:fakeLastName
}).toString();

export const fakeToken = faker.internet.jwt()

// ADMIN
export const fakeAdminFirstName=  faker.person.firstName();
export const fakeAdminLastName=  faker.person.lastName();

export const fakeAdminEmail: string = faker.internet.email({
  firstName:fakeAdminFirstName,
  lastName:fakeAdminLastName.toUpperCase()
}).toString();

// Password
export const fakeValidPassword=  faker.internet.password({length:10});

// Session 1
export const fakeSessionName = faker.word.words(6);
export const fakeSessionDescription = faker.word.words(20);

// Session 2
export const fakeSecondSessionName = faker.word.words(6);
export const fakeSecondSessionDescription = faker.word.words(20);

// Teacher
export const fakeTeacherLastName=  faker.person.lastName();
export const fakeTeacherFirstName=  faker.person.firstName();

