import { getScreenshotPath } from '../support/utils';

const insert_group = 'worteingabe';
const error_group = 'worteingabe-error';
describe(error_group, () => {
  beforeEach(() => {
    cy.visit('/game');

    // wait for loading screen to disappear and set timeout to 15s
    cy.get('[data-cy="loading-message"]', { timeout: 15_000 }).should(
      'not.exist'
    );

    cy.get('[data-cy="letter-0"]').should('exist');
  });

  const show_letter_input_test = 'should show letter_input';
  it(show_letter_input_test, () => {
    cy.get('[data-cy="letter-0"]').clear();
    cy.get('[data-cy="letter-0"]').type('A');
    cy.get('[data-cy="letter-0"]').should('have.value', 'A');

    cy.wait(500);

    cy.screenshot(
      getScreenshotPath('F001', insert_group, show_letter_input_test),
      {
        overwrite: true,
      }
    );
  });

  const show_non_letter_input = 'should show non_letter_input';
  it(show_non_letter_input, () => {
    cy.get('[data-cy="letter-0"]').clear();
    cy.get('[data-cy="letter-0"]').type('1');
    cy.get('[data-cy="letter-0"]').should('have.value', ''); // input field should be empty
    cy.get('[data-cy="guess-button"]').should('be.disabled'); // guess button should be disabled

    cy.wait(500);

    cy.screenshot(
      getScreenshotPath('F001', error_group, show_non_letter_input),
      {
        overwrite: true,
      }
    );
  });
});
