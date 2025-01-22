import * as cypress from "cypress";

beforeEach(() => {
  cy.visit('/login')
  cy.get('input[formcontrolname=email]').type(Cypress.env('userEmail'));
  cy.get('input[formcontrolname=password]').type(`${Cypress.env('userPassword')}{enter}{enter}`)

  cy.url().should('include', '/sessions')
  cy.getByDataCy("create-session").should('not.exist')
})

describe('User', () => {

  beforeEach(() => {
    //Cleanup
    cy.getByDataCy("detail-session-1").click()
    cy.get('[data-cy="unparticipate-1"], [data-cy="participate-1"]').then((btn) => {
      if (btn.attr('data-cy') === 'unparticipate-1') {
        cy.getByDataCy('unparticipate-1').click();
      }
      cy.getByDataCy("goBack").click()
    });
  })

  it('Should be able to see session and add to the session', () => {
    cy.getByDataCy("detail-session-1").click()
    cy.url().should('include', 'detail/1')
    cy.getByDataCy("participate-1").click()
    cy.getByDataCy("unparticipate-1").should('be.visible')
  })

  it('Should be able to see session and unsubscribe from session', () => {
    cy.getByDataCy("detail-session-1").click()
    cy.url().should('include', 'detail/1')
    cy.getByDataCy("participate-1").click()
    cy.getByDataCy("unparticipate-1").should('be.visible')

    cy.getByDataCy("unparticipate-1").click()
    cy.getByDataCy("participate-1").should('be.visible')
  })

})
