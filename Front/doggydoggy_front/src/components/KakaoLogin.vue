<template>
  <div class="social__img">
    <a :href="kakaoAuthUrl">
      <img src="@/assets/images/kakaoLogo.png" alt=""
    /></a>
  </div>
</template>

<script>
/*
카카오 로그인 컴포넌트
카카오 로고 클릭 시 카카오 로그인 수행
env 파일에서 카카오 인증을 위한 URL을 불러와서 a:href에 넣음
로고를 클릭하면 카카오 인증 화면으로 이동!

*/
export default {
  data() {
    return {
      kakaoAuthUrl: import.meta.env.VITE_KAKAO_AUTH_URL,
    };
  },
};
</script>

<style scoped>
.social__img {
  height: 50%;
}
.social__img a {
  display: inline-block;
  height: 100%;
}
img {
  height: 100%;
  border-radius: 20px;
}
</style>
