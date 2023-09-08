import { QRL, component$ } from "@builder.io/qwik"
import { LetterState } from "../../routes/game"

export interface LetterProps {
  letter: string,
  index: number,
  letterState: string
  onLetterChange: QRL<(index: number, letter: string) => void>;
}

export default component$<LetterProps>((props) => {

  return (
    <>
      <input type="text" class={
        ['w-8', 'h-8', 'md:w-12', 'md:h-12', 'lg:w-24', 'lg:h-24', 'text-center', 'mx-2', 'text-ctp-text', 'border', 'border-4', 'rounded-lg', 'bg-ctp-mantle', 'font-bold', 'uppercase', 'text-l', 'md:text-lg', 'lg:text-6xl', 'hover:scale-125',
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
