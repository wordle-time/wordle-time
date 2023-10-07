import { component$ } from '@builder.io/qwik';
import { Link } from '@builder.io/qwik-city';

export default component$(() => {
  return (
    <footer class="justify-self-end pt-16 pb-8 lg:pt-24 lg:pb-1 to-ctp-surface0 from-ctp-base bg-gradient-to-b">
      <div class="px-4 mx-auto max-w-8xl lg:px-4">
        <div class="grid gap-12 lg:grid-cols-5 lg:gap-18">
          <div class="col-span-2">
            <a class="flex mb-6" href="/">
              <span style="box-sizing: border-box; display: inline-block; overflow: hidden; width: initial; height: initial; background: none; opacity: 1; border: 0px; margin: 0px; padding: 0px; position: relative; max-width: 100%;">
                <span style="box-sizing: border-box; display: block; width: initial; height: initial; background: none; opacity: 1; border: 0px; margin: 0px; padding: 0px; max-width: 100%;"></span>
              </span>
              <span class="self-center  text-2xl text-ctp-text font-semibold ">
                Wordle Time
              </span>
            </a>
            <p class="text-ctp-subtext0">
              Wordle Time ist ein Wortspiel, bei dem Spieler versuchen, das
              geheime Wort des Tages in nur sechs Versuchen zu erraten.
            </p>
          </div>
          <div>
            <h3 class="mb-6 text-sm font-semibold text-ctp-subtext1 uppercase ">
              Dokumentation
            </h3>
            <ul>
              <li class="mb-4">
                <Link
                  data-cy="design-link"
                  class="font-medium text-ctp-blue  hover:underline"
                  href="/design"
                >
                  Designentscheidungen
                </Link>
              </li>
              <li class="mb-4">
                <Link
                  data-cy="glossary-link"
                  class="font-medium  text-ctp-blue hover:underline"
                  href="/glossary"
                >
                  Glossar
                </Link>
              </li>
              <li class="mb-4">
                <Link
                  data-cy="non-goals-link"
                  class="font-medium  text-ctp-blue hover:underline"
                  href="/non-goals"
                >
                  Nicht-Ziele
                </Link>
              </li>
            </ul>
          </div>
          <div>
            <h3 class="mb-6 text-sm font-semibold text-ctp-subtext1 uppercase ">
              Support
            </h3>
            <ul>
              <li class="mb-4">
                <Link
                  data-cy="api-spec-link"
                  class="font-medium  text-ctp-blue hover:underline"
                  href="/api-spec"
                >
                  API Spezifikation
                </Link>
              </li>
              <li class="mb-4">
                <Link
                  data-cy="requirements-link"
                  class="font-medium  text-ctp-blue hover:underline"
                  href="/requirements"
                >
                  Anforderungen
                </Link>
              </li>
            </ul>
          </div>
          <div>
            <h3 class="mb-6 text-sm font-semibold text-ctp-subtext1 uppercase ">
              Kontakt
            </h3>
            <ul>
              <li class="mb-4">
                <a
                  data-cy="discord-link"
                  href="https://discord.gg/PRyZGKPB"
                  rel="noreferrer nofollow"
                  class="font-medium  text-ctp-blue hover:underline"
                >
                  Discord
                </a>
              </li>
              <li class="mb-4">
                <a
                  data-cy="github-link"
                  href="https://github.com/wordle-time/"
                  rel="noreferrer nofollow"
                  class="font-medium  text-ctp-blue hover:underline"
                >
                  Github
                </a>
              </li>
            </ul>
          </div>
        </div>
        <hr class="my-8 border-gray-200 dark:border-gray-700 lg:my-12" />
        <span class="block font-normal text-center ">Wintersemester 2023 </span>
      </div>
    </footer>
  );
});
