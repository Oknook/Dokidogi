<template>
  <div class="App">
    <Header></Header>
    <div class="content">
      <router-view />
    </div>
    <Footer></Footer>
  </div>
</template>

<script>
import HomeView from './views/HomeView.vue';
import Header from '@/components/common/Header.vue';
import Footer from './components/common/Footer.vue';

export default {
  components: {
    'HomeView': HomeView,
    'Header': Header,
    'Footer': Footer,
  },
  methods: {},
};
</script>

<style scoped>
.App {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.content {
  flex: 1;
}

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
