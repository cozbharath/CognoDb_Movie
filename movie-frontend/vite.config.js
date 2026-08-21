import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],

  server: {
    port: 5173,

    proxy: {
      '/api': {
        target: 'http://localhost:8181',
        changeOrigin: true,

        rewrite: (path) => {
          // Auth endpoints are actually /auth/login and /auth/register
          if (path.startsWith('/api/auth/')) {
            return path.replace(/^\/api/, '');
          }

          // Movie endpoints are actually /api/movies
          return path;
        },
      },
    },
  },
});