/// <reference types="cypress" />

Cypress.Commands.add("getByDataCy", (selector, ...args) => {
  return cy.get(`[data-cy=${selector}]`, ...args)
})


Cypress.Commands.add("loginUser", (admin: boolean = false) => {
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

  cy.intercept('GET', '/api/user/1', {
    body: {
      "id": 1,
      "email": "email@test.com",
      "lastName": "lastName",
      "firstName": "firstName",
      "admin": admin,
      "createdAt": "2025-01-22T14:31:48",
      "updatedAt": "2025-01-22T14:31:48"
    }
  }).as('getUser')


  cy.get('input[formcontrolname=email]').type('user@email.com');
  cy.get('input[formcontrolname=password]').type('password{enter}{enter}')
  cy.url().should('include', '/sessions')
});

Cypress.Commands.add("sessionsInterceptor", () => {
  cy.intercept('GET', '/api/session', {
    statusCode: 200,
    body: [
      {
        id: 1,
        name: "Yoga session",
        date: "2025-01-22T00:00:00.000+00:00",
        teacher_id: 1,
        description: "Yoga session description",
        users: [],
        createdAt: "2025-01-23T09:12:33",
        updatedAt: "2025-01-23T09:12:33"
      },
      {
        id: 2,
        name: "Pilate session",
        date: "2025-01-22T00:00:00.000+00:00",
        teacher_id: 1,
        description: "Pilate session description",
        users: [],
        createdAt: "2025-01-23T09:12:33",
        updatedAt: "2025-01-23T09:12:33"
      }
    ]
  }).as('getSessions');

});

Cypress.Commands.add("teacherInterceptor", () => {
  cy.intercept('GET', '/api/teacher', {
    body: [
      {
        "id": 1,
        "lastName": "DELAHAYE",
        "firstName": "Margot",
        "createdAt": "2025-01-20T10:26:21",
        "updatedAt": "2025-01-20T10:26:21"
      },
      {
        "id": 2,
        "lastName": "JOHN",
        "firstName": "Doe",
        "createdAt": "2025-01-20T10:26:21",
        "updatedAt": "2025-01-20T10:26:21"
      }
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
});