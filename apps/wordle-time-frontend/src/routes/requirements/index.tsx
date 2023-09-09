import { Resource, component$ } from "@builder.io/qwik";
import { routeLoader$, Link } from "@builder.io/qwik-city";
import fetch from "node-fetch";

export interface Requirement {
  id: string
  title: string
  referece: string
  description: string
  impact: string
  criteria: string[]
}


export interface Requirements {
  requirements: Requirement[]
}

export const useRequirements = routeLoader$<Requirement[]>(async () => {
  const res = await fetch("http://localhost:8090/api/requirements");
  return (await res.json() as Requirements).requirements;
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
                <div key={requirement.id} class="m-20">

                  <h3><span>{requirement.id} -</span>{requirement.title}</h3>
                  <Link href={`/requirements/${requirement.id}`} class="bg-ctp-crust text-ctp-blue hover:underline rounded-md px-3 py-2 text-sm font-medium">Read More</Link>
                </div>
              ))}
            </div>
          )}
        />
      </div>
    </>)
});
