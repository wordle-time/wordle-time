import { component$, useStylesScoped$, useVisibleTask$ } from '@builder.io/qwik';
import { Link, type DocumentHead } from '@builder.io/qwik-city';
import { animate, spring } from 'motion';
import Letter from '../components/letter/letter';
import { ILetterState } from '@wordle-time/models';


export default component$(() => {
  return (
    <div class="grid-rows-3">
      <div class="h-[30vh] text-center flex-row justify-center items-center">
        <div class="p-4 ">
          <Letter letterState={"CorrectSpot"} letter='W'  />
          <Letter letterState={ILetterState.CorrectSpot} letter='O'  />
          <Letter letterState={ILetterState.CorrectSpot} letter='R'  />
          <Letter letterState={ILetterState.CorrectSpot} letter='D'  />
          <Letter letterState={ILetterState.CorrectSpot} letter='L'  />
          <Letter letterState={ILetterState.CorrectSpot} letter='E'  />
</div>
<div class="p-4">
          <Letter letterState={ILetterState.WrongSpot} letter='T'  />
          <Letter letterState={ILetterState.WrongSpot} letter='I'  />
          <Letter letterState={ILetterState.WrongSpot} letter='M'  />
          <Letter letterState={ILetterState.Undefined} letter='E'  />
        </div>
        <h2 class="text-4xl">Can you guess the word of the day?</h2>
      </div>
      <div class="h-[50vh] text-center">
        <h3>The word to guess changes daily, giving players a new challenge every day.</h3>
        <h4>How to Play?</h4>
        <p>Letter Input: The player inputs a letter, and the server responds with one of three possible clues:</p>
        <ul class="list-disc list-inside">
          <li>The letter is present in the word.</li>
          <li>The letter is in the correct position in the word.</li>
          <li>The letter is not in the word at all.</li>
        </ul>
        <p>If the player guesses the word within six attempts, they have won the game.</p>
        <p>If the player fails to guess the word after six attempts, they have lost the game.</p>
      </div>
      <div class="p-8 flex items-center justify-center h-[10vh]">
        <Link class="rounded-lg border-4 p-4 border-ctp-blue hover:bg-ctp-blue hover:text-ctp-base" href='game'>Play now</Link>
      </div>
    </div>
  );
});

export const head: DocumentHead = {
  title: 'Wordle Time',
  meta: [
    {
      name: 'description',
      content: 'Wordle Time - A wordle clone made with Qwik and Kotlin',
    },
  ],
};
