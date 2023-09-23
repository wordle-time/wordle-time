import { $, component$, useStore, useVisibleTask$ } from "@builder.io/qwik";
import Letter from "../../components/letter/letter";
import { server$ } from "@builder.io/qwik-city";
import { animate } from "motion";
import { ICurrentGuess, IGuessResult, ILetterState, IWordFromId } from "@wordle-time/models";

const guessRoute = "http://localhost:8090/api/guess?word=";
const wordForIdRoute = "http://localhost:8090/api/wordForGameID?gameID=";

export const guessResponse = server$(async (guess) => {
  const response = await fetch(guessRoute + guess.letter.join("").toLowerCase());
  console.log("server response: ", response);
  const json = await response.json();
  console.log("json: ", json);
  return json;
});

export const wordForIdResponse = server$(async (id) => {
  const response = await fetch(wordForIdRoute + id);
  console.log("server response: ", response);
  const json = await response.json();
  console.log("json: ", json);
  return json;
});



export interface GameState {
  tryCount: number;
  isComplete: boolean;
  isLoading: boolean;
  CurrentGuess: ICurrentGuess;
  GuessResult: IGuessResult;
  LocaleDateString: string;
  wordFromId?: IWordFromId;

}

export default component$(() => {
  const store = useStore<GameState>(
    {
      tryCount: 0,
      isComplete: false,
      isLoading: true,
      CurrentGuess: {
        letter: ["A", "E", "I", "O", "U"]
      },
      GuessResult: {
        letterStates: [
          ILetterState.Undefiend,
          ILetterState.Undefiend,
          ILetterState.Undefiend,
          ILetterState.Undefiend,
          ILetterState.Undefiend
        ]
      },
      LocaleDateString: new Date().toLocaleDateString()
    },
  )

  const isToDay = $((localeDateString: string) => {
    console.log("getting the current date");
    const today = new Date();
    console.log("today is", today.toLocaleDateString(), "localStorage is from", localeDateString);
    if (today.toLocaleDateString() === localeDateString) {
      console.log("Localstorage is from today");
      return true;
    }
  });

  const updateStateFromStorage = $((state: GameState) => {
    isToDay(state.LocaleDateString).then(
      (isToDay) => {
        if (isToDay) {
          console.log("Apply state from local storage");
          store.tryCount = state.tryCount;
          store.isComplete = state.isComplete;
          store.isLoading = state.isLoading;
          store.CurrentGuess = state.CurrentGuess;
          store.GuessResult = state.GuessResult;
          store.LocaleDateString = state.LocaleDateString;
        } else {
          console.log("Localstorage is not from today");
          window.localStorage.removeItem("gameState");
          console.log("state removed from local storage");
        }
      }
    )
  });


  // load the state of the game from local storage
  useVisibleTask$(() => {
    console.log("checking cookies for solutions");
    const cookie = document.cookie.split(";").find(cookie => cookie.includes("gameId"));
    if (cookie) {
      const id = cookie.split("=")[1];
      console.log("cookie found with id: ", id);
      wordForIdResponse(id).then(
        (wordFromId: IWordFromId) => {
          console.log("word from id: ", wordFromId);
          store.wordFromId = wordFromId;
        }
      )
      document.cookie = '';
      console.log("cookie removed");
    }

    console.log("checking local storage for game state");
    const localStorage = Object.keys(window.localStorage).find(key => key === "gameState");
    if (localStorage) {
      console.log("local storage found");
      const gameState = JSON.parse(window.localStorage.getItem("gameState") || "") as GameState;
      // chck if the date day, month and year are the same
      if (gameState) {
        updateStateFromStorage(gameState);
        console.log("store loaded from local storage");
      }
    }

    animate(
      ".loading",
      {
        scale: [1, 1.5, 1],
      },
      {
        duration: 1.5,
        repeat: Infinity,
      }
    )
    store.isLoading = false;
  });

  const onLetterChange = $((index: number, letter: string) => {
    store.CurrentGuess.letter[index] = letter;
  });

  return (
    <div class='grid h-screen place-items-center'>
      <div>
        {
          store.wordFromId && (
            <div class="flex-row items-center justify-center my-16">
              <h3 class="text-3xl text-ctp-blue">Last time you played on: {store.wordFromId.date}.</h3>
              <h3 class="text-3xl text-ctp-blue">Solution: {store.wordFromId.word?.toLocaleUpperCase()}</h3>
            </div>
          )
        }
      </div>
      <div>
        {
          store.isLoading && (
            <div class="flex items-center justify-center">
              <h1 class="text-3xl loading">Loading ...</h1>
            </div>
          )
        }
        {
          !store.isComplete && store.tryCount < 6 && !store.isLoading && (
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
                    const response = await guessResponse(store.CurrentGuess);
                    store.GuessResult.letterStates = response.letterStates;
                    store.tryCount = store.tryCount + 1;
                    animate(".tryCount", {
                      scale: [1, 1.5, 1],
                    })
                    window.localStorage.setItem("gameState", JSON.stringify(store));
                  }}
                >Raten</button>
              </div>
              <div class="flex items-center justify-center my-16">
                <h3 class="text-3xl tryCount" > Versuche: {store.tryCount} / 6</h3>
              </div>
            </>
          )
        }
        {
          store.isComplete && !store.isLoading && (
            <div class="flex items-center justify-center my-16">
              <h1 class="text-3xl text-ctp-green">You made it</h1>
            </div>
          )
        }
        {
          store.tryCount >= 6 && !store.isComplete && !store.isLoading && (
            <div class="flex items-center justify-center my-16">
              <h1 class="text-3xl text-ctp-red">You lost</h1>
            </div>
          )
        }
      </div>
    </div>
  );
}
);




















































function useClientEffect$(arg0: () => void) {
  throw new Error("Function not implemented.");
}

