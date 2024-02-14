<template>
  <div class="social__img" @click="naverLogin">
    <a :href="naverAuthUrl"> <img src="@/assets/images/naverLogo.png" alt="" /></a>
  </div>
</template>

<script>

  

export default {
  data() {
    return {
      naverAuthUrl: `https://nid.naver.com/oauth2.0/authorize?client_id=vUqy0Bu7rsgnsZgs11EW&redirect_uri=https://i10b202.p.ssafy.io/api/callback/naver&response_type=code`
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
