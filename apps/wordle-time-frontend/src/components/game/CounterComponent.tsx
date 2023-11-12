import { component$ } from "@builder.io/qwik"

interface ICounterComponentProps {
    tryCount: number
}

export default component$<ICounterComponentProps>((props) => {
    return <>
        <h3 class="text-3xl tryCount" data-cy="try-count">
            {' '}
            Tries: {props.tryCount} / 6
        </h3>
    </>
})