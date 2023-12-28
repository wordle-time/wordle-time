import { component$ } from '@builder.io/qwik';
import { Link } from '@builder.io/qwik-city';

interface NavLink {
  href: string;
  text: string;
}

export default component$(() => (
  <footer class="justify-self-end pt-16 pb-8 lg:pt-24 lg:pb-1 bg-gradient-to-b from-ctp-base to-ctp-surface0">
    <div class="px-4 mx-auto max-w-8xl lg:px-4">
      <nav class="grid gap-12 lg:grid-cols-5 lg:gap-18">
        <div class="col-span-2">
          <Link href="/" class="flex mb-6">
            <span class="text-2xl text-ctp-text font-semibold">
              Wordle Time
            </span>
          </Link>
          <p class="text-ctp-subtext0">
            Wordle Time is a word game where players try to guess the secret
            word of the day in just six tries.
          </p>
        </div>
        {renderSection("Dokumentation", [
          { href: "/docs/design", text: "Designentscheidungen" },
          { href: "/docs/glossary", text: "Glossar" },
          { href: "/docs/requirements", text: "Anforderungen" },
        ])}
        {renderSection("Kontakt", [
          { href: "https://discord.gg/PRyZGKPB", text: "Discord" },
          { href: "https://github.com/wordle-time/", text: "Github" },
        ])}
      </nav>
      <hr class="my-8 border-gray-200 dark:border-gray-700 lg:my-12" />
      <span class="block font-normal text-center ">Wintersemester 2023</span>
    </div>
  </footer>
));

function renderSection(title: string, links: NavLink[]) {
  return (
    <div>
      <h3 class="mb-6 text-sm font-semibold text-ctp-subtext1 uppercase">
        {title}
      </h3>
      <ul>
        {links.map((link, index) => (
          <li key={index} class="mb-4">
            <LinkOrExternalLink key={index} href={link.href} text={link.text} />
          </li>
        ))}
      </ul>
    </div>
  );
}

function LinkOrExternalLink({ href, text }: NavLink) {
  const isExternal = href.startsWith("http");
  const linkProps = isExternal
    ? { href, target: "_blank", rel: "noopener noreferrer" }
    : { href };

  return (
    <Link
      {...linkProps}
      data-cy={`${text.toLowerCase().replace(/\s+/g, '-')}-link`}
      class="font-medium text-ctp-blue hover:underline"
    >
      {text}
    </Link>
  );
}
