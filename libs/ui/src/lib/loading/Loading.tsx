import { component$, useVisibleTask$ } from "@builder.io/qwik";
import { animate } from "motion";

export default component$(() => {
    useVisibleTask$(() => {
        const sequence = async () => {

            // Color cycling
            await animate('.loading', {
                color: ["#000", "#FF00FF", "#00FFFF", "#FF0000", "#000"]
            }, { duration: 1 }).finished;

            // Glow effect
            await animate('.loading', {
                textShadow: ["0px 0px 2px #000", "0px 0px 10px #00FFFF", "0px 0px 2px #000"]
            }, { duration: 0.5 }).finished;

            setTimeout(sequence, 2000); // Wait a bit before repeating the sequence
        };

        sequence();
    });

    return (
        <>
            <h2 class="text-3xl loading" data-cy="loading-message" style="white-space: nowrap;">
                Loading ...
            </h2>
        </>
    );
});
