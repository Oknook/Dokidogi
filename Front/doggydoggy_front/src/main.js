import { createApp } from 'vue';
import { createPinia } from 'pinia';

import App from './App.vue';
import router from './router';

// window: 웹 페이지 전체에서 접근 가능한 객체, 웹 페이지에 로드된 모든 스크립트 및 변수에 접근 가능
window.Kakao.init('bd70ac45102794eb0eaab6abec9509c8');

const app = createApp(App);

app.use(createPinia());
app.use(router);
app.mount('#app');
