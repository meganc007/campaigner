import type { Config } from "tailwindcss";

export default {
  content: [
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      colors: {
        background: "var(--background)",
        foreground: "var(--foreground)",
      },
      fontFamily: {
        Lato: ["Lato", "sans-serif"],
        Montserrat: ["Montserrat", "sans-serif"],
        Oswald: ["Oswald", "sans-serif"],
      },
      fontWeight: {
        extralight: "100",
        light: "300",
        medium: "400",
        bold: "700",
        extrabold: "900",
      },
    },
  },
  plugins: [],
} satisfies Config;
