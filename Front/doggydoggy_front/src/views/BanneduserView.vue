<template>
    <div>
      <Header></Header>
      <div>
        <h2>차단된 사용자</h2>
        <ul>
          <li v-for="user in blockedUsers" :key="user.id">
            {{ user.name }}
            <button @click="unblockUser(user.id)">차단 해제</button>
          </li>
        </ul>
      </div>      
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import Header from '@/components/common/Header.vue';

  const blockedUsers = ref([]);
  
  onMounted(() => {
    fetchBlockedUsers();
  });
  
  function fetchBlockedUsers() {
    // 백엔드 URL을 사용하여 차단된 사용자 목록을 가져옵니다.
    fetch('YOUR_BACKEND_URL/blocked-users')
      .then(response => response.json())
      .then(data => {
        blockedUsers.value = data;
      })
      .catch(error => console.error('차단된 사용자를 불러오는 데 실패했습니다:', error));
  }
  
  function unblockUser(userId) {
    // 백엔드 URL을 사용하여 사용자 차단을 해제합니다.
    fetch(`YOUR_BACKEND_URL/unblock-user/${userId}`, {
      method: 'POST',
      // 필요한 헤더, 바디 또는 자격증명을 추가합니다.(토큰 방식?)
    })
    .then(response => {
      if (response.ok) {
        console.log('사용자 차단이 해제되었습니다.');
        fetchBlockedUsers(); // 차단된 사용자 목록을 새로고침합니다.
      } else {
        console.error('사용자 차단 해제에 실패했습니다.');
      }
    })
    .catch(error => console.error('사용자 차단 해제 중 오류가 발생했습니다:', error));
  }
  </script>
  
  <style>
  /* 여기에 스타일을 추가하세요 */
  </style>