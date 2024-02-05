<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { usePetStore } from '../stores/petStore'

/*
   반려동물 등록을 위한 컴포넌트
   등록번호와 이름 입력
*/
const router = useRouter()
const petregisterNm = ref('')
const username = ref('')
const API_key = ref('')
const petStore = usePetStore()

const petAPI = async () => {
  try {
    const response = await axios.get(`//apis.data.go.kr/1543061/animalInfoSrvc/animalInfo?dog_reg_no=${petregisterNm}&owner_nm=${username}&serviceKey=${API_key}`);
    if (response.data) {
      petStore.setPetData(response.data); // 스토어에 데이터 저장
      router.push('/register-detail'); // 페이지 이동
    }   
  } catch (error) {
    console.error(error);
  }
};


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
