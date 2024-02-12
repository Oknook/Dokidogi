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

   env 파일은 환경 변수 파일인데 이거를 쓰셔야합니다.
   여기에 카카오 API 키값, 네이버 API 키값, 필요한 리다이렉트 주소 등을 넣어서 import해서 쓰면 편하고 원래 보안상으로도 API Key는 env 파일에
   저장해서 쓰는게 안전하니까 꼭 쓰셔요!! env 파일 관련된 내용은 노션에 올려놨어요


   프로젝트 안에 있는 sw.js는 서비스워커 파일이고 manifest.js 는 매니페스트 파일
   둘다 PWA를 위한 설정 파일

   sw.js 는 동호님이 이제부터 작성하셔야하고...화이팅..sw.js 가 어렵습니다
   manifest.js 는 웹 브라우저에게 나 PWA 앱인데 이런 이런 정보를 가지고 있어~~ 라는 여권같은 존재입니다.
   그래서 이 안에 보시면 이런 정보가 들어있어요

   {
  "background_color": "#ffffff",
  "description": "Progressive Web App With Vue3",
  "dir": "ltr",
  "display": "standalone",
  "name": "DokiDogi",
  "orientation": "portrait",
  "scope": "/",
  "short_name": "Dokidogi",
  "start_url": "/",
  "theme_color": "#317EFB",
  "categories": ["social"],
  "screenshots": [],
  "shortcuts": [],
  "lang": "ko",
  "icons": [
    {
      "src": "src/assets/icons/icon-512x512.png",
      "sizes": "512x512",
      "type": "image/png"
    }
  ]
}

배경색, 앱에 대한 설명, dir 뭔지 모름 ㅎ, display: standalone 이 기본평.(우리가 평소에 쓰는 모바일의 모습), 앱 네임, 앱이 어떤 방향으로 화면에 표시될지. portrait가 수직. 이하 등등은 검색...! ㅎㅎ

*/
import { useUserStore } from '@/stores/userStore';
import axios from 'axios';
import { onMounted } from 'vue';
const userStore = useUserStore();

async function press() {
  axios.get('http://192.168.31.229:8080/kakao/kokoa').then((res) => {
    console.log(res.data);
  });
}
</script>

<template>
  <div class=".home">
    <button @click="press">button</button>
  </div>
</template>

<style scoped>
.home {
  height: 100%;
  background-color: whitesmoke;
  text-align: center;
}
</style>
