import { getScreenshotPath } from '../support/utils';

const counter_group = 'counter';
describe(counter_group, () => {
  beforeEach(() => {
    cy.visit('/game');
    // wait for loading screen to disappear and set timeout to 10s
    cy.get('[data-cy="loading-message"]', { timeout: 15_000 }).should(
      'not.exist'
    );

    cy.get('[data-cy="letter-0"]').should('exist');
  });


  const incremented_test = 'should show incremented';
  it(incremented_test, () => {
    // wait for loading screen to disappear and set timeout to 10s
    cy.get('[data-cy="loading-message"]', { timeout: 15_000 }).should(
      'not.exist'
    );

    cy.get('[data-cy="guess-button"]').click();
    cy.get('[data-cy="try-count"]').should('have.text', ' Tries: 1 / 6');
    cy.screenshot(getScreenshotPath('F003', counter_group, incremented_test), {
      overwrite: true,
    });
  });
});
