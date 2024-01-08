import { Resource, component$ } from '@builder.io/qwik';
import { Link, routeLoader$ } from '@builder.io/qwik-city';
import { IRequirement } from '@wordle-time/models';

export const useRequirement = routeLoader$<IRequirement>(
  async ({ params, redirect }) => {
    const res = await fetch(
      'http://127.0.0.1:8090/api/documentation/requirements/' + params.id
    );

    if (!res.ok) {
      throw redirect(301, '..');
    }

    return (await res.json()) as IRequirement;
  }
);

export default component$(() => {
  const requirement = useRequirement();
  return (
    <div class="p-20">
      <Resource
        value={requirement}
        onPending={() => <div>loading...</div>}
        onResolved={(requirement) => (

          <div>
            <h1 class="text-6xl pt-8 text-center">{requirement.id + " " + requirement.title}</h1>
            <p class="text-xl p-12 text-center">{requirement.description}</p>

            <p class="text-xl pb-6" >{requirement.impact}</p>
            <ul class="list-disc pl-5">
              {requirement.criteria.map((c) => (
                <li class="p-2 list-inside" key={c}>
                  {c}
                </li>
              ))}
            </ul>
            <p class="text-xl py-6">Sequenzdiagramm:</p>
            <img
              src={
                'http://localhost:8090/api/requirements/' +
                requirement.id +
                '/' +
                requirement.seqPic
              }
              width="400"
              height="400"
              class="mx-auto"
            />
            <p class="text-xl py-6">Aktivitätsdiagramm:</p>
            <img
              src={
                'http://localhost:8090/api/requirements/' +
                requirement.id +
                '/' +
                requirement.actPic
              }
              width="400"
              height="400"
              class="mx-auto"
            />
            <p class="text-xl py-6">Testfälle:</p>
            <ul class="list-disc pl-5 divide-y divide-dashed">
              {requirement.testCases.map((t) => (
                <li class="p-2 list-inside py-4 text-lg" key={t.name}>
                  {t.name}
                  <ol class="list list-decimal text-sm pl-12">
                    <li class="py-2">{t.requirement}</li>
                    <li class="py-2">{t.action}</li>
                    <li class="py-2">{t.expectation}</li>
                  </ol>

                  <img
                    src={
                      'http://localhost:8090/api/requirements/' +
                      requirement.id +
                      '/' +
                      t.testPic
                    }
                    width="200"
                    height="200"
                    class="py-2 "
                  />
                </li>
              ))}
            </ul>
            <div class="mt-6">

              <a
                href={requirement.reference}
                class="bg-ctp-crust mt-4 text-ctp-blue hover:underline rounded-md px-3 py-2 text-sm font-medium"
              >
                Auf GitHub ansehen
              </a>
            </div>
          </div>
        )}
      />

      <div class="mt-12">

        <Link
          href="/requirements"
          class="bg-ctp-crust text-ctp-blue hover:underline rounded-md px-3 py-2 text-sm font-medium"
        >
          Alle Anforderungen
        </Link>
      </div>
    </div>
  );
});
