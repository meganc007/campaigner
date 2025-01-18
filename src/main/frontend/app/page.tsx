import Link from "next/link";
import HomeGrid from "./components/home-grid";
import Hero from "./components/hero";

export default function Home() {
  return (
    <div className="md:text-lg">
      <Hero />
      <div className="flex flex-col md:flex-row">
        <HomeGrid />
      </div>
      <hr className="border-gunmetal border-1" />
      <Link href="/campaign">View all campaigns</Link>
    </div>
  );
}
