import { PropFunction, component$, useSignal } from "@builder.io/qwik";
import NavigationItem from "./Navigation-item";

export interface IDropDown {
    closePanel$: PropFunction<() => false>;
}

export default component$<IDropDown>((props) => {
    const isDocsOpen = useSignal(false);

    return <>
        {
            !isDocsOpen.value && (
                <button onClick$={() => isDocsOpen.value = !isDocsOpen.value} class="m-3 text-ctp-blue hover:underline rounded-md px-3 text-sm font-medium">Docs</button >
            )
        }
        {
            isDocsOpen.value &&
            (
                <>
                    <NavigationItem href="/docs/glossary" text="Glossary" closePanel$={props.closePanel$} />
                    <NavigationItem href="/docs/design" text="Designchoises" closePanel$={props.closePanel$} />
                    <NavigationItem href="/docs/requirements" text="Requirements" closePanel$={props.closePanel$} />
                </>
            )
        }
    </>
})