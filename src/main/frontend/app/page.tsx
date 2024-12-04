import Link from "next/link";

export default function Home() {
  return (
    <main className="container">
      <div className="m-1 p-5">
        <h1 className="flex justify-center">Welcome to Campaigner!</h1>
        <div className="flex justify-center max-w-4xl flex-col mx-auto bg-old-rose rounded-tl-2xl rounded-br-2xl p-5">
          <h2 className="p-5">Login</h2>
          <div className="space-y-1">
            <label htmlFor="email" className="block">
              Email:
            </label>
            <input id="email" className="block" type="email" />
          </div>
        </div>
        <Link href="/campaign">View all campaigns</Link>
      </div>
    </main>
  );
}
