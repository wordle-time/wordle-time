import { $, component$, useStore } from "@builder.io/qwik";
import Letter from "../../components/letter/letter";
import { server$ } from "@builder.io/qwik-city";

export enum LetterState {
  "CorrectSpot",
  "WrongSpot",
  "WrongLetter",
  "Empty"
}

export interface GuessResult {
  letterStates: LetterState[];
}

export interface CurrentGuess {
  letter: string[];
}

const endpoint = "http://localhost:8090/api/guess?word=";

const serverResponse = server$(async (guess: CurrentGuess) => {
  const response = await fetch(endpoint + guess.letter.join("").toLowerCase());
  const json = await response.json();
  console.log(json);
  return json;
});

export default component$(() => {

  const store = useStore(
    {
      tries: 5,
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
        <div> Tries: {store.tries} / 6</div>
        <div> Current Guess: {store.CurrentGuess.letter.join("")}</div>
        <div> Guess Result: {store.GuessResult.letterStates.join("")}</div>
      </section>

      <div> Guess Result: {store.GuessResult.letterStates.join("")}</div>
      {
        !store.isComplete && store.tries < 6 && (
          <>
            <div class="flex items-center justify-center">
              {store.CurrentGuess.letter.map((letter, index) => (
                <Letter
                  key={index}
                  index={index}
                  letter={letter}
                  letterState={store.GuessResult.letterStates[index]}
                  onLetterChange={onLetterChange} />
              ))}
            </div>
            <div class="flex items-center justify-center my-16">
              <button class="rounded-lg border-4 p-2 px-4 border-ctp-blue hover:bg-ctp-blue hover:text-ctp-base"
                onClick$={async () => {
                  const response = await serverResponse(store.CurrentGuess);
                  store.GuessResult.letterStates = response.letterStates;
                }}


              >Raten</button>
            </div>
          </>
        )
      }
      {
        store.isComplete && (
          <div class="flex items-center justify-center my-16">
            <h1>You made it</h1>
          </div>
        )
      }
      {
        store.tries >= 6 && !store.isComplete && (
          <div class="flex items-center justify-center my-16">
            <h1>You lost</h1>
          </div>
        )
      }
    </>
  );
}
);




















































