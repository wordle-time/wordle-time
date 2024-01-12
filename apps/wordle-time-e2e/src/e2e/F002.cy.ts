import { getScreenshotPath } from '../support/utils';

const feedback_group = 'feedback';
describe(feedback_group, () => {
  beforeEach(() => {
    cy.visit('/game');

    // wait for loading screen to disappear and set timeout to 10s
    cy.get('[data-cy="loading-message"]', { timeout: 15_000 }).should(
      'not.exist'
    );

    cy.get('[data-cy="letter-0"]').should('exist');
  });

  const correct_letter_test = 'should show correct_letter';
  it(correct_letter_test, () => {

    // if wrong char is entered, guess button should be disabled && input field should be empty
    cy.get('[data-cy="letter-0"]').clear();
    cy.get('[data-cy="letter-0"]').type('A');
    cy.get('[data-cy="guess-button"]').click();
    cy.get('[data-cy="letter-0"]', {timeout: 1_000}).should('have.class', 'border-ctp-green');

    cy.screenshot(getScreenshotPath('F002', feedback_group, correct_letter_test), {
      overwrite: true,
    });
  });

  const wrong_spot_test = 'should show wrong_sport';
  it(wrong_spot_test, () => {

    // if wrong char is entered, guess button should be disabled && input field should be empty
    cy.get('[data-cy="letter-0"]').clear();
    cy.get('[data-cy="letter-0"]').type('E');
    cy.get('[data-cy="guess-button"]').click();
    cy.get('[data-cy="letter-0"]', {timeout: 1_000}).should('have.class', 'border-ctp-yellow');

    cy.screenshot(getScreenshotPath('F002', feedback_group, wrong_spot_test), {
      overwrite: true,
    });
  });

  const wrong_letter_test = 'should show wrong_letter';
  it(wrong_spot_test, () => {

    // if wrong char is entered, guess button should be disabled && input field should be empty
    cy.get('[data-cy="letter-0"]').clear();
    cy.get('[data-cy="letter-0"]').type('U');
    cy.get('[data-cy="guess-button"]').click();
    cy.get('[data-cy="letter-0"]', {timeout: 1_000}).should('have.class', 'border-ctp-red');

    cy.screenshot(getScreenshotPath('F002', feedback_group, wrong_letter_test), {
      overwrite: true,
    });
  });

});
