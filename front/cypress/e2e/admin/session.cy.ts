import * as cypress from "cypress";

beforeEach(() => {
  cy.visit('/login')
  cy.get('input[formcontrolname=email]').type(Cypress.env('adminEmail'));
  cy.get('input[formcontrolname=password]').type(`${Cypress.env('adminPassword')}{enter}{enter}`)

  cy.url().should('include', '/sessions')
  cy.getByDataCy("create-session").should('exist')
})

