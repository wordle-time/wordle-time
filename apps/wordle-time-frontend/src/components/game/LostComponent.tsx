import { component$, $ } from "@builder.io/qwik";


const resetLocalStorage = () => {
    window.localStorage.removeItem('gameState');
};

const setDefaultState = () => {
    window.location.reload();
};

export default component$(() => {
    return <>
        <div class="flex-row items-center justify-center my-16 text-center">
            <h2 class="text-3xl text-ctp-red" data-cy="guess-fail">
                You lost
            </h2>
            <h3 class="text-2xl pt-5">
                Come back tomorrow to reveal the solution or{' '}
                <button
                    data-cy="reset-button"
                    class="rounded-lg border-4 p-2 px-4 border-ctp-blue hover:bg-ctp-blue hover:text-ctp-base"
                    onClick$={$(() => {
                        resetLocalStorage();
                        setDefaultState();
                    })}
                >
                    try again
                </button>
            </h3>
        </div>
    </>
});