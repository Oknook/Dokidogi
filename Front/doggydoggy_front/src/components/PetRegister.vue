<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

/*
   반려동물 등록을 위한 컴포넌트
   등록번호와 이름 입력
*/
const router = useRouter()
const petregisterNm = ref('')
const username = ref('')
const petAPI = function () {
  axios({
    method: 'get',
    url:  `//apis.data.go.kr/1543061/animalInfoSrvc/animalInfo?dog_reg_no=${petregisterNm}&owner_nm=${username}&serviceKey=${API_key}`,

  })
  .then((res) => {
    router.push({
      name: 'register-detail',
      query: {data: JSON.stringify(res.data)}
     });
  })
  .catch((err) => {
    console.log(err)
  })
}
</script>

<template>
  <div class="petRegister">
    <div>반려동물 등록창</div>
    <form @submit.prevent="petAPI">
      <div>
        <label for="registerID">등록번호: </label>
        <input type="text" id="registerID" v-model="petregisterNm">
      </div>
      <div>
        <label for="username">성함: </label>
        <input type="text" id="username" v-model="username">
      </div>
      <button type="submit">등록</button>
    </form>
  </div>
</template>

<style scoped>
.petRegister {
  height: 100%;
  text-align: center;
  display: flex;
  flex-direction: column;
  margin-top: 50%;
  gap: 10px;
}

.petRegister button {
  margin-top: 10px;
}
</style>
