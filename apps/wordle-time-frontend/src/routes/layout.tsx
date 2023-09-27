import { component$, Slot } from '@builder.io/qwik';
import { Footer, Navbar } from '@wordle-time/ui';

export default component$(() => {
  return (
    <>
      <main class="ctp-latte dark:ctp-mocha text-ctp-text">
        <Navbar />
        <section class="bg-ctp-base min-h-screen text-ctp-text">
          <Slot />
        </section>
        <Footer />
      </main>
    </>
  );
});
