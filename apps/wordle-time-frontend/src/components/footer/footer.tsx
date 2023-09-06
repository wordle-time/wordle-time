import { component$ } from "@builder.io/qwik";

export default component$(() => {
    return (
        <footer class="justify-self-end pt-16 pb-8 lg:pt-24 lg:pb-10">
            <div class="px-4 mx-auto max-w-8xl lg:px-4">
                <div class="grid gap-12 lg:grid-cols-6 lg:gap-18">
                    <div class="col-span-2">
                        <a class="flex mb-6" href="/">
                            <span style="box-sizing: border-box; display: inline-block; overflow: hidden; width: initial; height: initial; background: none; opacity: 1; border: 0px; margin: 0px; padding: 0px; position: relative; max-width: 100%;">
                                <span style="box-sizing: border-box; display: block; width: initial; height: initial; background: none; opacity: 1; border: 0px; margin: 0px; padding: 0px; max-width: 100%;">
                                </span>
                            </span>
                            <span class="self-center ml-3 text-2xl font-semibold text-gray-900 dark:text-white">
                                Flowbite</span>
                        </a>
                        <p class="text-gray-600 dark:text-gray-400">
                            Wordle Time ist ein Wortspiel, bei dem Spieler versuchen, das geheime Wort des Tages in nur sechs Versuchen zu erraten.</p>
                    </div>
                    <div>
                        <h3 class="mb-6 text-sm font-semibold text-gray-400 uppercase dark:text-white">
                            Resources</h3>
                        <ul>
                            <li class="mb-4">
                                <a href="https://flowbite.com/docs/getting-started/introduction/" class="font-medium text-gray-600 dark:text-gray-400 dark:hover:text-white hover:underline">
                                    Dokumentation</a>
                            </li>
                            <li class="mb-4">
                                <a class="font-medium text-gray-600 dark:text-gray-400 dark:hover:text-white hover:underline" href="/blocks/">
                                    Flowbite Blocks</a>
                            </li>
                            <li class="mb-4">
                                <a class="font-medium text-gray-600 dark:text-gray-400 dark:hover:text-white hover:underline" href="/icons/">
                                    Flowbite Icons</a>
                            </li>
                            <li class="mb-4">
                                <a class="font-medium text-gray-600 dark:text-gray-400 dark:hover:text-white hover:underline" href="/figma/">
                                    Flowbite Figma</a>
                            </li>
                            <li class="mb-4">
                                <a class="font-medium text-gray-600 dark:text-gray-400 dark:hover:text-white hover:underline" href="/pro/">
                                    Pro version</a>
                            </li>
                        </ul>
                    </div>
                    <div>
                        <h3 class="mb-6 text-sm font-semibold text-gray-400 uppercase dark:text-white">
                            Help &amp; support</h3>
                        <ul>
                            <li class="mb-4">
                                <a class="font-medium text-gray-600 dark:text-gray-400 dark:hover:text-white hover:underline" href="/contact/">
                                    Contact us</a>
                            </li>
                            <li class="mb-4">
                                <a class="font-medium text-gray-600 dark:text-gray-400 dark:hover:text-white hover:underline" href="/dashboard/support/">
                                    Support center</a>
                            </li>
                            <li class="mb-4 flex items-center">
                                <a class="font-medium text-gray-600 dark:text-gray-400 dark:hover:text-white hover:underline" href="/work-with-us/">
                                    Work with us</a>
                                <span class="bg-blue-100 text-blue-800 text-xs font-medium px-2.5 py-0.5 rounded dark:bg-blue-900 dark:text-blue-300 ml-2">
                                    New</span>
                            </li>
                        </ul>
                    </div>
                    <div>
                        <h3 class="mb-6 text-sm font-semibold text-gray-400 uppercase dark:text-white">
                            Follow us</h3>
                        <ul>
                            <li class="mb-4">
                                <a href="https://discord.gg/4eeurUVvTy" rel="noreferrer nofollow" class="font-medium text-gray-600 dark:text-gray-400 dark:hover:text-white hover:underline">
                                    Discord</a>
                            </li>
                            <li class="mb-4">
                                <a href="https://github.com/themesberg" rel="noreferrer nofollow" class="font-medium text-gray-600 dark:text-gray-400 dark:hover:text-white hover:underline">
                                    Github</a>
                            </li>
                            <li class="mb-4">
                                <a href="https://twitter.com/zoltanszogyenyi" rel="noreferrer nofollow" class="font-medium text-gray-600 dark:text-gray-400 dark:hover:text-white hover:underline">
                                    Twitter</a>
                            </li>
                        </ul>
                    </div>
                    <div>
                        <h3 class="mb-6 text-sm font-semibold text-gray-400 uppercase dark:text-white">
                            Legal</h3>
                        <ul>
                            <li class="mb-4">
                                <a class="font-medium text-gray-600 dark:text-gray-400 dark:hover:text-white hover:underline" href="/license/">
                                    License (EULA)</a>
                            </li>
                            <li class="mb-4">
                                <a class="font-medium text-gray-600 dark:text-gray-400 dark:hover:text-white hover:underline" href="/privacy-policy/">
                                    Privacy policy</a>
                            </li>
                            <li class="mb-4">
                                <a class="font-medium text-gray-600 dark:text-gray-400 dark:hover:text-white hover:underline" href="/terms-and-conditions/">
                                    Terms &amp; conditions</a>
                            </li>
                            <li class="mb-4">
                                <a class="font-medium text-gray-600 dark:text-gray-400 dark:hover:text-white hover:underline" href="/brand/">
                                    Brand guideline</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <hr class="my-8 border-gray-200 dark:border-gray-700 lg:my-12" />
                <span class="block font-normal text-center text-gray-600 dark:text-gray-400">
                    Wintersemester 2023 </span>
            </div>
        </footer>
    )
});