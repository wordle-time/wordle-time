import { component$, useVisibleTask$ } from '@builder.io/qwik';
import type { DocumentHead } from '@builder.io/qwik-city';
import { animate, spring } from 'motion';

export default component$(() => {

  useVisibleTask$(() => {
    animate(
      '#h1',
      // go from 0 to 100 opacity over 1 second
      // go 20px down
      {
        opacity: [0, 1],
        y: [-20, 0],
        scale: [0.5, 1],
      },
      {
        duration: 0.3,
        easing: 'ease-in-out',

      })
  });



  return (
    <div>
      <h1 id='h1' class="text-center pt-6">
        Welcome wordle-time-frontend <span class="lightning">⚡️</span>
      </h1>
    </div>
  );
});

export const head: DocumentHead = {
  title: 'Welcome to Qwik',
  meta: [
    {
      name: 'description',
      content: 'Qwik site description',
    },
  ],
};
