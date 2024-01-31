<script setup>
import { useUserStore } from '@/stores/userStore';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const userStore = useUserStore();

const nickname = ref('');
const birthday = ref('');
const sex = ref('');
function clickSubmit() {
  console.log(nickname.value, sex.value, birthday.value);
  localStorage.setItem('nickname', nickname.value);
  localStorage.setItem('birthday', birthday.value);
  localStorage.setItem('sex', sex.value);
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
