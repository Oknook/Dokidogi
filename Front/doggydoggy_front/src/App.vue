<template>
  <div>
    <Header></Header>
    <router-view />
  </div>
</template>

<script>
import HomeView from './views/HomeView.vue';
import Header from '@/components/common/Header.vue';
export default {
  components: {
    'HomeView': HomeView,
    'Header': Header,
  },
  methods: {},
};
</script>

<style scoped></style>
