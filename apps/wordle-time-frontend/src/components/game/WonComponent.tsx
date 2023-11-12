import { component$, useVisibleTask$ } from "@builder.io/qwik";
import { animate } from "motion";

export default component$(() => {
    useVisibleTask$(() => {
        // Pulsating glow effect
        animate('#guess-success', {
            textShadow: ['0px 0px 10px #00FF00', '0px 0px 20px #00FF00', '0px 0px 10px #00FF00'],
            scale: [1, 1.1, 1]
        }, {
            duration: 1,
            repeat: Infinity,
        });
    });

    return (
        <>
            <div class="flex items-center justify-center my-16 relative">
                <h1 id="guess-success" class="text-3xl text-ctp-green" data-cy="guess-success">
                    You made it
                </h1>
            </div>
        </>
    );
});
