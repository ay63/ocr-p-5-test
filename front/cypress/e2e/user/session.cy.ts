beforeEach(() => {
  cy.loginUser()
  cy.interceptSession()
  cy.getByDataCy("create-session").should('not.exist')
})

describe('User', () => {
  it('Should be able to see session and add subscript to the session', () => {
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'Session de test',
        description: 'Description de la session',
        date: '2023-10-01',
        createdAt: '2023-09-01',
        updatedAt: '2023-09-15',
        teacher_id: 1,
        users: [1],
      },
      statusCode: 200,
    }).as('addParticipate');
    
    cy.getByDataCy("create-session").should('not.exist')
    cy.getByDataCy('detail-session-1').click();
    cy.getByDataCy('unparticipate-1').should('be.visible');
    cy.getByDataCy('participate-1').should('not.exist');
  });

  it('Should be able unparticipate to one session', () => {
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'Session de test',
        description: 'Description de la session',
        date: '2023-10-01',
        createdAt: '2023-09-01',
        updatedAt: '2023-09-15',
        teacher_id: 1,
        users: [],
      },
      statusCode: 200,
    }).as('removeParticiapte');

    cy.getByDataCy("create-session").should('not.exist')
    cy.getByDataCy('detail-session-1').click();
    cy.getByDataCy('participate-1').should('be.visible');
    cy.getByDataCy('unparticipate-1').should('not.exist');
  });

  it('Should not be able to delete a session', () => {
    cy.getByDataCy("detail-session-1").click();
    cy.getByDataCy("delete-1").should('not.exist');
  })

})
