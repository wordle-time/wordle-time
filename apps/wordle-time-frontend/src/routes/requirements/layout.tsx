import { component$, Slot } from '@builder.io/qwik';
import { Link } from '@builder.io/qwik-city';

export default component$(() => {
    return (
        <>
            <section class="p-20">

                <Slot />
                <Link href='/' class="bg-ctp-crust text-ctp-blue hover:underline rounded-md px-3 py-2 text-sm font-medium">Home</Link>
            </section>
        </>
    );
});
