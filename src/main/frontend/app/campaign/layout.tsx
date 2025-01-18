import type { Metadata } from "next";
import "../globals.css";
import MenuTop from "../components/nav/menu-top";

export const metadata: Metadata = {
  title: "Campaigner",
  description: "Design your ttrpg campaign.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={`antialiased h-screen`}>
        <MenuTop />
        {children}
      </body>
    </html>
  );
}
