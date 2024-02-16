<script setup>
/*
  회원가입 후, 사용자의 정보를 입력하는 페이지

*/
import UserInput from '@/components/UserInput.vue';
</script>

<template>
  <div class="userInput">
    <UserInput />
  </div>
</template>

<style scoped>
.userInput {
  width: 100%;
  height: 100%;
  padding-bottom: 100px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
</style>
