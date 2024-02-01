<script setup>
/*
  카카오 로그인 요청 후, 토큰을 받아오면 토큰을 쿠키에 저장하고 UserInput Page로 넘어온다.
  거기서 볼 화면이 이 UserInput Component

  사용자의 닉네임, 생년월일(6자리), 성별을 받는다. 그리고 이를 로컬 스토리지에 저장한다.

*/
import { useUserStore } from '@/stores/userStore';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { loginCheck } from '@/components/common/Header.vue';

const router = useRouter();
const userStore = useUserStore();
console.log(loginCheck.value);
const nickname = ref('');
const birthday = ref('');
const sex = ref('');

function clickSubmit() {
  console.log(nickname.value, sex.value, birthday.value);
  localStorage.setItem('nickname', nickname.value);
  localStorage.setItem('birthday', birthday.value);
  localStorage.setItem('sex', sex.value);
  loginCheck.value = $cookies.isKey('token');
  alert('정보 등록 완료!');
  router.push('/');
}
</script>

<template>
  <form action="" @submit.prevent="clickSubmit">
    <div class="form__nickname">
      <label for=""><b>닉네임: </b></label>
      <input type="text" v-model="nickname" />
    </div>
    <div>
      <b>성별: </b>
      <label for="M">남성</label>
      <input type="radio" name="sex" id="M" value="M" v-model="sex" />
      <label for="F">여성</label>
      <input type="radio" name="sex" id="F" value="F" v-model="sex" />
    </div>
    <div>
      <b>생년월일: </b>
      <input
        type="text"
        placeholder="생년월일 6자리..."
        maxlength="6"
        minlength="0"
        pattern="\d*"
        v-model="birthday"
      />
    </div>
    <button>등록</button>
  </form>
</template>

<style scoped>
form {
  height: 50%;
  width: 80%;
  border: 1px solid black;
  border-radius: 20px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}

form div {
  margin: 0 auto;
}
form button {
  width: 50%;
  margin: 0 auto;
}
</style>
