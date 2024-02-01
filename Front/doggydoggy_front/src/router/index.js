import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import LoginView from '@/views/LoginView.vue';
import LoginWaitingView from '@/views/LoginWaitingView.vue';
import UserInputView from '@/views/UserInputView.vue';
import UserProfileView from '@/views/UserProfileView.vue';
import PetRegistListView from '@/views/PetRegistListView.vue';
import PetRegisterView from '@/views/PetRegisterView.vue';

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
    {
      path: '/user/profile',
      name: 'user-profile',
      component: UserProfileView,
    },
    {
      path: '/pet/register-list',
      name: 'register-list',
      component: PetRegistListView,
    },
    {
      path: '/pet/register',
      name: 'register',
      component: PetRegisterView,
    },
  ],
});

export default router;
