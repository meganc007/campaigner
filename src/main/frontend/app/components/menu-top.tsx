"use client";
import { useState } from "react";
import NavItem from "./nav-item";
import Link from "next/link";

export default function MenuTop() {
  const [toggleMenuButton, setToggleMenuButton] = useState(false);
  const [toggleMenuMobile, settoggleMenuMobile] = useState(true);

  const handleClick = () => {
    setToggleMenuButton((prevState) => !prevState);
    settoggleMenuMobile((prevState) => !prevState);
  };
  return (
    <div className="bg-white border-b-4 border-black drop-shadow-md shadow-gray-800 p-4">
      {/* <!-- Desktop Menu --> */}
      <nav className="flex justify-center mx-auto font-JetBrainsMono font-medium text-base">
        <div className="hidden md:flex md:space-x-6 md:content-start lg:space-x-14">
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
      <div
        className={`flex flex-col absolute top-0 bottom-0 left-0 self-end w-full py-1 pt-20 pl-2 space-y-3 bg-white text-black font-JetBrainsMono font-medium text-lg  border-b-4 border-black drop-shadow-md shadow-gray-800 ${
          toggleMenuMobile ? "hidden" : ""
        }`}
      >
        <div className="p-2 rounded-r-sm border-l-4 border-transparent hover:border-l-4 hover:border-black hover:bg-celestial_blue transition ease-in-out duration-150">
          <Link href="/" className="">
            Home
          </Link>
        </div>
        <div className="p-2 rounded-r-sm border-l-4 border-transparent hover:border-l-4 hover:border-black hover:bg-celestial_blue transition ease-in-out duration-150">
          <Link href="#" className="">
            Locations
          </Link>
        </div>
        <div className="p-2 rounded-r-sm border-l-4 border-transparent hover:border-l-4 hover:border-black hover:bg-celestial_blue transition ease-in-out duration-150">
          <Link href="#" className="">
            People
          </Link>
        </div>
        <div className="p-2 rounded-r-sm border-l-4 border-transparent hover:border-l-4 hover:border-black hover:bg-celestial_blue transition ease-in-out duration-150">
          <Link href="#" className="">
            Items
          </Link>
        </div>
        <div className="p-2 rounded-r-sm border-l-4 border-transparent hover:border-l-4 hover:border-black hover:bg-celestial_blue transition ease-in-out duration-150">
          <Link href="#" className="">
            Quests
          </Link>
        </div>
        <div className="p-2 rounded-r-sm border-l-4 border-transparent hover:border-l-4 hover:border-black hover:bg-celestial_blue transition ease-in-out duration-150">
          <Link href="#" className="">
            Events
          </Link>
        </div>
        <div className="p-2 rounded-r-sm border-l-4 border-transparent hover:border-l-4 hover:border-black hover:bg-celestial_blue transition ease-in-out duration-150">
          <Link href="#" className="">
            Monsters
          </Link>
        </div>
      </div>
    </div>
  );
}
