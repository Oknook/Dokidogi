<template>
  <div>
    <a :href="KAKAO_AUTH_URL">카카오 로그인</a>
  </div>
</template>

<script>
import axios from 'axios';
import { KAKAO_AUTH_URL } from '@/assets/OAuth';
export default {
  data() {
    return {
      KAKAO_AUTH_URL,
    };
  },
  created: function () {
    console.log(window.Kakao.isInitialized());
  },
  methods: {
    // kakaoLogin: function () {
    //   window.Kakao.Auth.authorize({
    //     redirectUri: 'http://localhost:5173/login/wating',
    //   });
    // },
  },
};
</script>

<style scoped>
body {
  margin: 0;
}
</style>
