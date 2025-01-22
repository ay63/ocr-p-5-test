describe('Login spec', () => {
  it('Login admin successfull', () => {
    cy.visit('/login')

    cy.get('input[formcontrolname=email]').type(Cypress.env('adminEmail'));
    cy.get('input[formcontrolname=password]').type(`${Cypress.env('adminPassword')}{enter}{enter}`)


    cy.url().should('include', '/sessions')
    cy.getByDataCy("create-session").should('exist')
  })


  it('Login successfull with none admin user', () => {
    cy.visit('/login')

    cy.get('input[formcontrolname=email]').type(Cypress.env('userEmail'))
    cy.get('input[formcontrolname=password]').type(`${Cypress.env('userPassword')}{enter}{enter}`)

    cy.url().should('include', '/sessions')
    cy.getByDataCy("create-session").should('not.exist')
  })

  it('Login failed missing form info', () => {
    cy.visit('/login')

    cy.get('form').submit()
    cy.get('.error').should('exist')
    cy.get('input[formcontrolname=email]').should('have.class', 'ng-invalid')
    cy.get('input[formcontrolname=password]').should('have.class', 'ng-invalid')

    cy.url().should('include', '/login')
  })

});
