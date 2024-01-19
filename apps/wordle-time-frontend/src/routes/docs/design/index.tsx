import { Resource, component$, useVisibleTask$ } from "@builder.io/qwik";
import { type DocumentHead, routeLoader$ } from '@builder.io/qwik-city';
import { animate, stagger } from 'motion';


interface IDesignChoices {
    "designEntscheidungen": IDesignChoice[]
}

interface IDesignChoice {
    "title": string,
    "description": string,
    "reasons": [
        {
            "type": string,
            "reason": string
        }
    ]
}

const designRoute = "http://127.0.0.1:8090/api/documentation/designChoices";

export const useDesignChoices = routeLoader$<IDesignChoice[]>(async () => {
    const res = await fetch(designRoute);
    return ((await res.json()) as IDesignChoices).designEntscheidungen;
});

export default component$(() => {
    const designChoices = useDesignChoices();
    useVisibleTask$(() => {
        animate(
            '.designChoice',
            // fade in from 0 to 1 and move from side in
            {
                opacity: [0, 1],
                x: [-100, 0],
            },
            {
                delay: stagger(0.25),
            }
        );
    }
    );
    return <>
        <Resource
            value={designChoices}
            onPending={() => <div>Loading...</div>}
            onResolved={(designChoices) => (
                <div class="px-4" >
                    {designChoices.map((designChoice) => (
                        <div key={designChoice.title}
                            class="designChoice border-2 border-transparent rounded-xl p-4 hover:border-ctp-yellow">
                            <h3 class="text-3xl">
                                {designChoice.title}
                            </h3>
                            <p class="text-xl">{designChoice.description}</p>
                            <h4 class="text-sm pt-4">
                                Die Verwendung von {designChoice.title} bietet mehrere Vorteile für das Projekt, einschließlich:
                            </h4>
                            <ul>
                                {designChoice.reasons.map((reason) => (
                                    <li class="py-2 pb-4 text-sm list-item list-inside list-disc" key={reason.type}>
                                        {reason.type}: {reason.reason}
                                    </li>
                                ))}
                            </ul>
                        </div>
                    ))}
                </div>
            )}
        />
    </>;
}
);


export const head: DocumentHead = {
  title: 'Design Choices - Wordle Time',
  meta: [
    {
      name: 'description',
      content: 'Design Choices for Wordle Time',
    },
  ],
};
