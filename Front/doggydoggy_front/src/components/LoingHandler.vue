<script setup>
import axios from 'axios';
import { ref, onMounted } from 'vue';
import { useUserStore } from '@/stores/userStore';
import { useRouter } from 'vue-router';

const router = useRouter();
const store = useUserStore();
const { user, setUser, setToken } = store;

let code = ref('');

function getCodeAndRedirect() {
  code = new URL(window.location.href).searchParams.get('code');
  console.log(code);
}

function kakaoLogin() {
  axios
    .get(`http://localhost:8080/kakao/callback?code=${code}`)
    .then((response) => {
      console.log(response.data);

      if (response.data != null) {
        // 토큰은 로컬스토리지에 저장하면 JS를 이용한 XSS 공격에 취약, 쿠키에 HTTP Only 로 저장히기
        $cookies.set('token', response.data);
        alert('로그인 성공');
        router.push('/user/input');
      }
    });
}

onMounted(() => {
  getCodeAndRedirect();
  kakaoLogin();
});
</script>

<template>
  <div>로그인 중입니다. 기다려주세요.</div>
</template>

<style scoped></style>
