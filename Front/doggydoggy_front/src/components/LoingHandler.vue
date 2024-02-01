<script setup>
/*
   카카오 로그인, 네이버 로그인의 redirect_url 핸들러 처리를 하는 코드
   
   인가 코드를 뽑아내고 백엔드 Spring 서버에 보내주는 동작을 실행

   여기에 나중에 네이버 로그인 핸들러 함수도 넣어야함!
   동호남 화이팅!

   




*/
import axios from 'axios';
import { ref, onMounted } from 'vue';
import { useUserStore } from '@/stores/userStore';
import { useRouter } from 'vue-router';

const router = useRouter();
const store = useUserStore();
const { user, setUser, setToken } = store;

let code = ref('');

// 카카오에서 넘어온 인가코드를 URL에서 추출하여 코드 변수에 저장
function getCodeAndRedirect() {
  code = new URL(window.location.href).searchParams.get('code');
  console.log(code);
}

// 인가코드를 이용하여 Spring Backend에 인가코드 전달 후 토큰 받아오기
function kakaoLogin() {
  axios
    .get(`http://localhost:8080/kakao/callback?code=${code}`)
    .then((response) => {
      console.log(response.data);

      if (response.data != null) {
        // 토큰은 로컬스토리지에 저장하면 JS를 이용한 XSS 공격에 취약, 쿠키에 HTTP Only 로 저장히기
        $cookies.set('token', response.data);
        alert('로그인 성공');

        // 백에서 사용자의 토큰 및 고유번호가 db에 있으면 user 정보가 있다는 것을 알려줘야 하고 없다고 알려주면 회원 정보 입력 창으로 이동한다.

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
