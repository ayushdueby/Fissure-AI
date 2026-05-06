import type { Config } from "tailwindcss";

const config: Config = {
  darkMode: ["class"],
  content: [
    "./app/**/*.{ts,tsx}",
    "./components/**/*.{ts,tsx}",
    "./hooks/**/*.{ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        background: "#090c11",
        foreground: "#d6deeb",
        card: "#0f141d",
        muted: "#99a5bd",
        border: "#202a3a",
        accent: "#5e7599",
      },
      borderRadius: {
        lg: "0.85rem",
      },
      boxShadow: {
        glass: "0 8px 32px rgba(0, 0, 0, 0.35)",
      },
    },
  },
  plugins: [],
};

export default config;
