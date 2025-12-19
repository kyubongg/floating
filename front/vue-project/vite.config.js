import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    proxy: {
      /**
       * 브라우저에서 /api-openai로 시작하는 요청을 보내면
       * Vite 개발 서버가 대신 gms.ssafy.io로 전달함 
       * 
       * 브라우저에서 open-api 호출 시 cors 에러가 발생하였기 때문에 설정함
       * */ 
      '/api-openai': {
        target: 'https://gms.ssafy.io',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api-openai/, '/gmsapi')
      }
    }
  }
})
