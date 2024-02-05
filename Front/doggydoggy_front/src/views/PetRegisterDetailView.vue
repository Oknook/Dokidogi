<template>
    <div>
        <Header></Header>
        <form @submit.prevent="addInfo" class="post-form">
          <div>
              <label for="title">이름:</label>
              <input type="text" name="title" v-model="title" class="form-control">
          </div>
          <div>
              <label for="owner">소유자:</label>
              <input type="text">
          </div>
          <div>
              <label for="birth">생년월일:</label>
              <input type="text">
          </div>
          <div>
              <label for="registernm">등록번호:</label>
              <input type="text">
          </div>
          <button type="submit">반려견 등록</button>
      </form>
      <button> 반려견 등록 취소</button>
    </div>
    
</template>



<!-- 사진입력, API 응답 데이터 -->
<script setup>
import Header from '@/components/common/Header.vue'
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { usePetStore } from './stores/petStore'
</script>

<style>

</style>