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
        silver: "#bfbfbf",
        platinum: "#e8e8e8",
        orange_wheel: "#FF7D00",
        celestial_blue: "#5398BE",
        verdigris: "#70A9A1",
        spring_green: "#00FF7D",
      },
      fontFamily: {
        CabinCondensed: ["CabinCondensed", "sans-serif"],
        JetBrainsMono: ["JetBrainsMono", "sans-serif"],
        Grenze: ["Grenze", "sans-serif"],
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
