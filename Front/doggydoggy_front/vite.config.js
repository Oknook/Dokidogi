import { fileURLToPath, URL } from 'node:url';
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    proxy: {
      // '/api' is the path to proxy.
      // For example, if you make a request to '/api/users' in a Vue.js application,
      // This request is forwarded to 'http://localhost:8080/api/users'.
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true, // Corrected: semicolon to comma
        secure: false,
      },
    },
  },
});