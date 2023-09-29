// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************

// eslint-disable-next-line @typescript-eslint/no-namespace
declare namespace Cypress {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  interface Chainable<Subject> {
    typeWord(word: string): void;
    //login(email: string, password: string): void;
  }
}
//
// -- This is a parent command --
Cypress.Commands.add('typeWord', (word) => {
  if(word.length != 5) throw new Error(`Can only guess words of length 5. ${word} has ${word.length} letters.`);

  console.log(`Entering ${word}`);

  for(let letterIndex = 0; letterIndex <= 4; letterIndex++) {
    const letterId = "letter" + letterIndex;
    cy.get(`#${letterId}`).clear();
    cy.get(`#${letterId}`).type(word[letterIndex]);
  }
});

/*
Cypress.Commands.add('login', (email, password) => {
  console.log('Custom command example: Login', email, password);
});
*/
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })
