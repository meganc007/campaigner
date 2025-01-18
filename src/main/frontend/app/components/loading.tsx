import Image from "next/image";

export default function Loading() {
  return (
    <div className="flex flex-col w-full p-2 md:flex-row md:text-lg">
      <Image
        src="/ring-resize.svg"
        alt="Loading spinner"
        width={24}
        height={24}
        className="mx-2"
      />
      <p>Loading...</p>
    </div>
  );
}
