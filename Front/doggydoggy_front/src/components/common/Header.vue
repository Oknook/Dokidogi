<script setup>
import { ref } from 'vue';

const loginCheck = ref($cookies.isKey('token'));
</script>

<template>
  <div class="header">
    <a class="logo" href="/"
      ><img
        class="logo__img"
        src="@/assets/images/logo.png"
        alt="도기도기 로고"
    /></a>
    <span class="header__friend">친구 찾기</span>
    <h2 v-if="loginCheck">프로필</h2>
    <router-link v-else v-bind:to="{ name: 'login' }" class="header__login"
      >로그인</router-link
    >
  </div>
</template>

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

.header__friend {
  color: black;
  font-weight: bold;
}
.header__login {
  text-decoration-line: none;
  color: black;
  font-weight: bold;
}
</style>
