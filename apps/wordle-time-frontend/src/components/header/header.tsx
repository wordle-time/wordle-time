import { component$, useSignal, useStore, useTask$, useVisibleTask$ } from "@builder.io/qwik";
import { HiBars3Outline } from "@qwikest/icons/heroicons";
import { animate } from "motion";


export default component$(() => {
    const isHeaderExanded = useSignal(false);


    return (
        <header
            class={`sticky top-0 z-40 flex-none mx-auto w-full transition-all bg-black`}
            id="header"

        >
            <div class="py-3 px-3 mx-auto w-full md:flex md:justify-between max-w-7xl md:px-4">
                <div class="flex justify-between">
                    <a class="flex items-center py-2.5" href={"/"}>
                    </a>
                    <div class="flex items-center md:hidden">

                        <button
                            type="button"
                            class={`ml-1.5 text-gray-500 dark:text-gray-400 hover:bg-gray-100 dark:hover:bg-gray-800 focus:outline-none focus:ring-4 focus:ring-gray-200 dark:focus:ring-gray-700 rounded-lg text-sm p-2.5 inline-flex items-center transition"
                                }`}
                            aria-label="Toggle Menu"
                            onClick$={() => {
                                isHeaderExanded.value = !isHeaderExanded.value;
                            }}
                        >
                            <HiBars3Outline />
                        </button>
                    </div>
                </div>
                <div class="md:self-center flex items-center md:mb-0 ml-4">
                    {
                        isHeaderExanded.value && (
                            <nav
                                id="box"
                                class="items-center w-full h-screen md:w-auto md:flex text-gray-500 dark:text-slate-200 md:h-auto overflow-y-hidden  pr-4"
                                aria-label="Main navigation"
                            >

                                <ul class="flex flex-col pt-8 md:pt-0 md:flex-row md:self-center w-full md:w-auto text-xl md:text-lg">

                                    <li>
                                        <a
                                            href="/"
                                            class="font-medium hover:text-gray-900 dark:hover:text-white px-4 py-3 flex items-center transition duration-150 ease-in-out"
                                        >
                                            Home
                                        </a>

                                    </li>
                                    <li>
                                        <a
                                            href="/rules"
                                            class="font-medium hover:text-gray-900 dark:hover:text-white px-4 py-3 flex items-center transition duration-150 ease-in-out"
                                        >
                                            Regeln
                                        </a>

                                    </li>
                                    <li>
                                        <a
                                            href="/game"
                                            class="font-medium hover:text-gray-900 dark:hover:text-white px-4 py-3 flex items-center transition duration-150 ease-in-out"
                                        >
                                            Spiel
                                        </a>

                                    </li>
                                </ul>
                            </nav>
                        )
                    }

                </div>
            </div>
        </header>
    );
});