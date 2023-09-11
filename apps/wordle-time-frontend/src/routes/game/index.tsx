import { $, component$, useStore } from "@builder.io/qwik";
import Letter from "../../components/letter/letter";
import { routeAction$, server$ } from "@builder.io/qwik-city";

export enum LetterState {
  "Undefiend" = 0,
  "CorrectSpot" = 1,
  "WrongSpot" = 2,
  "WrongLetter" = 3,
}

export interface GuessResult {
  letterStates: LetterState[];
}

export interface CurrentGuess {
  letter: string[];
}

const endpoint = "http://localhost:8090/api/guess?word=";

export const serverResponse = server$(async (guess) => {
  const response = await fetch(endpoint + guess.letter.join("").toLowerCase());
  const json = await response.json();
  console.log(json);
  return json;
});

export interface GameState {
  tryCount: number;
  isComplete: boolean;
  isLoading: boolean;
  CurrentGuess: CurrentGuess;
  GuessResult: GuessResult;
}

export default component$(() => {

  const store = useStore<GameState>(
    {
      tryCount: 0,
      isComplete: false,
      isLoading: false,
      CurrentGuess: {
        letter: ["A", "E", "I", "O", "U"]
      },
      GuessResult: {
        letterStates: [
          LetterState.Undefiend,
          LetterState.Undefiend,
          LetterState.Undefiend,
          LetterState.Undefiend,
          LetterState.Undefiend
        ]
      },
    }
  )



  const onLetterChange = $((index: number, letter: string) => {
    store.CurrentGuess.letter[index] = letter;
  });

  return (
    <>
      {
        !store.isComplete && store.tryCount < 6 && (
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
              <button disabled={
                store.CurrentGuess.letter.join("").length < 5
              } class="rounded-lg disabled:hover:cursor-not-allowed disabled:border-ctp-red disabled:bg-ctp-red disabled:text-ctp-crust border-4 p-2 px-4 border-ctp-blue hover:bg-ctp-blue hover:text-ctp-base"
                onClick$={async () => {
                  const response = await serverResponse(store.CurrentGuess);
                  store.GuessResult.letterStates = response.letterStates;
                  store.tryCount = store.tryCount + 1;
                }}
              >Raten</button>
            </div>
            <div class="flex items-center justify-center my-16">
              <h3 class="text-3xl"> Versuche: {store.tryCount} / 6</h3>
            </div>
          </>
        )
      }
      {
        store.isComplete && (
          <div class="flex items-center justify-center my-16">
            <h1 class="text-3xl text-ctp-green">You made it</h1>
          </div>
        )
      }
      {
        store.tryCount >= 6 && !store.isComplete && (
          <div class="flex items-center justify-center my-16">
            <h1 class="text-3xl text-ctp-red">You lost</h1>
          </div>
        )
      }
    </>
  );
}
);




















































