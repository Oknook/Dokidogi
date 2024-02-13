<script setup>

import { useUserStore } from '@/stores/userStore';
import axios from 'axios';
import { onMounted } from 'vue';
const userStore = useUserStore();


</script>

<template>
  <div class=".home">
    <button @click="press">button</button>
  </div>
</template>

<style scoped>
.home {
  height: 100%;
  background-color: whitesmoke;
  text-align: center;
}
</style>
