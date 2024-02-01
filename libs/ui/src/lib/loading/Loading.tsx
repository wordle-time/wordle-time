import { component$, useVisibleTask$ } from "@builder.io/qwik";
import { animate } from "motion";


export default component$(() => {
    useVisibleTask$(() => {

        // Color cycling
        animate('#loading-message', {
            color: ["#000", "#FF00FF", "#00FFFF", "#FF0000", "#000"]
        }, { duration: 1 }).finished;

        // Glow effect
        animate('#loading-message', {
            textShadow: ["0px 0px 2px #000", "0px 0px 10px #00FFFF", "0px 0px 2px #000"]
        }, { duration: 0.5 }).finished;
    });

    return (
        <>
            <h2 id="loading-message" class="text-3xl loading" data-cy="loading-message" style="white-space: nowrap;">
                Loading ...
            </h2>
        </>
    );
});
