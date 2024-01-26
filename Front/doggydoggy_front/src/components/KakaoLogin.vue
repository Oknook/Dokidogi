<template>
  <div>
    <figure>
      <a :href="kakaoAuthUrl">
        <img src="@/assets/images/kakaoLogo.png" alt=""
      /></a>
    </figure>
  </div>
</template>

<script>
export default {
  data() {
    return {
      kakaoAuthUrl: import.meta.env.VITE_KAKAO_AUTH_URL,
    };
  },
};
</script>

<style scoped>
img {
  width: 20%;
}
</style>
