/// <reference types="cypress" />


Cypress.Commands.add("getByDataCy", (selector, ...args) => {
  return cy.get(`[data-cy=${selector}]`, ...args)
})

Cypress.Commands.add("initUserDataAndLoginIn", (admin: boolean = false) => {
  cy.visit('/login')

  cy.intercept('POST', '/api/auth/login', {
    body: {
      id: 1,
      username: 'userName',
      firstName: 'firstName',
      lastName: 'lastName',
      admin: admin
    },
  }).as('postLogin')

  cy.intercept('GET', '/api/session', {
    body: [
      {
        "id": 1,
        "name": "test",
        "date": "2025-01-22T00:00:00.000+00:00",
        "teacher_id": 1,
        "description": "test",
        "users": [],
        "createdAt": "2025-01-23T09:12:33",
        "updatedAt": "2025-01-23T09:12:33"
      },
    ],
    statusCode: 200
  }).as('getSession')

  cy.intercept('GET', '/api/user/1', {
    body: {
      "id": 1,
      "email": "email0.6009744576622176@gmail.com",
      "lastName": "lastName",
      "firstName": "firstName",
      "admin": admin,
      "createdAt": "2025-01-22T14:31:48",
      "updatedAt": "2025-01-22T14:31:48"
    }
  }).as('getUser')

  cy.intercept('GET', '/api/teacher', {
    body: [
      {
        "id": 1,
        "lastName": "DELAHAYE",
        "firstName": "Margot",
        "createdAt": "2025-01-20T10:26:21",
        "updatedAt": "2025-01-20T10:26:21"
      },
    ],
    statusCode: 200
  }).as('getAllTeachers')

  cy.intercept('GET', '/api/teacher/1', {
    body:
      {
        "id": 1,
        "lastName": "DELAHAYE",
        "firstName": "Margot",
        "createdAt": "2025-01-20T10:26:21",
        "updatedAt": "2025-01-20T10:26:21"
      },
    statusCode: 200
  }).as('getTeacher')

  cy.intercept('POST', '/api/session/1/participate/1', {
    body: {},
    statusCode: 200
  }).as('postParticipate');

  cy.intercept('DELETE', '/api/session/1/participate/1', {
    body: {},
    statusCode: 200
  }).as('deleteParticipate')

  cy.intercept('POST', '/sessions/create', {
    body: {
      "name": "session 1",
      "date": "2012-01-01",
      "teacher_id": 1,
      "users": [],
      "description": "my description"
    },
    statusCode: 200
  }).as('postSession')


  cy.intercept('GET', '/api/session/1', {
    body: {
      "id": 1,
      "name": "test",
      "date": "2025-01-22T00:00:00.000+00:00",
      "teacher_id": 1,
      "description": "test",
      "users": [],
      "createdAt": "2025-01-23T09:12:33",
      "updatedAt": "2025-01-23T09:12:33"
    },
  }).as('getSession')


  cy.get('input[formcontrolname=email]').type('user@email.com');
  cy.get('input[formcontrolname=password]').type('password{enter}{enter}')
  cy.url().should('include', '/sessions')
})
