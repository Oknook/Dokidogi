<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

function onClickRegisterPet() {
  router.push('/pet/register');
}
</script>

<template>
  <div class="petList">
    반려동물 목록 리스트
    <button @click="onClickRegisterPet">반려동물 등록</button>
  </div>
</template>

<style scoped>
.petList {
  height: 100%;
  display: flex;
  flex-direction: column;
  text-align: center;
}
</style>
