<template>
    <div>
        <h1>새 글</h1>
        <form @submit.prevent="createComment">
        <label for="content">내용:</label>
        <textarea name="content" id="content" cols="30" rows="10" v-model="commentContents"></textarea>

        <button>게시글 생성</button>
        </form>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

import { useRouter, useRoute } from 'vue-router'


const title = ref('')
const commentContents = ref('')
const router = useRouter()
const route = useRoute()

const pk = route.params.pk // 게시글 ID 가져오기


const createComment = function () {
  axios({
    method: 'post',
    url: `/api/comment/save`,
    data: {
      commentContents: commentContents.value,
      boardId: boardId // 게시글 ID 포함
    },
    // headers: {
    //   Authorization: `Token ${store.token}`
    // }
  })
    .then((res) => {
      router.push({ name: 'detail', params: { boardId: boardId } }) // 게시글 상세보기로 이동
    })
    .catch((err) => {
      console.log(err)
    })
}



</script>

<style scoped>

</style>