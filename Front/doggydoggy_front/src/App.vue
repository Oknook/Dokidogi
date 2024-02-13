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
* {
  margin: 0;
}

body {
  background: #ffffff;
}

body::-webkit-scrollbar {
  width: 5px;
  background: rgb(255, 255, 255);
}

body::-webkit-scrollbar-thumb {
  width: 5px;
  background: beige;
}
</style>
