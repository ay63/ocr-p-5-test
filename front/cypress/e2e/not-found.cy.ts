describe('Not found', () => {
  it('Should show not found page on wrong url', () => {
    cy.visit('/fakeUrl')
    cy.getByDataCy("not-found").should('exist').contains('Page not found !')
  })
})

