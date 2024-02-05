<template>
    <div>
        <Header></Header>
        <p></p>
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