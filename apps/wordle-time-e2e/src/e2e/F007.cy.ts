import { getScreenshotPath } from '../support/utils';

const requirement_id = 'F007';
const mobile_group = 'mobile';
describe(mobile_group, () => {
  beforeEach(() => {
    cy.viewport(412, 915);
  });
  const show_rules_test = 'should show rules';
  it(show_rules_test, () => {
    cy.visit('/');

    cy.screenshot(
      getScreenshotPath(requirement_id, mobile_group, show_rules_test),
      {
        overwrite: true,
      }
    );
  });

  const show_game_test = 'should show game';
  it(show_game_test, () => {
    cy.visit('/game');

    // wait for loading screen to disappear and set timeout to 10s
    cy.get('[data-cy="loading-message"]', { timeout: 15_000 }).should(
      'not.exist'
    );

    cy.screenshot(
      getScreenshotPath(requirement_id, mobile_group, show_game_test),
      {
        overwrite: true,
      }
    );
  });
});
