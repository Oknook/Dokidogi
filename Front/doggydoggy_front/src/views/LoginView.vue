<template>
  <div><button @click="KakaoLogin">카카오 로그인</button></div>
</template>

<script>
export default {
  created: function () {
    console.log(window.Kakao.isInitialized());
  },
  methods: {
    KakaoLogin: function () {
      if (window.Kakao.isInitialized()) {
        window.Kakao.Auth.authorize({
          redirectUri:
            '${https://developers.kakao.com/docs/latest/ko/kakaologin/js#before-you-begin}',
        });
      }
    },
  },
};
</script>

<style scoped>
body {
  margin: 0;
}
</style>
