<template>
  <div>
    <router-view />
  </div>
</template>

<script>
import HomeView from './views/HomeView.vue';

HomeView;
export default {
  components: {
    'HomeView': HomeView,
  },
};
</script>

<style scoped></style>
