"use client";
import { useState } from "react";
import NavItem from "./nav-item";
import MobileMenu from "./mobile-menu";

export default function MenuTop() {
  const [toggleMenuButton, setToggleMenuButton] = useState(false);
  const [toggleMenuMobile, settoggleMenuMobile] = useState(true);

  const handleClick = () => {
    setToggleMenuButton((prevState) => !prevState);
    settoggleMenuMobile((prevState) => !prevState);
  };

  return (
    <header className="sticky w-full z-10 top-0 left-0 items-center">
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
          {/* <!-- Hamburger Button --> */}
          <div className="md:hidden">
            <button
              type="button"
              className={`z-40 block hamburger md:hidden ${
                toggleMenuButton ? "open" : ""
              }`}
              onClick={handleClick}
            >
              <span className="hamburger-top"></span>
              <span className="hamburger-middle"></span>
              <span className="hamburger-bottom"></span>
            </button>
          </div>
        </nav>
        {/* <!-- Mobile Menu --> */}
        <MobileMenu toggleMenuMobile={toggleMenuMobile} />
      </div>
    </header>
  );
}
