beforeEach(() => {
  cy.initUserDataAndLoginIn()
})
describe('Logout page', () => {

  it('logout successful', () => {
    cy.getByDataCy("logoutBtn").click()
    cy.url().should('include', '')
  })
})
