import { component$ } from "@builder.io/qwik";

interface ILastTimeSolutionComponentProps {
    wordFromId: string
}

export default component$<ILastTimeSolutionComponentProps>((props) => {
    return <>
        <h3 class="text-3xl text-ctp-blue" data-cy="last-time-solution">
            Last time Solution: {props.wordFromId}
        </h3>
    </>
})