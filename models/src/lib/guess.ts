import { LetterState } from './letter';

export interface IGuessResult {
  letterStates: LetterState[];
}

export interface ICurrentGuess {
  letter: string[];
}
