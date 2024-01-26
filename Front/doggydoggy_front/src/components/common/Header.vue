<template>
  <div class="header">
    <a class="logo" href="/"
      ><img
        class="logo__img"
        src="@/assets/images/logo.png"
        alt="도기도기 로고"
    /></a>
    <span>친구 찾기</span>
    <router-link v-bind:to="{ name: 'login' }">로그인</router-link>
  </div>
</template>

<script>
export default {};
</script>

<style scoped>
.header {
  height: 15%;
  background-color: white;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  align-items: center;
  border-bottom: solid 1px black;
}

.header .logo {
  height: 100%;
  text-decoration-line: none;
}

.logo__img {
  height: 85%;
}
</style>
