import * as cypress from "cypress";

beforeEach(() => {
  cy.visit('/login')

  cy.intercept('POST', '/api/auth/login', {
    body: {
      id: 1,
      username: 'userName',
      firstName: 'firstName',
      lastName: 'lastName',
      admin: true
    },
    statusCode: 200
  })

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
  })

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
  })

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
  })

  cy.get('input[formcontrolname=email]').type(Cypress.env('adminEmail'));
  cy.get('input[formcontrolname=password]').type(`${Cypress.env('adminPassword')}{enter}{enter}`)

  cy.url().should('include', '/sessions')
  cy.getByDataCy("create-session").should('exist')
})


describe('Admin session', () => {

  describe('Session create', () => {
    it('Should be able to create a session', () => {
      cy.getByDataCy("create-session").click()
      cy.url().should('include', '/create')

      cy.intercept('POST', '/sessions/create', {
        body: {
          "name": "session 1",
          "date": "2012-01-01",
          "teacher_id": 1,
          "users": null,
          "description": "my description"
        },
        statusCode: 200
      })

      cy.getByDataCy("name").type('test')
      cy.getByDataCy("date").type('2025-01-22')
      cy.getByDataCy("teacher-select").click();
      cy.getByDataCy("teacher-option").should('have.length.greaterThan', 0);
      cy.getByDataCy("teacher-option").first().click()
      cy.getByDataCy("description").type('a'.repeat(20))

      cy.getByDataCy("saveBtn").click()
      cy.url().should('include', '/sessions')
    })
  })

  it('Should be able to delete a session', () => {
    cy.intercept('DELETE', '/api/session', {
      statusCode: 200
    })

    cy.get('[data-cy^="detail-session-"]').first().then((btn) => {
      const dataCy = btn.attr('data-cy');
      const match = dataCy.match(/detail-session-(\d+)/);

      if (match) {
        const id = match[1];
        cy.wrap(btn).click();
        cy.url().should('include', `/detail/${id}`);
        cy.get(`[data-cy="delete-${id}"]`).click();
      }
    });

  })

  it('Should be able to update a session', () => {
    cy.get('[data-cy^="edit-session-"]').first().then((btn) => {
      const dataCy = btn.attr('data-cy');
      const match = dataCy.match(/edit-session-(\d+)/);

      if (match) {
        const id = match[1];
        cy.wrap(btn).click();
        cy.url().should('include', `/update/${id}`);
        cy.getByDataCy("description").clear()
        cy.getByDataCy("description").type("update description");
        cy.getByDataCy("saveBtn").click()
      }
    });

    cy.url().should('include', '/sessions')
  })

  describe('Session create errors form on create and update ', () => {
    it('Should not be able to create a session missing form data', () => {
      cy.getByDataCy("create-session").click()
      cy.url().should('include', '/create')
      cy.getByDataCy("name").invoke('val', '')
      cy.getByDataCy("date").invoke('val', '')
      cy.getByDataCy("description").invoke('val', '')
      cy.getByDataCy("saveBtn").should('be.disabled')

      cy.getByDataCy("teacher-select").should('have.class', 'mat-select-required')
      cy.getByDataCy("name").should('have.attr', 'required')
      cy.getByDataCy("date").should('have.attr', 'required')
      cy.getByDataCy("description").should('have.attr', 'required')

      cy.url().should('include', '/create')
    })

    it('should failed description max length 2000', () => {
      cy.getByDataCy("create-session").click()
      cy.getByDataCy("name").type('test')
      cy.getByDataCy("date").type('2025-01-22')
      cy.getByDataCy("teacher-select").click();
      cy.getByDataCy("teacher-option").should('have.length.greaterThan', 0);
      cy.getByDataCy("teacher-option").first().click()
      cy.getByDataCy("description").type('a'.repeat(2001))
      cy.getByDataCy("description").should('have.class', 'ng-invalid')
      cy.getByDataCy("saveBtn").should('be.disabled')
    })
  })

})
