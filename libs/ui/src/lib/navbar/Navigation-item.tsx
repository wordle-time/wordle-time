import { component$ } from '@builder.io/qwik';
import { Link } from '@builder.io/qwik-city';
import { INavigationItem } from './interface/navigation';

export default component$<INavigationItem>((props) => {
  return (
    <Link
      data-cy={'navbutton-' + props.text}
      class="m-3 text-ctp-blue hover:underline rounded-md px-3 text-sm font-medium"
      href={props.href}
    >
      {props.text}
    </Link>
  );
});
