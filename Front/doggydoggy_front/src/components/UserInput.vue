
<script setup>
/*
  카카오 로그인 요청 후, 토큰을 받아오면 토큰을 쿠키에 저장하고 UserInput Page로 넘어온다.
  거기서 볼 화면이 이 UserInput Component

  사용자의 닉네임, 생년월일(6자리), 성별을 받는다. 그리고 이를 로컬 스토리지에 저장한다.

*/
import { useUserStore } from '@/stores/userStore';
import { ref, ssrContextKey,onMounted } from 'vue';

import { useRouter } from 'vue-router';
import { loginCheck } from '@/components/common/Header.vue';
import axios from 'axios';

const router = useRouter();
const userStore = useUserStore();
console.log(loginCheck.value);
const nickname = ref('');
const birthday = ref('');
const sex = ref('');

const postcode = ref('');
const address = ref('');
const detailAddress = ref('');

const latitude= ref('');
const longitude= ref('');



const dog_nickname = ref('');
const dog_birthday = ref('');
const dog_sex = ref('');
const dog_dd = ref('');

let accessToken = ref('');

// 카카오에서 넘어온 인가코드를 URL에서 추출하여 코드 변수에 저장
const boardFile = ref(null); // 파일 데이터를 저장할 ref

const uploadedImageUrl = ref(null);
const handleFileUpload = (event) => {
  const file = event.target.files[0];
  boardFile.value = file;
  // Optional: Implement client-side logic to preview the image before uploading
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      uploadedImageUrl.value = e.target.result;
    };
    reader.readAsDataURL(file);
  }
};

const loadDaumPostcode = () => {
  return new Promise((resolve, reject) => {
    if (window.daum && window.daum.Postcode) {
      return resolve();
    }
    const script = document.createElement('script');
    script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
    script.onload = () => resolve();
    script.onerror = () => reject(new Error('Failed to load the Daum Postcode script'));
    document.head.appendChild(script);
  });
};

const openPostcodePopup = async () => {
  await loadDaumPostcode();
  new window.daum.Postcode({
    oncomplete: (data) => {
      postcode.value = data.zonecode;
      address.value = data.address;
      detailAddress.value = ''; // Clear the detail address field for user input
    }
  }).open();
};

function getCodeAndRedirect() {
  const urlParams = new URL(window.location.href).searchParams;
  accessToken.value = urlParams.get('token');

  if (accessToken.value) {
    console.log('Token:', accessToken.value);
    // You can now use the token to make authenticated requests
  }
}




function clickSubmit() {
  console.log("현재 로그인 상태:", userStore.isLoggedIn);
  userStore.setLoginStatus(true);

  console.log("현재 로그인 상태:", userStore.isLoggedIn);
  router.push('/')

}


onMounted(() => {
  getCodeAndRedirect();
});

</script>

<template>
  <form action="" @submit.prevent="clickSubmit" class="form-container">
    <div class="input-group">
      <div class="form__nickname">
        <label for=""><b>닉네임: </b></label>
        <input type="text" v-model="nickname" />
      </div>
      <div>
        <b>성별: </b>
        <label for="M">남성</label>
        <input type="radio" name="sex" id="M" value="M" v-model="sex" />
        <label for="F">여성</label>
        <input type="radio" name="sex" id="F" value="F" v-model="sex" />
      </div>
      <div>
        <b>생년월일: </b>
        <input
            type="text"
            placeholder="생년월일 6자리..."
            maxlength="6"
            minlength="0"
            pattern="\d*"
            v-model="birthday"
        />
      </div>
      <div class="form__address">
        <label><b>우편 번호:</b></label>
        <input type="text" v-model="postcode" readonly />
        <button type="button" @click="openPostcodePopup">우편 번호 찾기</button>
      </div>
      <div>
        <label><b>주소:</b></label>
        <input type="text" v-model="address" readonly />
      </div>
      <div>
        <label><b>상세 주소:</b></label>
        <input type="text" v-model="detailAddress" />
      </div>


      <div class="form__nickname">
        <label for=""><b>반려동물 이름: </b></label>
        <input type="text" v-model="dog_nickname" />
      </div>

      <div class="form__nickname">
        <label for=""><b>반려동물 종류: </b></label>
        <input type="text" v-model="dog_dd" />
      </div>


      <div class="form__nickname">
        <label for=""><b>반려동물 성별: </b></label>
        <input type="text" v-model="dog_sex" />
      </div>

      <div class="form__nickname">
        <label for=""><b>반려동물 나이: </b></label>
        <input type="text" v-model="dog_birthday" />
      </div>


      <div class="form-group">
        <input type="file" @change="handleFileUpload" />
        <div v-if="uploadedImageUrl">
          <img :src="uploadedImageUrl" alt="Uploaded Picture" style="width: 200px; height: auto;">
        </div>
      </div>
      <button class="form-b">등록</button>
    </div>

  </form>
</template>

<style scoped>
.form-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 600px;
}

.input-group {
  display: flex;
  flex-direction: column;
  height: 900px;
  width: 80%;
  border: 1px solid black;
  border-radius: 20px;
  padding: 20px;
}

.input-group div {
  margin: 10px 0;
}

form button {
  width: 50%;
  margin-left: 30px;
  background-color: rgb(0, 123, 255);
  border-radius: 15px;
  border: none;
  box-shadow: none;
  height: 30px;
  color: white;
}

b {
  margin-right: 15px;
}

.form-b {
  margin-top: 30px;
  background-color: rgb(0, 123, 255);
  border-radius: 15px;
  border: none;
  box-shadow: none;
  height: 30px;
  color: white;
}

.userInput {

  height: 1200px;
}

</style>
