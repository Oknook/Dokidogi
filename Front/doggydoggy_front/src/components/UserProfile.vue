<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const nickanme = ref(localStorage.getItem('nickname'));
const birthday = ref(localStorage.getItem('birthday'));
const sex = ref(localStorage.getItem('sex'));

function onClickRegisterPet() {
  router.push('/pet/register-list');
}
</script>

<template>
  <div class="userProfile">
    <div class="userProfile__detail">
      <b>견주 프로필</b>
      <br />
      <b>닉네임</b>
      <p>{{ nickanme }}</p>
      <b>나이</b>
      <p>{{ birthday }}</p>
      <b>성별</b>
      <p>{{ sex }}</p>
      <button @click="onClickRegisterPet">반려동물 등록</button>
      <button>프로필 수정</button>
      <button>차단 목록</button>
    </div>
  </div>
</template>

<style scoped>
.userProfile {
  width: 100%;
  height: 100%;
  background-color: whitesmoke;
  display: flex;
  justify-content: center;
}

.userProfile__detail {
  width: 80%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: left;
  margin-top: 10%;
  gap: 10px;
}

.userProfile__detail * {
  margin-left: 5%;
}

.userProfile__detail p {
  margin-top: 0;
  margin-bottom: 0;
}

.userProfile__detail button {
  border-radius: 20px;
}
</style>
