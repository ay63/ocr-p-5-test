import * as http from "node:http";

const fakeEmail = `email${Math.random()}@gmail.com`
const fakePassword = 'test!1234'

beforeEach(() => {
    cy.visit('/register')
})

describe('Register functionality', () => {

    it('register successful', () => {
      cy.intercept('POST', '/api/auth/register', {
        body: {
          email: fakeEmail,
          password: fakePassword,
          firstName: "firstName",
          lastName: "lastName"
        },
        statusCode: 200
      })

        cy.getByDataCy("firstName").type("firstName")
        cy.getByDataCy("lastName").type("lastName")
        cy.getByDataCy("email").type(fakeEmail)
        cy.getByDataCy("password").type(`${fakePassword}{enter}{enter}`)

        cy.url().should('include', '/login')
    })

    it('should register user login', () => {
        cy.visit('/login')

        cy.intercept('POST', '/auth/login', {
           body: {
            email: fakeEmail,
            password: fakePassword
           },
          statusCode: 200
        })

        cy.get('input[formcontrolname=email]').type(fakeEmail)
        cy.get('input[formcontrolname=password]').type(`${fakePassword}{enter}{enter}`)

        cy.url().should('include', '/login')
    })
})


describe('register failed form', () => {
    it('should failed empty format', () => {
        cy.getByDataCy("firstName").invoke('val', '')
        cy.getByDataCy("lastName").invoke('val', '')
        cy.getByDataCy("email").invoke('val', '')
        cy.getByDataCy("password").type(`{enter}{enter}`)

        cy.getByDataCy("lastName").should('have.class', 'ng-invalid')
        cy.getByDataCy("email").should('have.class', 'ng-invalid')
        cy.getByDataCy("password").should('have.class', 'ng-invalid')

        cy.url().should('include', '/register')
    })


    it('should failed email format', () => {
        cy.getByDataCy("firstName").type("firstName")
        cy.getByDataCy("lastName").type("lastName")
        cy.getByDataCy("password").type(`${fakePassword}{enter}{enter}`)
        cy.getByDataCy("email").type('invalid-email')
        cy.getByDataCy("email").should('have.class', 'ng-invalid')

        cy.get('form').submit()

        cy.get('.error').should('exist')
    })

    it('should failed firstName min length 3', () => {
        cy.getByDataCy("lastName").type("lastName")
        cy.getByDataCy("email").type(fakeEmail)
        cy.getByDataCy("password").type(`${fakePassword}{enter}{enter}`)
        cy.getByDataCy("firstName").type("er")

        cy.get('form').submit()

        cy.get('.error').should('exist')
    })

    it('should failed firstName max length 20', () => {
        cy.getByDataCy("lastName").type("lastName")
        cy.getByDataCy("email").type(fakeEmail)
        cy.getByDataCy("password").type(`${fakePassword}{enter}{enter}`)
        cy.getByDataCy("firstName").type("a".repeat(21))

        cy.get('form').submit()

        cy.get('.error').should('exist')
    })


    it('should failed lastName min length 3', () => {
        cy.getByDataCy("firstName").type("firstName")
        cy.getByDataCy("email").type(fakeEmail)
        cy.getByDataCy("password").type(`${fakePassword}{enter}{enter}`)
        cy.getByDataCy("lastName").type("er")

        cy.get('form').submit()

        cy.get('.error').should('exist')
    })

    it('should failed lastName max length 20', () => {
        cy.getByDataCy("firstName").type("firstName")
        cy.getByDataCy("email").type(fakeEmail)
        cy.getByDataCy("password").type(`${fakePassword}{enter}{enter}`)
        cy.getByDataCy("lastName").type("a".repeat(21))

        cy.get('form').submit()

        cy.get('.error').should('exist')
    })

    it('should failed password min length 3', () => {
        cy.getByDataCy("firstName").type("firstName")
        cy.getByDataCy("lastName").type("lastName")
        cy.getByDataCy("email").type(fakeEmail)
        cy.getByDataCy("password").type('in')

        cy.get('form').submit()

        cy.get('.error').should('exist')

    })

    it('should failed password max length 40', () => {
        cy.getByDataCy("firstName").type("firstName")
        cy.getByDataCy("lastName").type("lastName")
        cy.getByDataCy("email").type(fakeEmail)
        cy.getByDataCy("password").type('a'.repeat(41))

        cy.get('form').submit()

        cy.get('.error').should('exist')
    })
})
