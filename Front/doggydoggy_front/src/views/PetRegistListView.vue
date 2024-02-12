<script setup>
/*
  등록된 펫 리스트 화면
  여기서 펫 등록이 가능하다.
*/
import { onMounted, ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

// 백엔드에서 반려동물 목록 데이터를 가져오기 위한 URL
const backendUrl = 'http://your-backend-url.com/pets';
const petsList = ref([]); // 반려동물 목록을 저장할 반응형 데이터
const router = useRouter();

// 백엔드로부터 반려동물 목록을 가져오는 함수
async function fetchPetsList() {
  try {
    const response = await axios.get(backendUrl);
    petsList.value = response.data; // 응답 데이터를 petsList에 저장
  } catch (error) {
    console.error('Error fetching pets list:', error);
  }
}

// 컴포넌트가 마운트될 때 반려동물 목록을 가져옴
onMounted(fetchPetsList);

function onClickRegisterPet() {
  router.push('/pet/register');
}
</script>


<template>
  <div class="petListContainer">
    <div class="petList">
      <div class="petItem" v-for="pet in petsList" :key="pet.id">
        <img :src="pet.imageUrl" :alt="pet.name" class="petImage">
        <div class="petName">{{ pet.name }}</div>
      </div>
    </div>
    <button @click="onClickRegisterPet" class="registerPetButton">반려동물 등록</button>
  </div>
</template>

<style scoped>
.petListContainer {
  padding: 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.petList {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  justify-content: center;
  margin-bottom: 24px;
}

.petItem {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.petImage {
  width: 80px; /* 조정이 필요할 수 있습니다 */
  height: 80px; /* 조정이 필요할 수 있습니다 */
  border-radius: 50%;
  object-fit: cover;
}

.petName {
  margin-top: 8px;
}

.registerPetButton {
  padding: 8px 16px;
}
</style>
