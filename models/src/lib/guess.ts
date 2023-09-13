import { LetterState } from './letter';

export interface GuessResult {
  letterStates: LetterState[];
}

export interface CurrentGuess {
  letter: string[];
}
