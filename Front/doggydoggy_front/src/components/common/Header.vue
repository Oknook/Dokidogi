<script setup>
/*
Header.vue는 공통 컴포넌트에 속한다.
Header에 필요한 Nav가 들어있다.

도기도기 로고, 친구 찾기, 채팅, 로그인
도기도기 로고: 클릭하면 Home 화면으로 이동
로그인: 소셜 로그인 페이지로 이동

로그인 완료 여부는 loginCheck 변수를 통해 확인한다.
로그인 완료 되면 로그인이 프로필로 바뀐다.

프로필: 프로필 페이지 이동
로그아웃: 로그아웃 실시

*/
import { ref } from 'vue';

/*
로그아웃 함수
로그아웃 수행 시, token 쿠키 제거. 
loginCheck 변수 갱신
로컬스토리지에 담긴 nickname, sex, birthday 제거

*/
function onClickLogout() {
  $cookies.remove('token');
  loginCheck.value = $cookies.isKey('token');
  localStorage.removeItem('nickname');
  localStorage.removeItem('sex');
  localStorage.removeItem('birthday');
  alert('로그아웃 완료');
}
</script>

<!-- 
  다른 컴포넌트에서 쓰기 위해 변수를 export 형태로 작성
  이를 통해 다른 컴포넌트에서 변수를 import해 사용 가능

 -->
<script>
export const loginCheck = ref($cookies.isKey('token'));
</script>

<template>
  <div class="header">
    <a class="logo" href="/"
      ><img
        class="logo__img"
        src="@/assets/images/logo.png"
        alt="도기도기 로고"
    /></a>
    <router-link to="/" class="header__friend">친구 찾기</router-link>
    <router-link to="/" class="header__chat">채팅</router-link>
    <RouterLink :to="{name: 'kakaomaps'}" class="header__map">지도</RouterLink>
    <RouterLink :to="{name: 'communities'}" class="header__post">자유게시판</RouterLink>

    <div class="header__user">
      <router-link :to="{ name: 'user-profile' }"
        v-if="loginCheck" class="header__user--profile">
        프로필</router-link>
      <router-link :to="{ name: 'login' }" class="header__user--login" v-else>
        로그인</router-link>
      <button v-if="loginCheck" class="header__user--logout" @click="onClickLogout">
        로그아웃</button>
    </div>
  </div>
</template>

<style scoped>
.header {
  height: 10%;
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
  height: 60%;
}

.header__friend,
.header__chat {
  color: black;
  font-weight: bold;
  text-decoration-line: none;
}

.header__user {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.header__user--login,
.header__user--profile {
  text-decoration-line: none;
  color: black;
  font-weight: bold;
}
</style>
