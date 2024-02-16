
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
  position: relative; /* Adjusted to fit within Vue component structure */
  margin-top: 50px;
  margin-left: 28%;
  width: 100%;
  height: 1100px;
  padding-bottom: 100px;


  box-sizing: border-box; /* Include padding in width calculation */
  background: #FFFFFF;
  border: 1px solid #008000;
  //box-shadow: 7px 7px 39px rgba(0, 104, 255, 0.25);
  box-shadow: 1px 1px 2px #008000;
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  align-items: center; /* Center items for better visual alignment */
}

.input-group {
  display: flex;
  flex-direction: column;
  width: 100%; /* Ensure input group takes full width of the container */
}

input[type="text"],
input[type="file"],
input[type="password"], /* Added password input type for styling */
button {
  padding: 10px;
  margin: 10px 0; /* Add space above and below each field */
  border: none;
  border-bottom: 1px solid #CFCFCF; /* Style for bottom border only */
  width: 100%; /* Full width to match container's width */
  box-sizing: border-box; /* Include padding in width calculation */
}

/* Override for buttons to reset border style */
button {
  border: #008000 solid 1px;
  background-color: #FFFFFF;
  color: #008000;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #e7f3ff; /* Lighten background on hover */
}

label {
  color: lightgrey;
  font-weight: normal; /* Reset font weight to match reference style */
}

/* Specific styles for radio buttons and checkboxes */
.radio,
input.agree {
  margin-right: 5px; /* Add space between radio/checkbox and label text */
}

/* Add some styles for the header to match reference */
h2 {
  color: #0068FF;
  font-weight: 700;
  font-size: 32px;
  text-align: center; /* Center the header text */
  margin-bottom: 20px; /* Add some space below the header */
}

/* Add styles for form elements alignment and spacing */
.form-group, .input-group div {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 100%;
}

/* Adjustments for file upload section to better match the overall style */
.form-group {
  border: 1px dashed #ccc;
  padding: 10px;
  width: calc(100% - 20px); /* Adjust width to account for padding */
  margin: 10px 0; /* Consistent margin with other fields */
  align-items: center; /* Center content */
  justify-content: center;
}

.form-group img {
  margin-top: 10px; /* Add space above the image */
  max-width: 100%; /* Ensure image does not overflow its container */
  height: auto; /* Maintain aspect ratio */
}

/* Responsive adjustments */
.form-container {
  margin-bottom: auto; /* This pushes the footer down */
  padding: 50px 100px; /* Adjusted padding */
  max-width: 670px;
  width: 100%;
  /* Rest of your styles... */
}

body, html {
  height: 100%;
  margin: 0;
  display: flex;
  flex-direction: column;
}

.input-group {
  height: 2000px;
}
/* Adjust the main content container to grow and take necessary space */

userInput{
  height: 1500px;

}
</style>