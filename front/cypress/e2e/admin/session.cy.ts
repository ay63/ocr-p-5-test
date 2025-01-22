import * as cypress from "cypress";

beforeEach(() => {
  cy.visit('/login')
  cy.get('input[formcontrolname=email]').type(Cypress.env('adminEmail'));
  cy.get('input[formcontrolname=password]').type(`${Cypress.env('adminPassword')}{enter}{enter}`)

  cy.url().should('include', '/sessions')
  cy.getByDataCy("create-session").should('exist')
})


describe('Admin session', () => {


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


  it('Should be able to delete a session', () => {
    let dataCy;
    let match;

    cy.get('[data-cy^="detail-session-"]').first().then((btn) => {
      dataCy = btn.attr('data-cy');
      match = dataCy.match(/detail-session-(\d+)/);

      if (match) {
        const id = match[1];
        cy.wrap(btn).click();
        cy.url().should('include', `/detail/${id}`);
        cy.get(`[data-cy="delete-${id}"]`).click();
      }
    });

  })

  it('Should be able to update a session', () => {
    let dataCy;
    let match;

    cy.get('[data-cy^="edit-session-"]').first().then((btn) => {
      dataCy = btn.attr('data-cy');
      match = dataCy.match(/edit-session-(\d+)/);

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


})
