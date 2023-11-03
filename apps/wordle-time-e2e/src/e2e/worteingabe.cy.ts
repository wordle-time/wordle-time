import { getScreenshotPath } from '../support/utils';

const error_group = 'worteingabe-error';
describe(error_group, () => {
  beforeEach(() => cy.visit('/'));

  const too_many_letters_test = 'should show too_many_letters';
  it(too_many_letters_test, () => {
    cy.get("a[href='/game']").click();

    cy.get('[data-cy="guess-button"]').contains('Raten').should('exist');

    // cy.typeWord('aaaaa');

    // cy.get('[data-cy="guess-button"]').contains('Raten').click();

    // cy.get('[data-cy="try-count"]').contains('Tries: 1 / 6').should('exist');

    cy.screenshot(
      getScreenshotPath('F001', error_group, too_many_letters_test),
      {
        overwrite: true,
      }
    );
  });
});
