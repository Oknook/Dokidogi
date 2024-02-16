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
import userEditViewVue from '@/components/userEditView.vue';
import petRegisterViewVue from '@/components/petRegisterView.vue';

import BoardView from '@/views/BoardView.vue';
import PostCreateView from '@/views/PostCreateView.vue';
import PostDetailView from '@/views/PostDetailView.vue';
import PostDetailUpdateView from '@/views/PostDetailUpdate.vue';
import KakaoMapView from '@/views/KakaoMapView.vue';
import CommentCreateView from '@/views/CommentCreateView.vue';
import MatchingView from "@/views/MatchingView.vue";

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
      path: '/user/pet',
      name: 'register',
      component: petRegisterViewVue,
    },
    {
      path: '/user/change',
      name: 'user-edit',
      component: userEditViewVue,
    },
    {
      path: '/pet/register-list',
      name: 'register-list',
      component: PetRegistListView,
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
    // 웹소켓 URL 직접 접근 처리를 위한 라우트
    {
      path: '/websocketurl',
      name: 'web-socket',
      beforeEnter(to, from, next) {
        window.location.href = 'websocketurl';
      },
    },
    // 게시판 및 게시글 관련 라우트
    {
      path: '/board',
      name: 'communities',
      component: BoardView,
    },
    {
      path: '/board/create',
      name: 'postcreate',
      component: PostCreateView,
    },
    {
      path: '/board/:boardId',
      name: 'detail',
      component: PostDetailView,
    },
    {
      path: '/board/:boardId/update',
      name: 'detailupdate',
      component: PostDetailUpdateView,
    },
    // 카카오 맵 뷰
    {
      path: '/kakaomaps',
      name: 'kakaomaps',
      component: KakaoMapView,
    },
    // 댓글 생성 뷰
    {
      path: '/comments',
      name: 'comments',
      component: CommentCreateView,
    },
    // 댓글 생성 뷰
    {
      path: '/matching',
      name: 'matching',
      component: MatchingView,
    },
  ],
});

export default router;

