beforeEach(() => {
  cy.visit('/login')

  cy.intercept('POST', '/api/auth/login', {
    body: {
      id: 1,
      username: 'userName',
      firstName: 'firstName',
      lastName: 'lastName',
      admin: false
    },
    statusCode: 200
  })

  cy.intercept('GET', '/api/session', {
    body: [
      {
        "id": 1,
        "name": "test",
        "date": "2025-01-22T00:00:00.000+00:00",
        "teacher_id": 1,
        "description": "test",
        "users": [],
        "createdAt": "2025-01-23T09:12:33",
        "updatedAt": "2025-01-23T09:12:33"
      },
    ],
    statusCode: 200
  })

  cy.get('input[formcontrolname=email]').type(Cypress.env('adminEmail'));
  cy.get('input[formcontrolname=password]').type(`${Cypress.env('adminPassword')}{enter}{enter}`)
  cy.url().should('include', '/sessions')
})
describe('Logout page', () => {

  it('logout successful', () => {
    cy.getByDataCy("logoutBtn").click()
    cy.url().should('include', '')
  })
})
