<template>
  <div><LoingHandler /></div>
</template>

<script>
/*
  로그인 요청 후, 인가코드르 받을 때까지 대기할 페이지
  여기서 로그인 핸들러 연산을 처리한다.

*/
import LoingHandler from '@/components/LoingHandler.vue';
export default {
  components: {
    LoingHandler,
  },
};
</script>

<style scoped></style>
