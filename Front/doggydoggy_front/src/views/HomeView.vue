<script setup>
/*
  Main Homepage

  동호님 Pinia를 써보니까 Option API로 Pinia를 받기가 굉장히 어렵습니다.
  그래서 그냥 composition API 쓰시는 것을 추천합니다.

   vue-cookies 라이브러리(Vue에서 쿠키를 사용하기 위한 라이브러리) 사용법은 
   npm install vue-cookies로 라이브러리 설치
   main.js에 vue-cookies 세팅
   
   $cookies.set('token', response.data); 쿠키 저장
   $cookies.get('token', response.data); 저장된 쿠키 정보 가져오기 
   $cookies.isKey('token'); 쿠키가 있는지 없는지 true false 반환
   
   더 자세한건 vue-cookies 구글 검색

*/
import { useUserStore } from '@/stores/userStore';
const userStore = useUserStore();
</script>

<template>
  <div class=".home"></div>
</template>

<style scoped>
.home {
  height: 100%;
  background-color: whitesmoke;
  text-align: center;
}
</style>
