import { QRL, component$, useVisibleTask$ } from '@builder.io/qwik';
import { ILetterState } from '@wordle-time/models';
import { animate, stagger } from 'motion';

export interface LetterProps {
  letter: string;
  index: number;
  letterState: string | ILetterState;
  onLetterChange: QRL<(index: number, letter: string) => void>;
}

export default component$<LetterProps>((props) => {
  const previewChars = ['A', 'E', 'I', 'O', 'U'];

  useVisibleTask$(() => {
    animate(
      '.letter',
      {
        scale: [1, 2, 2, 1, 1],
        rotate: [0, 0, 270, 270, 0],
      },
      {
        delay: stagger(0.1),
      }
    );
  });

  return (
    <>
      <input
        cy-data={'letter-' + props.index}
        type="text"
        onFocus$={(evt) => {
          (evt.target as HTMLInputElement).select();
        }}
        placeholder={previewChars[props.index]}
        id={'letter-' + props.index}
        class={[
          'w-8',
          'letter',
          'h-8',
          'md:w-12',
          'md:h-12',
          'lg:w-24',
          'lg:h-24',
          'text-center',
          'mx-2',
          'text-ctp-text',
          'border',
          'border-4',
          'rounded-lg',
          'bg-ctp-mantle',
          'font-bold',
          'uppercase',
          'text-l',
          'md:text-lg',
          'lg:text-6xl',
          { 'border-ctp-green': props.letterState == 'CorrectSpot' },
          { 'border-ctp-yellow': props.letterState == 'WrongSpot' },
          { 'border-ctp-red': props.letterState == 'WrongLetter' },
          { 'border-ctp-sky': props.letterState == 'Empty' },
        ]}
        value={props.letter}
        maxLength={1}
        onInput$={(evt) => {
          props.onLetterChange(
            props.index,
            (evt.target as HTMLInputElement).value
          );
        }}
      />
    </>
  );
});
