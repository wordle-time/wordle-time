import { Resource, component$ } from '@builder.io/qwik';
import { Link, routeLoader$ } from '@builder.io/qwik-city';
import { IRequirement } from '@wordle-time/models';

export const useRequirement = routeLoader$<IRequirement>(
  async ({ params, redirect }) => {
    const res = await fetch(
      'http://localhost:8090/api/requirements/' + params.id
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
    <div>
      <Resource
        value={requirement}
        onPending={() => <div>loading...</div>}
        onResolved={(requirement) => (
          <div>
            <h1>{requirement.title}</h1>
            <p>{requirement.description}</p>
            <a
              href={requirement.reference}
              class="bg-ctp-crust text-ctp-blue hover:underline rounded-md px-3 py-2 text-sm font-medium"
            >
              Auf GitHub ansehen{' '}
            </a>
            <p>{requirement.impact}</p>
            <ul class="list-disc pl-5">
              {requirement.criteria.map((c) => (
                <li class="" key={c}>
                  {c}
                </li>
              ))}
            </ul>
            <img src={"http://localhost:8090/api/requirements/" + requirement.id + "/" + requirement.seqPic} width="200" height="200" />
          </div>
        )}
      />
      <Link
        href="/requirements"
        class="bg-ctp-crust text-ctp-blue hover:underline rounded-md px-3 py-2 text-sm font-medium"
      >
        Anforderungen
      </Link>
    </div>
  );
});
