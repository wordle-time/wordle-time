import { component$, Slot } from '@builder.io/qwik';
import Header from '../components/navbar/navbar';
import Footer from '../components/footer/footer';

export default component$(() => {
  return (
    <>
      <main class="min-h-screen">
        <Header />
        <section>
          <Slot />
        </section>
      </main>
      <Footer />
    </>
  );
});
