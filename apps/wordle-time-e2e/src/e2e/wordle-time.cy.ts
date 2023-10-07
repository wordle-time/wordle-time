describe('wordle-time-frontend', () => {
  beforeEach(() => cy.visit('/'));

  it('should navigate game', () => {
    cy.get("a[href='/game']").click();

    cy.get('[data-cy="guess-button"]').contains('Raten').should('exist');

    cy.typeWord('aaaaa');

    cy.get('[data-cy="guess-button"]').contains('Raten').click();

    cy.get('[data-cy="try-count"]').contains('Tries: 1 / 6').should('exist');
  });
});
