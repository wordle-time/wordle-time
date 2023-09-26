import { $, component$, useStore, useVisibleTask$ } from "@builder.io/qwik";
import Letter from "../../components/letter/letter";
import { Form, RequestHandler, routeAction$, routeLoader$, server$ } from "@builder.io/qwik-city";
import { animate } from "motion";
import { ICurrentGuess, IGuessResult, ILetterState, IWordFromId } from "@wordle-time/models";

const guessRoute = "http://localhost:8090/api/guess/word?word=";
const wordForIdRoute = "http://localhost:8090/api/guess/wordForGameID?gameID=";
const currentIdRoute = "http://localhost:8090/api/guess/currentGameID";


export const useCurrentId = routeAction$(async (data, { cookie }): Promise<IWordFromId> => {
  let wordFormId: IWordFromId = {}

  const gameID = cookie.get("gameID")?.value;
  if (gameID) {
    wordFormId.gameID = gameID as unknown as number;
  } else {
    const response = await fetch(currentIdRoute);
    const result = await response.json();
    wordFormId = result as IWordFromId;
    cookie.set("gameID", wordFormId.gameID as unknown as string);
  }


  console.log("wordfromID: ", wordFormId);
  return wordFormId;
});

export const useGuessResult = routeAction$(async (data, { cookie }) => {
  const response = await fetch(guessRoute + data.word, {
    headers: {
      cookie: cookie.get("gameID") ? "gameID=" + cookie.get("gameID")?.value : ""
    }
  });

  const gameId = response.headers.getSetCookie();
  if (gameId) {
    cookie.set(gameId[0].split("=")[0], gameId[0].split("=")[1]);
  }

  const result = await response.json();
  return result;
});

// fetch gameID if not present
export const getGameID = async () => {
  const response = await fetch(currentIdRoute);
  const result = await response.json();
  return result;
};


export const useWordForId = routeAction$(async (data, { cookie }) => {
  const gameID = cookie.get("gameID")?.value;
  if (!gameID) {
    const wordForGameID = await getGameID;
    console.log("wordfromID: ", wordForGameID);
  }
  const response = await fetch(wordForIdRoute + cookie.get("gameID")?.value);
  const result = await response.json();
  return result;
});

export interface GameState {
  tryCount: number;
  isComplete: boolean;
  isLoading: boolean;
  CurrentGuess: ICurrentGuess;
  GuessResult: IGuessResult;
  LocaleDateString: string;
  wordFromId: IWordFromId;
}

export default component$(() => {

  const guessResult = useGuessResult();
  const currentId = useCurrentId();


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
      LocaleDateString: new Date().toLocaleDateString(),
      wordFromId: {

      }
    },
  )

  useVisibleTask$(async () => {
    console.log("visible");
    const { value } = await currentId.submit();
    console.log("wordFormId: ", value);
    store.wordFromId = value;
    console.log("store.wordFromId: ", store.wordFromId);

    // guessResult.submit({ word: store.CurrentGuess.letter.join("") });
  });






  return (
    <div class='grid h-screen place-items-center' onLoad$={async () => {

    }
    }>
      <h1>Hallo</h1>
      <Form action={guessResult}>
        <input type="text" name="word" />
        <button type="submit">Guess</button>
      </Form>
    </div>
  );
});