beforeEach(() => {
  cy.visit('/login')
})

describe('Login spec', () => {
  it('Should login successfull with admin user', () => {
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })
    cy.get('input[formcontrolname=email]').type('admin@email.com');
    cy.get('input[formcontrolname=password]').type('password{enter}{enter}')

    cy.url().should('include', '/sessions')
    cy.getByDataCy("create-session").should('exist')
  })


  it('Should login successfull with standard user', () => {
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false
      },
    })

    cy.get('input[formcontrolname=email]').type('user@email.com');
    cy.get('input[formcontrolname=password]').type('password{enter}{enter}')

    cy.url().should('include', '/sessions')
    cy.getByDataCy("create-session").should('not.exist')
  })

  it('Should display error when login failed with missing form info', () => {
    cy.get('form').submit()
    cy.get('.error').should('exist')
    cy.get('input[formcontrolname=email]').should('have.class', 'ng-invalid')
    cy.get('input[formcontrolname=password]').should('have.class', 'ng-invalid')

    cy.url().should('include', '/login')
  })

  it('Should display error when login failed with wrong credentials', () => {
    cy.get('form').submit()
    cy.intercept('POST', '/api/auth/login', {
      body: {},
      statusCode: 401
    })
    cy.get('.error').should('exist')
    cy.url().should('include', '/login')
  })

});
