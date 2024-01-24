<template>
  <div class="header">
    <img class="logo" src="@/assets/images/logo.png" alt="도기도기 로고" />
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
}

.header a {
  text-decoration-line: none;
}
.logo {
  height: 75%;
}
</style>
