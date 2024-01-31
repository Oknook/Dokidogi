import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import LoginView from '@/views/LoginView.vue';
import LoginWaitingView from '@/views/LoginWaitingView.vue';
import UserInputView from '@/views/UserInputView.vue';
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/login/waiting',
      name: 'waiting',
      component: LoginWaitingView,
    },
    {
      path: '/user/input',
      name: 'user-input',
      component: UserInputView,
    },
  ],
});

export default router;
