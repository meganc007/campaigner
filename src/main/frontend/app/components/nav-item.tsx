import Link from "next/link";
import Image from "next/image";

export default function NavItem({
  linkTo,
  src,
  itemText,
}: {
  linkTo: string;
  src: string;
  itemText: string;
}) {
  return (
    <div className="p-2 rounded-r-sm border-l-4 border-transparent hover:border-l-4 hover:border-black hover:bg-celestial_blue transition ease-in-out duration-150">
      <Link href={linkTo} className="mx-auto">
        <Image src={src} alt="" height={30} width={30} className="mx-auto" />
        {itemText}
      </Link>
    </div>
  );
}
