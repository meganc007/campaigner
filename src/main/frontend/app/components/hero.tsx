import Link from "next/link";
import Input from "./input";

export default function Hero() {
  return (
    <div className="flex flex-col justify-between md:flex-row">
      <div className="flex flex-col p-2 bg-celestial_blue bg- md:justify-center md:w-6/12">
        <h1 className="md:text-7xl lg:text-8xl">Campaigner</h1>
        <h2 className="px-2 max-w-xl md:text-3xl lg:text-4xl">
          A new way to organize your ttrpg campaign
        </h2>
      </div>
      <div className="flex flex-col py-2 px-6 border-8 border-celestial_blue space-y-2 md:w-6/12 md:px-12">
        <h3 className="text-2xl bg-celestial_blue border-l-8 border-gunmetal px-4 py-2 text-black">
          Sign Up
        </h3>
        <Input
          labelText="Email"
          inputId="email"
          inputType="email"
          placeholder="youremail@example.com"
        />
        <Input
          labelText="Password"
          inputId="password"
          inputType="password"
          placeholder="Enter your password"
        />
        <button className="w-1/2 p-2 bg-icterine border-y-4 border-x-8 border-gunmetal skew-x-[-12deg] hover:transition-transform hover:bg-celestial_blue duration-1000">
          Sign up
        </button>
        <p>
          Already have an account? <Link href="/">Log in</Link>
        </p>
      </div>
    </div>
  );
}
