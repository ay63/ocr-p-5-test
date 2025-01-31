beforeEach(() => {
  cy.initUserDataAndLoginIn(true)
  cy.getByDataCy("create-session").should('exist')
})

describe('Admin session', () => {
  describe('Session create', () => {
    it('Should be able to create a session', () => {
      cy.getByDataCy("create-session").click()
      cy.url().should('include', '/create')

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

    cy.getByDataCy("detail-session-1").click()
    cy.getByDataCy("delete-1").click();
    cy.url().should('include', '/detail/1');
  })

  it('Should be able to update a session', () => {
    cy.getByDataCy("edit-session-1").click();
    cy.url().should('include', `/update/1`);
    cy.getByDataCy("description").clear()
    cy.getByDataCy("description").type("update description");
    cy.getByDataCy("saveBtn").click()
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
