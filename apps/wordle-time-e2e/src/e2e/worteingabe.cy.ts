import { getScreenshotPath } from '../support/utils';

const error_group = 'worteingabe-error';
describe(error_group, () => {
  beforeEach(() => {
    cy.visit('/game');
  });

  const wrong_char_test = 'should disable guess button if wrong char';
  it(wrong_char_test, () => {
    // wait for loading screen to disappear
    // if wrong char is entered, guess button should be disabled && input field should be empty
    cy.get('[data-cy="letter-0"]').should('exist');
    cy.get('[data-cy="letter-0"]').clear();
    cy.get('[data-cy="letter-0"]').type('1');
    cy.get('[data-cy="letter-0"]').should('have.value', ''); // input field should be empty
    cy.get('[data-cy="guess-button"]').should('be.disabled'); // guess button should be disabled

    cy.screenshot(getScreenshotPath('F001', error_group, wrong_char_test), {
      overwrite: true,
    });
  });
});
