import { $, QRL, component$, useStore, useVisibleTask$ } from '@builder.io/qwik';
import { routeAction$ } from '@builder.io/qwik-city';
import {
  ICurrentGuess,
  IGuessResult,
  ILetterState,
  IWordFromId,
} from '@wordle-time/models';
import Letter from '../../components/letter/letter';
import { animate } from 'motion';
import { Loading } from '@wordle-time/ui';

const guessRoute = 'http://127.0.0.1:8090/api/guess/word?word=';
const wordForIdRoute = 'http://127.0.0.1:8090/api/guess/wordForGameID?gameID=';
const currentIdRoute = 'http://127.0.0.1:8090/api/guess/currentGameID';

export const useCurrentId = routeAction$(
  async (data, { cookie }): Promise<IWordFromId> => {
    const gameID = cookie.get('gameID')?.value;
    if (gameID) {
      const wordFormId = {} as IWordFromId;
      wordFormId.gameID = gameID as unknown as number;
      return wordFormId;
    }
    const response = await fetch(currentIdRoute);
    const result = await response.json();
    const wordFormId = result as IWordFromId;
    cookie.set('gameID', wordFormId.gameID as unknown as string);
    return wordFormId;
  }
);

export const useGuessResult = routeAction$(async (data, { cookie }) => {
  const response = await fetch(guessRoute + data.word, {
    headers: {
      cookie: cookie.get('gameID')
        ? 'gameID=' + cookie.get('gameID')?.value
        : '',
    },
  });

  const gameId = response.headers.getSetCookie();
  if (gameId.length > 0) {
    console.log(gameId);
    cookie.set(gameId[0].split('=')[0], gameId[0].split('=')[1]);
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
  const gameID = cookie.get('gameID')?.value;
  if (!gameID) {
    getGameID;
  }
  const response = await fetch(wordForIdRoute + cookie.get('gameID')?.value);
  const result = await response.json();
  if (result.word) {
    cookie.delete('gameID');
  }
  return result;
});

const isToDay = $((localeDateString: string) => {
  const today = new Date();
  if (today.toLocaleDateString() === localeDateString) {
    return true;
  }
});

interface IGameState {
  tryCount: number;
  incrementTryCount: QRL<(this: IGameState) => void>;
  isComplete: boolean;
  isLoading: boolean;
  CurrentGuess: ICurrentGuess;
  GuessResult: IGuessResult;
  LocaleDateString: string;
  wordFromId: IWordFromId;
}

const resetLocalStorage = () => {
  window.localStorage.removeItem('gameState');
};

const setDefaultState = () => {
  window.location.reload();
};

const initialState: IGameState = {
  tryCount: 0,
  incrementTryCount: $(function (this: IGameState) {
    this.tryCount++;
  }),

  isComplete: false,
  isLoading: true,
  CurrentGuess: {
    letter: ['A', 'P', 'P', 'L', 'O'],
  },
  GuessResult: {
    letterStates: [
      ILetterState.Undefined,
      ILetterState.Undefined,
      ILetterState.Undefined,
      ILetterState.Undefined,
      ILetterState.Undefined,
    ],
  },
  LocaleDateString: new Date().toLocaleDateString(),
  wordFromId: {},
};

export default component$(() => {
  const guessResult = useGuessResult();
  const currentId = useCurrentId();
  const wordForId = useWordForId();

  const store = useStore<IGameState>(initialState);

  const updateStateFromStorage = $((state: IGameState) => {
    isToDay(state.LocaleDateString).then((isToDay) => {
      if (isToDay) {
        console.log('Apply state from local storage');
        store.tryCount = state.tryCount;
        store.isComplete = state.isComplete;
        store.isLoading = state.isLoading;
        store.CurrentGuess = state.CurrentGuess;
        store.GuessResult = state.GuessResult;
        store.LocaleDateString = state.LocaleDateString;
      } else {
        console.log('Local-storage is not from today');
        window.localStorage.removeItem('gameState');
        console.log('state removed from local storage');
      }
    });
  });

  useVisibleTask$(async () => {
    const { value } = await currentId.submit();
    store.wordFromId = value;
    const { value: res } = await wordForId.submit();
    if (res.word) {
      store.wordFromId.word = res.word;
      store.wordFromId.gameID = 0;
      console.log(store.wordFromId.word);
    }

    const localStorage = Object.keys(window.localStorage).find(
      (key) => key === 'gameState'
    );
    if (localStorage) {
      const gameState = JSON.parse(
        window.localStorage.getItem('gameState') || ''
      ) as IGameState;
      // check if the date day, month and year are the same
      if (gameState) {
        updateStateFromStorage(gameState);
      }
    }
    store.isLoading = false;
  });

  const onLetterChange = $((index: number, letter: string) => {
    store.CurrentGuess.letter[index] = letter;
    document.getElementById('letter-' + (index + 1))?.focus();
  });

  return (
    <div class="grid h-screen place-items-center">
      <div>
        {store.wordFromId.word && (
          <div class="flex-row items-center justify-center my-16">
            <LastTimeSolutionComponent wordFromId={store.wordFromId.word?.toLocaleUpperCase()} />
          </div>
        )}
      </div>
      <div>
        {store.isLoading && (
          <div class="flex items-center justify-center">
            <Loading />
          </div>
        )}
        {!store.isComplete && store.tryCount < 6 && !store.isLoading && (
          <>
            <div class="flex items-center justify-center">
              {store.CurrentGuess.letter.map((letter, index) => (
                <Letter
                  key={index}
                  index={index}
                  letter={letter}
                  letterState={store.GuessResult.letterStates[index]}
                  onLetterChange={onLetterChange}
                />
              ))}
            </div>
            <div class="flex items-center justify-center my-16">
              <button
                data-cy="guess-button"
                disabled={store.CurrentGuess.letter.join('').length < 5}
                class="rounded-lg disabled:hover:cursor-not-allowed disabled:border-ctp-red disabled:bg-ctp-red disabled:text-ctp-crust border-4 p-2 px-4 border-ctp-blue hover:bg-ctp-blue hover:text-ctp-base"
                onClick$={async () => {
                  const result = await guessResult.submit({
                    word: store.CurrentGuess.letter
                      .join('')
                      .toLocaleLowerCase(),
                  });
                  console.log(result.value.letterStates);
                  store.GuessResult.letterStates = result.value.letterStates;
                  store.incrementTryCount();
                  animate('.tryCount', {
                    scale: [1, 1.5, 1],
                  });
                  window.localStorage.setItem(
                    'gameState',
                    JSON.stringify(store)
                  );
                }}
              >
                Raten
              </button>
            </div>
            <div class="flex items-center justify-center my-16">
              <CounterComponent tryCount={store.tryCount} />
            </div>
          </>
        )}
        {store.isComplete && !store.isLoading && (
          <WonComponent />
        )}
        {store.tryCount >= 6 && !store.isComplete && !store.isLoading && (
          <LostComponent />
        )}
      </div>
    </div>
  );
});


interface ILastTimeSolutionComponentProps {
  wordFromId: string
}

const LastTimeSolutionComponent = component$<ILastTimeSolutionComponentProps>((props) => {
  return <>
    <h3 class="text-3xl text-ctp-blue" data-cy="last-time-solution">
      Last time Solution: {props.wordFromId}
    </h3>
  </>
})

interface ICounterComponentProps {
  tryCount: number
}

const CounterComponent = component$<ICounterComponentProps>((props) => {
  return <>
    <h3 class="text-3xl tryCount" data-cy="try-count">
      {' '}
      Tries: {props.tryCount} / 6
    </h3>
  </>
})

const WonComponent = component$(() => {
  return <>
    <div class="flex items-center justify-center my-16">
      <h1 class="text-3xl text-ctp-green" data-cy="guess-success">
        You made it
      </h1>
    </div>
  </>
})


const LostComponent = component$(() => {
  return <>
    <div class="flex-row items-center justify-center my-16 text-center">
      <h2 class="text-3xl text-ctp-red" data-cy="guess-fail">
        You lost
      </h2>
      <h3 class="text-2xl pt-5">
        Come back tomorrow to reveal the solution or{' '}
        <button
          data-cy="reset-button"
          class="rounded-lg border-4 p-2 px-4 border-ctp-blue hover:bg-ctp-blue hover:text-ctp-base"
          onClick$={$(() => {
            resetLocalStorage();
            setDefaultState();
          })}
        >
          try again
        </button>
      </h3>
    </div>
  </>
});
