import { $, component$, useSignal, useStore } from "@builder.io/qwik";
import { Form, RequestHandler } from "@builder.io/qwik-city";
import { Input } from "postcss";
import Letter from "../../components/letter/letter";

export enum LetterState {
  "CorrectSpot" = 0,
  "WrongSpot" = 1,
  "WrongLetter" = 2,
  "Empty" = 3
}


export interface GuessResult {
  letterStates: LetterState[];
}

export interface CurrentGuess {
  letter: string[];
}

export default component$(() => {
  const store = useStore(
    {
      tries: 0,
      isComplete: false,
      CurrentGuess: {
        letter: ["a", "b", "a", "a", "b"]
      } as CurrentGuess,
      GuessResult: {
        letterStates: [LetterState.Empty, LetterState.CorrectSpot, LetterState.WrongSpot, LetterState.WrongLetter, LetterState.CorrectSpot]
      } as GuessResult
    }
  )

  const onLetterChange = $((index: number, letter: string) => {
    store.CurrentGuess.letter[index] = letter;
  });


  return (
    <>
      <div>Game</div>
      <section class="p-4">
        <div> Tries: {store.tries}</div>
        <div> Current Guess: {store.CurrentGuess.letter.join("")}</div>
        <div> Guess Result: {store.GuessResult.letterStates.join("")}</div>
        {store.CurrentGuess.letter.map((letter, index) => (
          <Letter
            key={index}
            index={index}
            letter={letter}
            letterState={store.GuessResult.letterStates[index]}
            onLetterChange={onLetterChange} />
        ))}
      </section>
    </>
  )
});




















































