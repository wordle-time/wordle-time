import { component$, useVisibleTask$ } from '@builder.io/qwik';
import { animate } from 'motion';

export default component$(() => {

  return (
    <div
    >
      Spielmechanik
      Buchstaben-Eingabe: Der Spieler gibt einen Buchstaben ein, und der Server antwortet daraufhin mit einem von drei möglichen Hinweisen:

      Der Buchstabe ist im gesuchten Wort vorhanden.
      Der Buchstabe steht an der richtigen Stelle im Wort.
      Der Buchstabe ist nicht im gesuchten Wort enthalten.
      Spielende:

      Gewonnen: Wenn der Spieler das Wort innerhalb der sechs Versuche errät, hat er das Spiel gewonnen.
      Verloren: Wenn der Spieler das Wort nach sechs Versuchen nicht erraten hat, ist das Spiel verloren.
      Mehrfache Versuche: Spieler können so oft sie möchten versuchen, das Wort zu erraten, solange sie es innerhalb der sechs Versuche schaffen.
      Tägliches Wort: Das zu erratende Wort wechselt täglich, sodass Spieler jeden Tag eine neue Herausforderung haben.
    </div>
  );
});
