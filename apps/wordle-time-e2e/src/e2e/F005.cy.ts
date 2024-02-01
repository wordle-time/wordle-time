import { getScreenshotPath } from '../support/utils';

const requirement_id = 'F005';
const endgame_group = 'endgame';
describe(endgame_group, () => {
  beforeEach(() => {
    cy.visit('/game');

    // wait for loading screen to disappear and set timeout to 10s
    cy.get('[data-cy="loading-message"]', { timeout: 15_000 }).should(
      'not.exist'
    );

    cy.get('[data-cy="letter-0"]').should('exist');
  });

  const not_guessed_test = 'should show not_guessed';
  it(not_guessed_test, () => {
    cy.get('[data-cy="guess-button"]').click();
    cy.get('[data-cy="guess-button"]').click();
    cy.get('[data-cy="guess-button"]').click();
    cy.get('[data-cy="guess-button"]').click();
    cy.get('[data-cy="guess-button"]').click();
    cy.get('[data-cy="guess-button"]').click();

    cy.get('[data-cy="guess-fail"]', { timeout: 5_000 }).should('exist');

    cy.screenshot(
      getScreenshotPath(requirement_id, endgame_group, not_guessed_test),
      {
        overwrite: true,
      }
    );
  });

  const restart_game_test = 'should show restart_game';
  it(restart_game_test, () => {
    cy.get('[data-cy="guess-button"]').click();
    cy.get('[data-cy="guess-button"]').click();
    cy.get('[data-cy="guess-button"]').click();
    cy.get('[data-cy="guess-button"]').click();
    cy.get('[data-cy="guess-button"]').click();
    cy.get('[data-cy="guess-button"]').click();

    cy.get('[data-cy="reset-button"]', { timeout: 5_000 }).should('exist');

    cy.screenshot(
      getScreenshotPath(requirement_id, endgame_group, restart_game_test),
      {
        overwrite: true,
      }
    );
  });

  const remaining_time_test = 'should show remaining_time';
  it(remaining_time_test, () => {
    //tbd
    cy.get('[data-cy="letter-0"]').clear();
    cy.get('[data-cy="letter-0"]').type('A');
    cy.get('[data-cy="letter-0"]').should('have.value', 'A');

    cy.get('[data-cy="letter-1"]').clear();
    cy.get('[data-cy="letter-1"]').type('P');
    cy.get('[data-cy="letter-1"]').should('have.value', 'P');

    cy.get('[data-cy="letter-2"]').clear();
    cy.get('[data-cy="letter-2"]').type('P');
    cy.get('[data-cy="letter-2"]').should('have.value', 'P');

    cy.get('[data-cy="letter-3"]').clear();
    cy.get('[data-cy="letter-3"]').type('L');
    cy.get('[data-cy="letter-3"]').should('have.value', 'L');

    cy.get('[data-cy="letter-4"]').clear();
    cy.get('[data-cy="letter-4"]').type('E');
    cy.get('[data-cy="letter-4"]').should('have.value', 'E');

    // need to loose focus to trigger the validation
    // cy.get('[data-cy="letter-3"]');

    cy.get('[data-cy="guess-button"]').click();
    cy.get('#guess-success', { timeout: 10_000 }).should('exist');

    // wait for 5 seconds to see the remaining time otherwise the test will pass but the screenshot will be wrong
    cy.wait(5_000);

    cy.screenshot(
      getScreenshotPath(requirement_id, endgame_group, remaining_time_test),
      {
        overwrite: true,
      }
    );
  });
});
