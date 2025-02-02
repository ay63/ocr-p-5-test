beforeEach(() => {
  cy.sessionsInterceptor()
  cy.teacherInterceptor()
  cy.loginUser()
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
        users: [],
      },
      statusCode: 200,
    }).as('getSessionwithoutParticipate');

    cy.getByDataCy('detail-session-1').click();
    cy.wait('@getSessionwithoutParticipate')

    cy.getByDataCy('unparticipate-1').should('not.exist');
    cy.getByDataCy('participate-1').should('be.visible');

    cy.intercept('POST', 'api/session/1/participate/1', {
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
    }).as('postParticipate');


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
    }).as('getSessionwitParticipate');

    cy.getByDataCy('participate-1').click();

    cy.wait('@postParticipate');
    cy.wait('@getSessionwitParticipate');

    cy.getByDataCy('unparticipate-1').should('be.visible');
    cy.getByDataCy('participate-1').should('not.exist');
    cy.get('.attendees').should('have.text', '1 attendees');

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
        users: [1],
      },
      statusCode: 200,
    }).as('getSessionwitParticipate');

    cy.getByDataCy('detail-session-1').click();
    cy.getByDataCy('unparticipate-1').should('be.visible');
    cy.getByDataCy('participate-1').should('not.exist');

    cy.wait('@getSessionwitParticipate')

    cy.intercept('DELETE', 'api/session/1/participate/1', {
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
    }).as('getSessionwithoutParticipate');

    cy.getByDataCy('unparticipate-1').click();

    cy.wait('@removeParticiapte');
    cy.wait('@getSessionwithoutParticipate');

    cy.getByDataCy('unparticipate-1').should('not.exist');
    cy.getByDataCy('participate-1').should('be.visible');
    cy.get('.attendees').should('have.text', '0 attendees');
  });

  it('Should not be able to delete a session', () => {
    cy.getByDataCy("detail-session-1").click();
    cy.getByDataCy("delete-1").should('not.exist');
  });

})
