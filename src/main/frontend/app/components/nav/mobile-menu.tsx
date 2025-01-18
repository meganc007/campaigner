import Link from "next/link";
import MobileNavItem from "./mobile-nav-item";

export default function MobileMenu({
  toggleMenuMobile,
}: {
  toggleMenuMobile: boolean;
}) {
  return (
    <div
      className={`flex flex-col absolute top-0 bottom-0 left-0 self-end w-full py-1 pt-20 pl-2 space-y-3 bg-silver text-black font-JetBrainsMono font-medium text-lg  border-b-4 border-gunmetal drop-shadow-md shadow-gray-800 md:hidden ${
        toggleMenuMobile ? "hidden" : ""
      }`}
    >
      <MobileNavItem linkTo="/" itemText="Home" />
      <MobileNavItem linkTo="/" itemText="Locations" />
      <MobileNavItem linkTo="/" itemText="People" />
      <MobileNavItem linkTo="/" itemText="Items" />
      <MobileNavItem linkTo="/" itemText="Quests" />
      <MobileNavItem linkTo="/" itemText="Events" />
      <MobileNavItem linkTo="/" itemText="Monsters" />
    </div>
  );
}
