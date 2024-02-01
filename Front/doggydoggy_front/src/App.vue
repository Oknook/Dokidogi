<template>
  <div class="App">
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

<style scoped>
.App {
  height: 150vh;
  display: flex;
  flex-direction: column;
  background-color: whitesmoke;
}
</style>
