import { component$ } from "@builder.io/qwik"

export default component$(() => {
    return <>
        <div class="flex items-center justify-center my-16">
            <h1 class="text-3xl text-ctp-green" data-cy="guess-success">
                You made it
            </h1>
        </div>
    </>
})
