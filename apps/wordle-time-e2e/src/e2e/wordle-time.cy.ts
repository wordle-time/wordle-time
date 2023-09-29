describe('wordle-time-frontend', () => {
  beforeEach(() => cy.visit('/'));

  it('should navigate game', () => {
    cy.get("a[href='/game']").click();

    cy.get("button").contains("Raten").should("exist");

    cy.typeWord("aaaaa");

    cy.get("button").contains("Raten").click();

    cy.get("h3.tryCount").contains("Tries: 1 / 6").should("exist");

    
    /*
    // Custom command example, see `../support/commands.ts` file
    cy.login('my-email@something.com', 'myPassword');

    // Function helper example, see `../support/app.po.ts` file
    getGreeting().contains('Welcome wordle-time-frontend');
     */
  });
});
