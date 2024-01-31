<script setup>
import { useUserStore } from '@/stores/userStore';
const userStore = useUserStore();
</script>

<template>
  <div class=".home"></div>
</template>

<style scoped>
.home {
  height: 100%;
  background-color: whitesmoke;
  text-align: center;
}
</style>
