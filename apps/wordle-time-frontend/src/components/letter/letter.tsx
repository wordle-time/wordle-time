import { QRL, component$, useVisibleTask$ } from "@builder.io/qwik"
import { LetterState } from "@wordle-time/models";
import { animate, stagger } from "motion";

export interface LetterProps {
  letter: string,
  index: number,
  letterState: string | LetterState,
  onLetterChange: QRL<(index: number, letter: string) => void>;
}

export default component$<LetterProps>((props) => {
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
    )
  })



  return (
    <>
      <input type="text" placeholder="A" class={
        ['w-8', 'letter', 'h-8', 'md:w-12', 'md:h-12', 'lg:w-24', 'lg:h-24', 'text-center', 'mx-2', 'text-ctp-text', 'border', 'border-4', 'rounded-lg', 'bg-ctp-mantle', 'font-bold', 'uppercase', 'text-l', 'md:text-lg', 'lg:text-6xl', 'hover:scale-125',
          { "border-ctp-green": props.letterState == "CorrectSpot" },
          { "border-ctp-yellow": props.letterState == "WrongSpot" },
          { "border-ctp-red": props.letterState == "WrongLetter" },
          { "border-ctp-sky": props.letterState == "Empty" },]
      }
        value={props.letter} maxLength={1} onInput$={
          (evt) => {
            props.onLetterChange(props.index, (evt.target as HTMLInputElement).value)
          }
        } />
    </>
  )
})
