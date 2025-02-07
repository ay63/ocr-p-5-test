beforeEach(() => {
  cy.sessionsInterceptor()
  cy.loginUser(true)
  cy.teacherInterceptor()
})

describe('Admin session', () => {
  describe('Session create', () => {
    it('Should be able to create a session', () => {
      cy.intercept('POST', '/api/session', {
        statusCode: 200,
        body: {
          "id": 1,
          "name": "Yoga session",
          "date": "2025-01-22T00:00:00.000+00:00",
          "teacher_id": 1,
          "description": "Yoga session description",
          "users": [],
          "createdAt": "2025-01-23T09:12:33",
          "updatedAt": "2025-01-23T09:12:33"
        }
      }).as('createSession')

      cy.intercept('GET', '/api/session', {
        statusCode: 200,
        body: [{
          "id": 1,
          "name": "Yoga session",
          "date": "2025-01-22T00:00:00.000+00:00",
          "teacher_id": 1,
          "description": "Yoga session description",
          "users": [],
          "createdAt": "2025-01-23T09:12:33",
          "updatedAt": "2025-01-23T09:12:33"
        }]
      }).as('getCreatedSessions')

      cy.getByDataCy("create-session").click()
      cy.url().should('include', '/create')

      cy.getByDataCy("name").type('Yoga session')
      cy.getByDataCy("date").type('2025-01-22')
      cy.getByDataCy("teacher-select").click();
      cy.getByDataCy("teacher-option").should('have.length.greaterThan', 0);
      cy.getByDataCy("teacher-option").first().click()
      cy.getByDataCy("description").type('a'.repeat(20))
      cy.getByDataCy("saveBtn").click()

      cy.url().should('eq', Cypress.config('baseUrl') + 'sessions')
      cy.getByDataCy("detail-session-1").click();
    })
  })

  describe('Session delete', () => {
    it('Should be able to delete a session', () => {
      cy.intercept('DELETE', '/api/session/1', {
        statusCode: 200
      }).as('deleteSession');

      cy.intercept('GET', '/api/session', {
        statusCode: 200,
        body: [
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
      }).as('getSessionsAfterDelete');

      cy.intercept('GET', '/api/session/1', {
        body: {
          id: 1,
          name: "Yoga session",
          date: "2025-01-22T00:00:00.000+00:00",
          teacher_id: 1,
          description: "Yoga session description",
          users: [],
          createdAt: "2025-01-23T09:12:33",
          updatedAt: "2025-01-23T09:12:33"
        },
      }).as('getSessionById')

      cy.getByDataCy("detail-session-1").click();
      cy.wait('@getSessionById');

      cy.getByDataCy("delete-session-1").click();
      cy.wait('@getSessionsAfterDelete');
      cy.wait('@deleteSession');

      cy.url().should('eq', Cypress.config('baseUrl') + 'sessions');
      cy.getByDataCy("detail-session-1").should('not.exist');
      cy.getByDataCy("detail-session-2").should('exist');
    });
  });

  describe('Session update', () => {
    it('Should be able to update a session', () => {
      cy.intercept('GET', '/api/session/1', {
        body: {
          id: 1,
          name: "Yoga session",
          date: "2025-01-22T00:00:00.000+00:00",
          teacher_id: 1,
          description: "Yoga session description",
          users: [],
          createdAt: "2025-01-23T09:12:33",
          updatedAt: "2025-01-23T09:12:33"
        },
      }).as('getSessionById')

      cy.intercept('PUT', '/api/session/1', {
        statusCode: 200,
        body: {
          "id": 1,
          "name": "Update Yoga session",
          "date": "2025-01-22T00:00:00.000+00:00",
          "teacher_id": 1,
          "description": "update description",
          "users": [],
          "createdAt": "2025-01-23T09:12:33",
          "updatedAt": "2025-01-23T09:12:33"
        }
      }).as('updateSession');

      cy.intercept('GET', '/api/session', {
        statusCode: 200,
        body: [
          {
            "id": 1,
            "name": "Update Yoga session",
            "date": "2025-01-22T00:00:00.000+00:00",
            "teacher_id": 1,
            "description": "update description",
            "users": [],
            "createdAt": "2025-01-23T09:12:33",
            "updatedAt": "2025-01-23T09:12:33"
          }
        ]
      }).as('getSessions');

      cy.getByDataCy("edit-session-1").click();
      cy.wait('@getSessionById')
      cy.url().should('include', `/update/1`);

      cy.getByDataCy("description").clear()
      cy.getByDataCy("description").type("update description");
      cy.getByDataCy("name").clear()
      cy.getByDataCy("name").type("Update Yoga session");

      cy.getByDataCy("saveBtn").click()

      cy.wait('@getSessions');
      cy.wait('@updateSession');

      cy.url().should('eq', Cypress.config('baseUrl') + 'sessions')
      cy.getByDataCy("session-description").should('contain.text', 'update description');
      cy.getByDataCy("session-name").should('contain.text', 'Update Yoga session');

    })
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
