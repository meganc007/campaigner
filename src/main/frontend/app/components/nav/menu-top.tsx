"use client";
import { useState } from "react";
import NavItem from "./nav-item";
import MobileMenu from "./mobile-menu";
import HamburgerButton from "./hamburger-btn";

export default function MenuTop() {
  const [toggleMenuButton, setToggleMenuButton] = useState(false);
  const [toggleMenuMobile, settoggleMenuMobile] = useState(true);

  const handleClick = () => {
    setToggleMenuButton((prevState) => !prevState);
    settoggleMenuMobile((prevState) => !prevState);
  };

  return (
    <div className="sticky w-full z-10 top-0 left-0 items-center">
      <div className="bg-silver border-b-4 border-gunmetal drop-shadow-md shadow-gray-800 p-4">
        {/* <!-- Desktop Menu --> */}
        <nav className="flex justify-center font-JetBrainsMono font-medium text-base">
          <div className="hidden md:flex md:flex-row md:space-x-2 lg:space-x-8">
            <NavItem linkTo="#" src="/globe.svg" itemText="Locations" />
            <NavItem linkTo="#" src="/globe.svg" itemText="People" />
            <NavItem linkTo="#" src="/globe.svg" itemText="Items" />
            <NavItem linkTo="/" src="/file.svg" itemText="Home" />
            <NavItem linkTo="#" src="/globe.svg" itemText="Quests" />
            <NavItem linkTo="#" src="/globe.svg" itemText="Events" />
            <NavItem linkTo="#" src="/globe.svg" itemText="Monsters" />
          </div>
          <HamburgerButton
            toggleMenuButton={toggleMenuButton}
            onClick={handleClick}
          />
        </nav>
        <MobileMenu toggleMenuMobile={toggleMenuMobile} />
      </div>
    </div>
  );
}
