import { component$ } from "@builder.io/qwik";

export default component$(() => {
    return <>
        <h2 class="text-3xl loading" data-cy="loading-message">
            Loading ...
        </h2>
    </>
})