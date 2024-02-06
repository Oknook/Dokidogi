<script setup>
/*
  UserProfile에 사용자 정보 표시

*/
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import header from '@/components/common/Header.vue'

const router = useRouter();
const route = useRoute()

const nickname = ref('')
const birthday = ref('');
const sex = ref('');
// 백엔드에서 사용자 정보를 가져오는 함수
async function fetchUserInfo() {
  try {
    const response = await axios.get(`http://your-backend-url.com/user/${userID.value}`);
    const data = response.data;
    // 데이터를 로컬 상태에 할당
    nickname.value = data.nickname;
    birthday.value = data.birthday;
    sex.value = data.sex;
    // 필요한 경우 localStorage에 저장
    localStorage.setItem('nickname', data.nickname);
    localStorage.setItem('birthday', data.birthday);
    localStorage.setItem('sex', data.sex);
  } catch (error) {
    console.error('Error fetching user info:', error);
  }
}


const userID = ref(null)
onMounted(() => {
  userID.value = route.params.userid
  if (userID.value) {
    fetchUserInfo()
  }
})



function onClickRegisterPet() {
  router.push('/pet/register-list');
}

function seebannedusers() {
  router.push({name: 'banned-user'})
}
</script>

<template>
  <div>
    <header></header>
    <div class="userProfile">
    <div class="userProfile__detail">
      <b>견주 프로필</b>
      <br />
      <b>닉네임</b>
      <p>{{ nickanme }}</p>
      <b>나이</b>
      <p>{{ birthday }}</p>
      <b>성별</b>
      <p>{{ sex }}</p>
      <button @click="onClickRegisterPet">반려동물 등록</button>
      <button @click="profileupdate">프로필 수정</button>
      <!-- 유저일때만 채팅방 참여 버튼 보이게 만들기 -->
      <button v-if="howdareyou" @click="gotochatroom">채팅방 참여</button>
      <button @click="seebannedusers">차단 목록</button>
    </div>
  </div>
  </div>
  
</template>

<style scoped>
.userProfile {
  width: 100%;
  height: 100%;
  background-color: whitesmoke;
  display: flex;
  justify-content: center;
}

.userProfile__detail {
  width: 80%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: left;
  margin-top: 10%;
  gap: 10px;
}

.userProfile__detail * {
  margin-left: 5%;
}

.userProfile__detail p {
  margin-top: 0;
  margin-bottom: 0;
}

.userProfile__detail button {
  border-radius: 20px;
}
</style>
