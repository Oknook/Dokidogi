<template>
  <div>로그인 중입니다. 기다려주세요.</div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      code: '',
    };
  },
  methods: {
    GetCodeAndRedirect: function () {
      let code = new URL(window.location.href).searchParams.get('code');
      console.log(code);
      this.code = code;
    },
    kakaoLogin: function () {
      axios
        .get(`http://localhost:8080/kakao/callback?code=${this.code}`)
        .then((response) => {
          console.log(response.data);

          if (response.data != null) {
            alert('로그인 성공!');
            this.$router.push('/');
          }
        });
    },
  },
  mounted() {
    this.GetCodeAndRedirect();
    this.kakaoLogin();
  },
};
</script>

<style scoped></style>
