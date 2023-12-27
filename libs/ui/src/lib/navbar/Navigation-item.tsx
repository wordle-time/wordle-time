import { PropFunction, component$ } from '@builder.io/qwik';
import { Link } from '@builder.io/qwik-city';

export interface INavigationItem {
  href: string;
  text: string;
  closePanel$: PropFunction<() => false>;
}

export default component$<INavigationItem>((props) => {
  return (
    <Link
      data-cy={'navbutton-' + props.text}
      class="m-3 text-ctp-blue hover:underline rounded-md px-3 text-sm font-medium"
      href={props.href}
      onClick$={props.closePanel$}
    >
      {props.text}
    </Link >
  );
});
