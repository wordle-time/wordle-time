import { ILetterState } from './letter';

export interface IGuessResult {
  letterStates: ILetterState[];
}

export interface ICurrentGuess {
  letter: string[];
}

export interface IWordFromId {
  word?: string;
  gameID?: number;
  date?: string;
}
