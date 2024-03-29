import { component$, useVisibleTask$, useSignal } from "@builder.io/qwik";
import { animate } from "motion";

const nextGameAtRoute = 'http://127.0.0.1:8090/api/guess/nextWordAt';

export const getNextWordAt = async () => {
    const response = await fetch(nextGameAtRoute);
    const result = await response.json();
    return result as TimeInterface;
};

interface TimeInterface {
    time: string;
}

export default component$(() => {
    const remainingTime = useSignal("");

    useVisibleTask$(() => {
        const nextGameAt = getNextWordAt();
        nextGameAt.then((nextGameAtResolved) => {
            setInterval(() => {
                // get difference between now and nextGameAt
                const now = new Date();
                const next = new Date(nextGameAtResolved.time);
                const diff = next.getTime() - now.getTime();


                let seconds: string | number = Math.floor((diff / 1000) % 60);
                let minutes: string | number = Math.floor((diff / (1000 * 60)) % 60);
                let hours: string | number = Math.floor((diff / (1000 * 60 * 60)) % 24);

                hours = (hours < 10) ? "0" + hours : hours;
                minutes = (minutes < 10) ? "0" + minutes : minutes;
                seconds = (seconds < 10) ? "0" + seconds : seconds;

                remainingTime.value = hours + ":" + minutes + ":" + seconds;

            }, 1000);
        });

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
            <div id="guess-success" class="flex flex-col items-center justify-center my-16 relative" >
                {remainingTime.value !== "" &&
                    <p class="text-3xl text-ctp-green">
                        Next word in {remainingTime.value}
                    </p>
                }
                <h1 class="text-3xl text-ctp-green" data-cy="guess-success">
                    You made it
                </h1>
            </div>
        </>
    );
});
