import { component$ } from '@builder.io/qwik';
import { Link, type DocumentHead } from '@builder.io/qwik-city';
import Letter from '../components/letter/letter';

const getRandomLetterStateString = () => {
  const states = ['CorrectSpot', 'WrongSpot', 'WrongLetter', 'Undefined'];
  return states[Math.floor(Math.random() * states.length)];
}

export default component$(() => {
  return (
    <div class="grid-rows-3">
      <div class="h-[30vh] text-center flex-row justify-center items-center">
        <div class="p-4 ">
          <Letter letterState={getRandomLetterStateString()} letter='W' />
          <Letter letterState={getRandomLetterStateString()} letter='O' />
          <Letter letterState={getRandomLetterStateString()} letter='R' />
          <Letter letterState={getRandomLetterStateString()} letter='D' />
          <Letter letterState={getRandomLetterStateString()} letter='L' />
          <Letter letterState={getRandomLetterStateString()} letter='E' />
        </div>
        <div class="p-4">
          <Letter letterState={getRandomLetterStateString()} letter='T' />
          <Letter letterState={getRandomLetterStateString()} letter='I' />
          <Letter letterState={getRandomLetterStateString()} letter='M' />
          <Letter letterState={getRandomLetterStateString()} letter='E' />
        </div>
        <h2 class="text-4xl">Can you guess the word of the day?</h2>
      </div>
      <div class="h-[50vh] text-center pt-20">
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
