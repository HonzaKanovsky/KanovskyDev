/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Poppins', 'sans-serif']
      },
      backgroundImage: {
        'gradient-background': 'linear-gradient(to bottom-right, #262eec, #d51f86)',
      }
    },
  },
  variants: {
    extend: {},
  },
  plugins: [],
}


