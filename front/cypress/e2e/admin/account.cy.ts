beforeEach(() => {
  cy.initUserDataAndLoginIn(true)
  cy.getByDataCy("accountBtn").click()
})

describe('Account Admin user', () => {

  it('Should be access to account detail and see data', () => {
    cy.url().should('include', '/me')
    cy.contains('lastName'.toUpperCase()).should('be.visible');
    cy.contains('firstName').should('be.visible');
    cy.contains('You are admin').should('be.visible');
    cy.contains('January 22, 2025').should('be.visible');

    cy.contains('email0.6009744576622176@gmail.com').should('be.visible');
  })

  it('Should not be able to delete account', () => {
    cy.url().should('include', '/me')
    cy.getByDataCy("deleteBtn").should('not.exist')
  })
})


