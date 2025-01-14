import Link from "next/link";
import HomeGrid from "./components/home-grid";

export default function Home() {
  return (
    <div className="md:text-lg">
      <div className="flex flex-col justify-between md:flex-row">
        <div className="flex flex-col p-2 bg-orange_wheel md:w-6/12">
          <h1 className="md:text-8xl z-10">Campaigner</h1>
          <h2 className="md:text-4xl px-2 max-w-xl">
            A new way to organize your ttrpg campaign
          </h2>
        </div>
        <div className="flex flex-col py-2 px-6 border-8 border-orange_wheel space-y-2 md:w-6/12 md:px-12">
          <h3 className="text-2xl bg-celestial_blue border-l-8 border-black px-4 py-2 text-black">
            Sign Up
          </h3>
          <label htmlFor="email">Email</label>
          <input
            id="email"
            className="w-11/12 p-2 skew-x-[-12deg] border-y-4 border-x-8 border-black bg-transparent placeholder:text-zinc-600 placeholder:tracking-wider focus:outline-none md:w-full"
            type="email"
            placeholder="youremail@example.com"
          />
          <label htmlFor="password">Password</label>
          <input
            id="password"
            className="w-11/12 p-2 skew-x-[-12deg] border-y-4 border-x-8 border-black bg-transparent placeholder:text-zinc-600 placeholder:tracking-wider focus:outline-none md:w-full"
            type="password"
            placeholder="Enter your password"
          />
          <button className="w-1/2 p-2 bg-celestial_blue border-y-4 border-x-8 border-black skew-x-[-12deg] hover:transition-transform hover:bg-black hover:border-celestial_blue hover:text-celestial_blue duration-1000">
            Sign up
          </button>
          <p>
            Already have an account? <span>Log in</span>
          </p>
        </div>
      </div>
      <div className="flex flex-col md:flex-row">
        <HomeGrid />
      </div>
      <hr className="border-black border-1" />
      <Link href="/campaign">View all campaigns</Link>
    </div>
  );
}
