beforeEach(() => {
  cy.initUserDataAndLoginIn()
  cy.getByDataCy("accountBtn").click()
})


describe('Account user', () => {
  it('Should be access to account detail and see data and see delete button', () => {
    cy.url().should('include', '/me')
    cy.getByDataCy("deleteBtn").should('exist')
    cy.contains('January 22, 2025').should('be.visible');
    cy.contains('lastName'.toUpperCase()).should('be.visible');
    cy.contains('firstName').should('be.visible');
    cy.contains('email0.6009744576622176@gmail.com').should('be.visible');
  })

  it('Should be able to delete account', () => {
    cy.url().should('include', '/me')

    cy.intercept('DELETE', '/api/user/1', {
      statusCode: 200
    })

    cy.getByDataCy("deleteBtn").click()
    cy.url().should('eq', Cypress.config('baseUrl') + '/');
  })
})
