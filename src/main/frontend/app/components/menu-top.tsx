import NavItem from "./nav-item";

export default function MenuTop() {
  return (
    <div className="bg-white border-b-4 border-black drop-shadow-md shadow-gray-800 p-4">
      <nav className="flex justify-center mx-auto font-JetBrainsMono font-medium text-sm md:text-base">
        <div className="hidden md:flex md:space-x-6 md:content-start lg:space-x-14">
          <NavItem linkTo="#" src="/globe.svg" itemText="Locations" />
          <NavItem linkTo="#" src="/globe.svg" itemText="People" />
          <NavItem linkTo="#" src="/globe.svg" itemText="Items" />
          <NavItem linkTo="/" src="/file.svg" itemText="Home" />
          <NavItem linkTo="#" src="/globe.svg" itemText="Quests" />
          <NavItem linkTo="#" src="/globe.svg" itemText="Rewards" />
          <NavItem linkTo="#" src="/globe.svg" itemText="Monsters" />
        </div>
      </nav>
    </div>
  );
}
