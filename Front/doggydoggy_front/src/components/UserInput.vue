
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


let accessToken = ref('');

// 카카오에서 넘어온 인가코드를 URL에서 추출하여 코드 변수에 저장

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

async function convertAddressToCoords(address) {
  try {
    const response = await axios.get(`https://dapi.kakao.com/v2/local/search/address.json`, {
      params: { query: address },
      headers: { Authorization: `KakaoAK {f263f4cccf14d7c0b53113b856a963a5}` } // 여기서 {REST_API_KEY}는 발급받은 JavaScript 키를 사용합니다.
    });
    if (response.data.documents.length > 0) {
      const coords = response.data.documents[0];
      console.log(coords)
      return { latitude: coords.y, longitude: coords.x };
    } else {
      return null;
    }
  } catch (error) {
    console.error("주소-좌표 변환 에러", error);
    return null;
  }
}




async function clickSubmit() {
  // 비동기 함수 내에서 await 사용
  console.log(nickname.value, sex.value, birthday.value);
  localStorage.setItem('nickname', nickname.value);
  localStorage.setItem('birthday', birthday.value);
  localStorage.setItem('sex', sex.value);

  // 주소로부터 위도와 경도 변환
  try {
    const coords = await convertAddressToCoords(address.value + " " + detailAddress.value);
    if (coords) {
      console.log(`위도: ${coords.latitude}, 경도: ${coords.longitude}`);
      // 위도와 경도를 로컬 스토리지에 저장하거나 백엔드로 전송할 수 있습니다.
      latitude.value = coords.latitude;
      longitude.value = coords.longitude;
      // 백엔드로 사용자 정보 및 위도, 경도 전송
      await sendUserInfo();
    } else {
      console.error("주소로부터 위도와 경도를 변환할 수 없습니다.");
    }
  } catch (error) {
    console.error("주소-좌표 변환 에러", error);
  }

  // loginCheck.value = $cookies.isKey('token');
  alert('정보 등록 완료!');
  router.push('/');
}

/*
  백엔드 Spring에 입력한 정보를 보내는 Post request

*/
async function sendUserInfo() {
  await axios
      .post('https://i10b202.p.ssafy.io/api/user/signup', {
        nickname: nickname.value,
        sex: sex.value,
        birth: birthday.value,
        latitude: latitude.value,
        longitude: longitude.value,
      }, {
        headers: {
          'Authorization': accessToken.value
        }
      })
      .then((response) => {
        console.log(response);
      })
      .catch((error) => {
        console.error('There was an error sending the user info:', error);
      });
}


onMounted(() => {
  getCodeAndRedirect();
});

</script>

<template>
  <form action="" @submit.prevent="clickSubmit">
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

    
    <!-- Submit button -->


    <button>등록</button>
  </form>
</template>

<style scoped>
form {
  height: 50%;
  width: 80%;
  border: 1px solid black;
  border-radius: 20px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}

form div {
  margin: 0 auto;
}
form button {
  width: 50%;
  margin: 0 auto;
}
</style>
