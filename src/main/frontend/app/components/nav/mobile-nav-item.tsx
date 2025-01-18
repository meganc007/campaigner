import Link from "next/link";

export default function MobileNavItem({
  linkTo,
  itemText,
}: {
  linkTo: string;
  itemText: string;
}) {
  return (
    <div className="mobile-nav-item">
      <Link href={linkTo}>{itemText}</Link>
    </div>
  );
}
