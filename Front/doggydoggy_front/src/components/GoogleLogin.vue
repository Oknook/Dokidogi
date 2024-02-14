<template>
  <div class="social__img" @click="googleLogin">
    <a :href="googleAuthUrl"> <img src="@/assets/images/google.png" alt="" /></a>
  </div>
</template>

<script>



export default {
  data() {
    return {
      googleAuthUrl: 'https://accounts.google.com/o/oauth2/v2/auth?client_id=25526902806-o0qoq0iel49ca0sev5vrptq86i6kdmed.apps.googleusercontent.com&redirect_uri=http://i10b202.p.ssafy.io/api/callback/google&response_type=code&scope=profile',
    };
  },
};


/*
  네이버 로그인 요청 URL을 이용하여 네이버에 로그인 요청
  state와 redirect_url을 인코딩해서 넣어야한다.

// */
// 민관님 코드
// export default {
//   data() {
//     return {
//       // state는 인코딩해서 넣어줘야 함.
//       state: btoa(import.meta.env.VITE_NAVER_STATE),
//       reurl: encodeURIComponent(import.meta.env.VITE_NAVER_REDIRECT_URI),
//       url: `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=${
//         import.meta.env.VITE_NAVER_CLIENT_ID
//       }&redirect_uri=http%3a%2f%2flocalhost%3a5173%2flogin%2fwaiting&state=${
//         this.state
//       }`,
//     };
//   },
// };
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
