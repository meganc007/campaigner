import type { Metadata } from "next";
import "@/app/globals.css";
import MenuTop from "@/app/components/nav/menu-top";

export const metadata: Metadata = {
  title: "Campaigner",
  description: "Design your ttrpg campaign.",
};

export default function OverviewLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <>
      <header>
        <MenuTop />
      </header>
      <main>{children}</main>
    </>
  );
}
