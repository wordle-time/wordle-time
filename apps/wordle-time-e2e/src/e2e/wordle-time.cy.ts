const group = 'worteingabe-error';
describe(group, () => {
  beforeEach(() => cy.visit('/'));

  const testname = 'should show too_many_letters';
  it(testname, () => {
    cy.get("a[href='/game']").click();

    cy.get('[data-cy="guess-button"]').contains('Raten').should('exist');

    // cy.typeWord('aaaaa');

    // cy.get('[data-cy="guess-button"]').contains('Raten').click();

    // cy.get('[data-cy="try-count"]').contains('Tries: 1 / 6').should('exist');

    cy.screenshot('/F001/' + group + ' -- ' + testname, {
      overwrite: true,
    });
  });
});
