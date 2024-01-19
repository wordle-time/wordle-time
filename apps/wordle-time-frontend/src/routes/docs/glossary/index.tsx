import { Resource, component$ } from "@builder.io/qwik"
import { type DocumentHead, routeLoader$ } from '@builder.io/qwik-city';
import { s } from "vitest/dist/reporters-5f784f42";

interface IGlossaries {
    "glossaries": IGlossary[]
}

interface IGlossary {
    "name": string,
    "description": string,
    "url": string | null | undefined
}

const glossaryRoute = "http://127.0.0.1:8090//api/documentation/glossaries";

export const useGlossaries = routeLoader$<IGlossary[]>(async () => {
    const res = await fetch(glossaryRoute);
    return ((await res.json()) as IGlossaries).glossaries;
});

export default component$(() => {
    const glossaries = useGlossaries();
    return <>
        <Resource
            value={glossaries}
            onPending={() => <div>Loading...</div>}
            onResolved={(glossaries) => (
                <div class="px-4" >
                    {glossaries.map((glossary) => (
                        <div key={glossary.name} onClick$={() => {
                            if (glossary.url) {
                                window.open(glossary.url);
                            }
                        }}
                            class={["glossary border-2 border-transparent rounded-xl p-4 hover:border-ctp-yellow ",
                                {
                                    "cursor-pointer": glossary.url != (null || undefined)
                                },
                                {
                                    "cursor-default": glossary.url == (null || undefined)
                                }]}>
                            <h3 class="text-3xl">
                                {glossary.name}
                            </h3>
                            <p class="text-xl">{glossary.description}</p>
                        </div>
                    ))}
                </div>
            )}
        />
    </>
})

export const head: DocumentHead = {
  title: 'Glossary - Wordle Time',
  meta: [
    {
      name: 'description',
      content: 'Glossary for Wordle Time',
    },
  ],
};
