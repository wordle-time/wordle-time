import { Resource, component$ } from '@builder.io/qwik';
import { routeLoader$, Link, type DocumentHead } from '@builder.io/qwik-city';
import {
  IRequirement,
  Requirements as IRequirements,
} from '@wordle-time/models';
import fetch from 'node-fetch';

export const useRequirements = routeLoader$<IRequirement[]>(async () => {
  const res = await fetch('http://127.0.0.1:8090/api/documentation/requirements');
  return ((await res.json()) as IRequirements).requirements;
});

export default component$(() => {
  const requirements = useRequirements();

  return (
    <>
      <Resource
        value={requirements}
        onPending={() => <div>Loading...</div>}
        onResolved={(requirements) => (
          <div class="flex flex-wrap" >
            {requirements.map((requirement) => (
              <Link key={requirement.id}
                href={`/docs/requirements/${requirement.id}`}
              >
                <div class="w-64 h-72 m-6 border-2 rounded-xl p-4 hover:border-ctp-yellow ">
                  <h3 class="text-5xl">
                    {requirement.id}
                  </h3>
                  <h4 class="text-xl">{requirement.title}</h4>
                  <p class="py-2 pb-4 text-sm">{requirement.description}</p>
                </div>
              </Link>
            ))}
          </div>
        )}
      />
    </>
  );
});

export const head: DocumentHead = {
  title: 'Requirements - Wordle Time',
  meta: [
    {
      name: 'description',
      content: 'Wordle Time - Requirements for Wordle Time',
    },
  ],
};
