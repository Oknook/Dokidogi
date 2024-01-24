<template>
  <div><button>카카오 로그인</button></div>
</template>

<script>
export default {
  created: function () {
    console.log(window.Kakao.isInitialized());
  },
};
</script>

<style scoped></style>
