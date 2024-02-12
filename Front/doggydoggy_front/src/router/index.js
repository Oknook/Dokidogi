import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import LoginView from '@/views/LoginView.vue';
import LoginWaitingView from '@/views/LoginWaitingView.vue';
import UserInputView from '@/views/UserInputView.vue';
import UserProfileView from '@/views/UserProfileView.vue';
import PetRegistListView from '@/views/PetRegistListView.vue';
import PetRegisterView from '@/views/PetRegisterView.vue';
import PetRegisterDetailView from '@/views/PetRegisterDetailView.vue';
import BanneduserView from '@/views/BanneduserView.vue';

import BoardView from '@/views/BoardView.vue' 
import PostCreateView from '@/views/PostCreateView.vue'
import PostDetailView from '@/views/PostDetailView.vue'
import PostDetailUpdateView from '@/views/PostDetailUpdate.vue'
import KakaoMapView from '@/views/KakaoMapView.vue'
import CommentCreateView from '@/views/CommentCreateView.vue'

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
    {
      path: '/pet/register-detail',
      name: 'register-detail',
      component: PetRegisterDetailView,
    },
    {
      path: '/user/banned',
      name: 'banned-user',
      component: BanneduserView,
    },
    {
      path: '/websocketurl',
      name: 'web-socket',
      beforeEnter(to, from, next) {
        window.location.href = 'websocketurl'
      }
    },

    {
      path: '/api/board',
      name: 'communities',
      component: BoardView
    },
    {
      path: '/api/board/save',
      name: 'postcreate',
      component: PostCreateView
    },
    {
      path: '/api/board/:boardId',
      name: 'detail',
      component: PostDetailView
    },

    {
      path: '/api/board/:boardId/update',
      name: 'detailupdate',
      component: PostDetailUpdateView
    },

    {
      path: '/kakaomaps',
      name: 'kakaomaps',
      component: KakaoMapView
    },

    {
      path: '/comments',
      name: 'comments',
      component: CommentCreateView
    },


  ],
});

export default router;
