<script setup>

import { ref } from 'vue';


function onClickLogout() {
  $cookies.remove('token');
  loginCheck.value = $cookies.isKey('token');
  localStorage.removeItem('nickname');
  localStorage.removeItem('sex');
  localStorage.removeItem('birthday');
  alert('로그아웃 완료');
}
</script>

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
  height: 50%;
  margin-top: 5%;
  text-decoration-line: none;
  display: flex;
  justify-content: center;
}

.logo__img {
  height: 100px;
  padding-bottom: 20px;
  -webkit-transform: rotate(0) scale(1);
  transform: rotate(0) scale(1);
  -webkit-transition: .3s ease-in-out;
  transition: .3s ease-in-out;
}

.logo__img:hover {
  -webkit-transform: rotate(15deg) scale(1.4);
  transform: rotate(15deg) scale(1.4);
}

.header__friend,
.header__map,
.header__post,
.header__chat {
  position: relative; 
  display: inline-block; 
  color: black;
  font-weight: bold;
  text-decoration-line: none;
}

.header__map

.header__user {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
}

.header__user--login,
.header__user--profile {
  position: relative; 
  display: inline-block; 
  text-decoration-line: none;
  color: black;
  font-weight: bold;
}




.header__friend:before, 
.header__friend:after {
  content: '';
  position: absolute;
  bottom: -5px; /* 선의 위치 조정, 요소 아래에 위치 */
  width: 0;
  height: 3px; /* 선의 두께 */
  background-color: black; /* 선의 색상 */
  transition: width 0.2s ease; /* 애니메이션 효과 */
}

.header__friend:before {
  left: 0;
}

.header__friend:after {
  right: 0;
}

.header__friend:hover:before, 
.header__friend:hover:after {
  width: 50%;  
}


.header__chat:before, 
.header__chat:after {
  content: '';
  position: absolute;
  bottom: -5px; /* 선의 위치 조정, 요소 아래에 위치 */
  width: 0;
  height: 3px; /* 선의 두께 */
  background-color: black; /* 선의 색상 */
  transition: width 0.2s ease; /* 애니메이션 효과 */
}

.header__chat:before {
  left: 0;
}

.header__chat:after {
  right: 0;
}

.header__chat:hover:before, 
.header__chat:hover:after {
  width: 50%;  
}




.header__map:before, 
.header__map:after {
  content: '';
  position: absolute;
  bottom: -5px; /* 선의 위치 조정, 요소 아래에 위치 */
  width: 0;
  height: 3px; /* 선의 두께 */
  background-color: black; /* 선의 색상 */
  transition: width 0.2s ease; /* 애니메이션 효과 */
}

.header__map:before {
  left: 0;
}

.header__map:after {
  right: 0;
}

.header__map:hover:before, 
.header__map:hover:after {
  width: 50%;  
}



.header__post:before, 
.header__post:after {
  content: '';
  position: absolute;
  bottom: -5px; /* 선의 위치 조정, 요소 아래에 위치 */
  width: 0;
  height: 3px; /* 선의 두께 */
  background-color: black; /* 선의 색상 */
  transition: width 0.2s ease; /* 애니메이션 효과 */
}

.header__post:before {
  left: 0;
}

.header__post:after {
  right: 0;
}

.header__post:hover:before, 
.header__post:hover:after {
  width: 50%;  
}



.header__user--login:before, 
.header__user--login:after {
  content: '';
  position: absolute;
  bottom: -5px; /* 선의 위치 조정, 요소 아래에 위치 */
  width: 0;
  height: 3px; /* 선의 두께 */
  background-color: black; /* 선의 색상 */
  transition: width 0.2s ease; /* 애니메이션 효과 */
}

.header__user--login:before {
  left: 0;
}

.header__user--login:after {
  right: 0;
}

.header__user--login:hover:before, 
.header__user--login:hover:after {
  width: 50%;  
}








/* 작은 화면 크기에 대한 스타일 */
@media (max-width: 600px) {
  .header__friend,
  .header__chat,
  .header__user,
  .header__user--login,
  .header__user--profile,
  .header__map,
  .header__post {
    font-size: 20px; /* 화면 너비가 600px 이하일 때 글자 크기 조정 */
  }
}

/* 중간 화면 크기에 대한 스타일 */
@media (min-width: 601px) and (max-width: 1024px) {
  .header__friend,
  .header__chat,
  .header__user,
  .header__user--login,
  .header__user--profile,
  .header__map,
  .header__post  {
    font-size: 30px; /* 화면 너비가 601px에서 1024px 사이일 때 글자 크기 조정 */
  }
}

/* 큰 화면 크기에 대한 스타일 */
@media (min-width: 1025px) {
  .header__friend,
  .header__chat,
  .header__user,
  .header__user--login,
  .header__user--profile,
  .header__map,
  .header__post  {
    font-size: 40px; /* 화면 너비가 1025px 이상일 때 글자 크기 조정 */
  }
}
</style>
