
beforeEach(() => {
    cy.visit('/register')
})

describe('Register functionality', () => {

    it('Should register successfull', () => {
      cy.intercept('POST', '/api/auth/register', {
        body: {
          email: "user@gmail.com",
          password: "test!1234",
          firstName: "firstName",
          lastName: "lastName"
        },
        statusCode: 200
      })

        cy.getByDataCy("firstName").type("firstName")
        cy.getByDataCy("lastName").type("lastName")
        cy.getByDataCy("email").type("user@gmail.com")
        cy.getByDataCy("password").type("test!1234")

        cy.getByDataCy("registerBtn").click()

        cy.url().should('include', '/login')
    })
})


describe('Register failed form', () => {
    it('Should display error when register failed with empty format', () => {
        cy.getByDataCy("firstName").invoke('val', '')
        cy.getByDataCy("lastName").invoke('val', '')
        cy.getByDataCy("email").invoke('val', '')
        cy.getByDataCy("password").type(`{enter}{enter}`)

        cy.getByDataCy("lastName").should('have.class', 'ng-invalid')
        cy.getByDataCy("email").should('have.class', 'ng-invalid')
        cy.getByDataCy("password").should('have.class', 'ng-invalid')

        cy.url().should('include', '/register')
    })


    it('Should display error when register failed with invalid email format', () => {
        cy.getByDataCy("firstName").type("firstName")
        cy.getByDataCy("lastName").type("lastName")
        cy.getByDataCy("password").type("test!1234{enter}{enter}")
        cy.getByDataCy("email").type('invalid-email')
        cy.getByDataCy("email").should('have.class', 'ng-invalid')

        cy.get('form').submit()

        cy.get('.error').should('exist')
    })

    it('Should display error when register failed with firstName min length 3', () => {
        cy.getByDataCy("lastName").type("lastName")
        cy.getByDataCy("email").type("user@gmail.com")
        cy.getByDataCy("password").type("test!1234{enter}{enter}")
        cy.getByDataCy("firstName").type("er")

        cy.get('form').submit()

        cy.get('.error').should('exist')
    })

    it('Should display error when register failed with firstName max length 20', () => {
        cy.getByDataCy("lastName").type("lastName")
        cy.getByDataCy("email").type("user@gmail.com")
        cy.getByDataCy("password").type("test!1234{enter}{enter}")
        cy.getByDataCy("firstName").type("a".repeat(21))

        cy.get('form').submit()

        cy.get('.error').should('exist')
    })


    it('Should display error when register failed with lastName min length 3', () => {
        cy.getByDataCy("firstName").type("firstName")
        cy.getByDataCy("email").type("user@gmail.com")
        cy.getByDataCy("password").type("test!1234{enter}{enter}")
        cy.getByDataCy("lastName").type("er")

        cy.get('form').submit()

        cy.get('.error').should('exist')
    })

    it('Should display error when register failed with lastName max length 20', () => {
        cy.getByDataCy("firstName").type("firstName")
        cy.getByDataCy("email").type("user@gmail.com")
        cy.getByDataCy("password").type("test!1234{enter}{enter}")
        cy.getByDataCy("lastName").type("a".repeat(21))

        cy.get('form').submit()

        cy.get('.error').should('exist')
    })

    it('Should display error when register failed with password min length 3', () => {
        cy.getByDataCy("firstName").type("firstName")
        cy.getByDataCy("lastName").type("lastName")
        cy.getByDataCy("email").type("user@gmail.com")
        cy.getByDataCy("password").type('in')

        cy.get('form').submit()

        cy.get('.error').should('exist')

    })

    it('Should display error when register failed with password max length 40', () => {
        cy.getByDataCy("firstName").type("firstName")
        cy.getByDataCy("lastName").type("lastName")
        cy.getByDataCy("email").type("user@gmail.com")
        cy.getByDataCy("password").type('a'.repeat(41))

        cy.get('form').submit()

        cy.get('.error').should('exist')
    })
})
