import { $, component$, useSignal } from '@builder.io/qwik';
import Icon from '../icon/Icon';
import NavigationItem from './Navigation-item';

export default component$(() => {
  const isHamburgerOpen = useSignal(false);
  const closePanel$ = $(() => isHamburgerOpen.value = false);

  return (
    <nav class="from-ctp-surface0 to-ctp-base bg-gradient-to-b">
      <div class="mx-auto max-w-7xl px-2 sm:px-6 lg:px-8">
        <div class="relative flex h-16 items-center justify-between">
          <div class="absolute inset-y-0 left-0 flex items-center sm:hidden">
            <button
              data-cy="hamburger-button"
              onClick$={() => (isHamburgerOpen.value = !isHamburgerOpen.value)}
              type="button"
              class="relative inline-flex items-center justify-center rounded-md p-2 text-gray-400 hover:bg-gray-700 hover:text-white focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white"
              aria-controls="mobile-menu"
              aria-expanded="false"
            >
              <span class="absolute -inset-0.5"></span>
              <span class="sr-only">Open main menu</span>

              {!isHamburgerOpen.value ? (
                <svg
                  class="block h-6 w-6"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke-width="1.5"
                  stroke="currentColor"
                  aria-hidden="true"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"
                  />
                </svg>
              ) : (
                <svg
                  class="h-6 w-6"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke-width="1.5"
                  stroke="currentColor"
                  aria-hidden="true"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
              )}
            </button>
          </div>
          <div class="flex flex-1 items-center justify-center sm:items-stretch sm:justify-start">
            <div class="flex flex-shrink-0 items-center">
              <Icon />
              <div class="hidden sm:ml-6 sm:block">
                <NavigationItem href="/" text="Home" closePanel$={closePanel$} />
                <NavigationItem href="/game" text="Game" closePanel$={closePanel$} />
                <NavigationItem href="/docs" text="Docs" closePanel$={closePanel$} />
              </div>
            </div>
          </div>
        </div>
      </div>

      {isHamburgerOpen.value && (
        <div class="sm:hidden flex flex-col h-screen" id="mobile-menu">
          <NavigationItem
            href="/"
            text="Home"
            closePanel$={closePanel$}
          />
          <NavigationItem
            href="/game"
            text="Game"
            closePanel$={closePanel$}
          />
          <NavigationItem
            href="/docs"
            text="Docs"
            closePanel$={closePanel$}
          />
        </div>
      )}
    </nav>
  );
});
