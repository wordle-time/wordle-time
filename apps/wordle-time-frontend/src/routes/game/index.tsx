import { $, component$, useStore, useVisibleTask$ } from "@builder.io/qwik";
import Letter from "../../components/letter/letter";
import { Form, RequestHandler, routeAction$, routeLoader$, server$ } from "@builder.io/qwik-city";
import { animate } from "motion";
import { ICurrentGuess, IGuessResult, ILetterState, IWordFromId } from "@wordle-time/models";

const guessRoute = "http://localhost:8090/api/guess/word?word=";
const wordForIdRoute = "http://localhost:8090/api/guess/wordForGameID?gameID=";
const currentIdRoute = "http://localhost:8090/api/guess/currentGameID";


export const onGet: RequestHandler = async ({ headers, headersSent }) => {
  // //cookie.set("gameID", "5");
  // // const response = await fetch(guessRoute + 'abcde');

  // // console.log("response: ", response);
  // // send(response.status, await response.text());
  // console.log("headers: ", headers);
  // console.log("headersSent: ", headersSent);
}

export const useCurrentId = routeAction$(async (data, { cookie }) => {
  const response = await fetch(currentIdRoute);
  const result = await response.json();
  //console.log("response: ", response);

  // console.log("result: ", result);
  return result as IWordFromId;
});

export const useGuessResult = routeAction$(async (data, { cookie }) => {
  //console.log("requestEvent: ", requestEvent);
  // console.log("data: ", data);

  // const gammeIdCookie = cookie.get("gameID");
  // if (gammeIdCookie) {
  //   console.log(gammeIdCookie);
  // }

  const response = await fetch(guessRoute + data.word, {
    headers: {
      cookie: cookie.get("gameID") ? "gameID=" + cookie.get("gameID")?.value : ""
    }
  });

  //console.log("response headers: ", response.headers);
  const gameId = response.headers.getSetCookie();
  if (gameId) {
    // console.log("gameId: ", gameId);
    cookie.set(gameId[0].split("=")[0], gameId[0].split("=")[1]);
  }
  //console.log("response: ", response);
  const result = await response.json();
  // console.log("result: ", result);
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
      wordFromId: {}
    },
  )

  guessResult.submit({ word: store.CurrentGuess.letter.join("") });

  // load the state of the game from local storage
  useVisibleTask$(() => {
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
    // store.isLoading = false;
  });



  return (
    <div class='grid h-screen place-items-center'>
      <h1>Hallo</h1>
      <Form action={guessResult}>
        <input type="text" name="word" />
        <button type="submit">Guess</button>
      </Form>
    </div>
  );
});




















































function useClientEffect$(arg0: () => void) {
  throw new Error("Function not implemented.");
}

