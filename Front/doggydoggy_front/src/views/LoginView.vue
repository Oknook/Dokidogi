<template>
  <div class="login">
    <h2>소셜로그인</h2>
    <div class="login__border">
      <div class="login__images">
        <KakaoLogin />
        <NaverLogin />
        <GoogleLogin />
      </div>
    </div>
    <a href=""><h4>처음이신가요? 회원가입을 해주세요!</h4></a>
  </div>
</template>

<script>
/*
  로그인 뷰에 카카오로그인 컴포넌트랑 네이버 로그인 컴포넌트 부착

*/
import KakaoLogin from '@/components/KakaoLogin.vue';
import NaverLogin from '@/components/NaverLogin.vue';
import GoogleLogin from '@/components/GoogleLogin.vue';

export default {
  data() {
    return {
      kakaoAuthUrl: import.meta.env.VITE_KAKAO_AUTH_URL,
    };
  },
  components: {
    KakaoLogin,
    NaverLogin,
    GoogleLogin,
  },
};
</script>

<style scoped>
h2,
h3,
h4 {
  text-align: center;
}
.login {
  height: 100vh;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
}

.login__border {
  height: 20%;
  width: 80%;
  border: 1px solid black;
  border-radius: 20px;
}

.login__border + a {
  text-decoration-line: underline;
  color: black;
}

.login__images {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-around;
  text-align: center;
}
</style>
