import { Resource, component$ } from "@builder.io/qwik";
import { routeLoader$ } from "@builder.io/qwik-city";
import { Requirement } from "..";

export const useRequirement = routeLoader$<Requirement>(async ({ params, redirect }) => {
  const res = await fetch("http://localhost:3000/requirements/" + params.id);

  if (!res.ok) {
    throw redirect(301, "..")
  }

  return (await res.json() as Requirement)

})


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
          </div>)} />
    </div >
  )
})
