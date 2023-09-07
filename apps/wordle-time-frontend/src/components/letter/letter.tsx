import { QRL, component$ } from "@builder.io/qwik"
import { LetterState } from "../../routes/game"

export interface LetterProps {
  letter: string,
  index: number,
  letterState: LetterState
  onLetterChange: QRL<(index: number, letter: string) => void>;
}

export default component$<LetterProps>((props) => {

  return (

    <input type="text" class={
      ['w-24', 'h-24', 'text-center', 'mr-10', 'text-ctp-text', 'border', 'border-4', 'rounded-lg', 'bg-ctp-mantle', 'font-bold', 'uppercase text-6xl',
        { "border-ctp-green": props.letterState == 0 },
        { "border-ctp-yellow": props.letterState == 1 },
        { "border-ctp-red": props.letterState == 2 },
        { "border-ctp-mantle": props.letterState == 3 },]
    }
      value={props.letter} maxLength={1} onInput$={
        (evt) => {
          props.onLetterChange(props.index, (evt.target as HTMLInputElement).value)
        }
      } />

  )
})
