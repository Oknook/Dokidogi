<template>
  <div class="post-creation-container">
    <h1>게시글 수정</h1>
    <form class="post-form" @submit.prevent="updatePost">
      <div class="form-group">
        <label for="title">제목:</label>
        <input id="title" v-model="updateData.title" class="form-control" type="text"/>
      </div>
      <div class="form-group">
        <label for="contents">내용:</label>
        <textarea id="contents" v-model="updateData.contents" class="form-control"></textarea>
      </div>
      <div class="form-group">
        <label for="image">이미지:</label>
        <input id="image" class="form-control" type="file" @change="handleFileUpload"/>
        <!-- 기존 이미지가 있을 경우 보여주기 -->
        <img v-if="updateData.imageUrl" :src="updateData.imageUrl" alt="게시글 이미지" class="existing-image"/>
      </div>
      <div class="form-group">
        <label for="category">카테고리:</label>
        <select v-model="selectedCategory" class="form-control" name="category">
          <option v-for="category in categories" :key="category" :value="category">{{ category }}</option>
        </select>
      </div>

      <button class="submit-btn" type="submit">수정하기</button>
    </form>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {usePostStore} from '../stores/posts';

const route = useRoute();
const router = useRouter();
const store = usePostStore();
const categories = ref(['AAA', 'BBB', 'CCC', 'DDD']);
const selectedCategory = ref('');
const updateData = ref({title: '', contents: '', boardCategory: ''});

onMounted(async () => {
  await store.getDetailPost(route.params.boardId);
  updateData.value = {...store.detailPost};
  selectedCategory.value = store.detailPost.boardCategory; // 현재 카테고리 값으로 초기화
  // 이미지 URL 설정
  updateData.value.imageUrl = `https://donghotest.s3.ap-northeast-2.amazonaws.com/${store.detailPost.storedFileName}`;
});

const updatePost = async () => {
  const formData = new FormData();
  formData.append('title', updateData.value.title);
  formData.append('contents', updateData.value.contents);
  formData.append('boardCategory', selectedCategory.value);
  if (updateData.value.boardFile) {
    formData.append('boardFile', updateData.value.boardFile);
  }

  try {
    await store.updatePost(route.params.boardId, formData); // 비동기 함수 호출
    router.push({name: 'communities', params: {boardId: route.params.boardId}});
  } catch (err) {
    console.error('게시글 수정 중 오류 발생:', err);
  }

};

const handleFileUpload = event => {
  if (event.target.files.length > 0) {
    const file = event.target.files[0];
    updateData.value.boardFile = file;
  }
};

</script>

<style scoped>
.post-creation-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-contents: center;
  min-height: 80vh;
}

.post-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-control {
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  width: 700px;
}

.submit-btn {
  background-color: darkgreen;
  color: white;
  padding: 8px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  width: 150px;
  height: 50px;
}

.submit-btn:hover {
  background-color: #005600;
}
</style>