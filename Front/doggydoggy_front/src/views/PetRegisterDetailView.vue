<template>
    <div>
        <Header></Header>
        <form @submit.prevent="addInfo" class="post-form">
          <div>
                <!-- 사진 입력 -->
              <input type="file" @change="handleFileUpload" accept="image/*">
              <div v-if="imageUrl">
                <img :src="imageUrl" alt="Uploaded Image" style="max-width: 200px; max-height: 200px;">
              </div>
          </div>
          <!-- 기본 정보 추가 입력 -->
          <div>
              <label for="dogname">이름:</label>
              <input type="text" name="dogname" v-model="dogname">
          </div>
          <div>
              <label for="owner">소유자:</label>
              <input type="text" name="owner" v-model="owner">
          </div>
          <div>
              <label for="birth">강아지 생년월일:</label>
              <input type="text" name="birth" v-model="birth">
          </div>
          <div>
              <label for="registernm">등록번호:</label>
              <input type="text" name="registernm" v-model="registernm">
          </div>
          <button type="submit">반려견 등록</button>
      </form>
      <button @click="gotoHome"> 반려견 등록 취소</button>
    </div>
    
</template>



<script setup>
import Header from '@/components/common/Header.vue'
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { usePetStore } from '../stores/petStore'

const petStore = usePetStore()
const dogname = ref('')
const owner = ref('')
const birth = ref('')
const registernm = ref('')
const imageUrl = ref(null)
const imageFile = ref(null)

const router = useRouter();

onMounted(() => {
    if (petStore.petData) {
        dogname.value = petStore.petData.body.item.dogNm
        owner.value = petStore.petData.username
        registernm.value = petStore.petData.body.item.registernm
    }
})

function handleFileUpload(event) {
    const file = event.target.files[0];
    if (file) {
        imageFile.value = file; // 선택된 이미지 파일 저장

        // FileReader API를 사용하여 이미지 파일의 데이터 URL 생성
        const reader = new FileReader();
        reader.onload = e => {
            imageUrl.value = e.target.result; // 데이터 URL을 imageUrl에 저장
        };
        reader.readAsDataURL(file); // 파일 읽기 시작
    }
}


async function addInfo() {
    // FormData 객체 생성
    const formData = new FormData();
    formData.append('dogname', dogname.value);
    formData.append('owner', owner.value);
    formData.append('birth', birth.value);
    formData.append('registernm', registernm.value);
    if (imageFile.value) {
        formData.append('image', imageFile.value); // 이미지 파일 추가
    }   


    // axios를 사용하여 백엔드로 POST 요청 전송
    try {
        const response = await axios.post('http://your-backend-url.com/pet', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        console.log(response.data);
        // 성공적으로 데이터를 전송한 후의 처리 (예: 라우터를 사용하여 다른 페이지로 이동)
        router.push({name: 'register-list'});
    } catch (error) {
        console.error(error);
    }
}

</script>

<style>

</style>