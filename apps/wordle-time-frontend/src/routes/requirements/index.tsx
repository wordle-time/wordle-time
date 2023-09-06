import { Resource, component$, useSignal, useStore } from "@builder.io/qwik";
import { routeLoader$, type DocumentHead, Link } from "@builder.io/qwik-city";
import fetch from "node-fetch";

export interface Requirement {
  id: string
  title: string
  referece: string
  description: string
  impact: string
  criteria: string[]
}

export const useRequirements = routeLoader$<Requirement[]>(async () => {
  const res = await fetch("http://localhost:3000/requirements")

  return (await res.json() as Requirement[]);
});

export default component$(() => {
  const requirements = useRequirements();

  return (
    <>
      <div>
        <Resource value={requirements}
          onPending={() => <div>Loading...</div>}
          onResolved={(requirements) => (
            <div >
              {requirements.map((requirement) => (
                <div key={requirement.id}>
                  <h3>{requirement.title}</h3>
                  <Link href={`/requirements/${requirement.id}`}>Read More</Link>
                </div>
              ))}
            </div>
          )}
        />
        );
      </div>
    </>)
});
