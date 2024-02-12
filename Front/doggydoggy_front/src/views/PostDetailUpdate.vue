<template>
  <div class="post-creation-container">
    <h1>게시글 수정</h1>
    <form @submit.prevent="updatePost" class="post-form">
      <div class="form-group">
        <label for="title">제목:</label>
        <input id="title" v-model="updateData.title" type="text" class="form-control" />
      </div>
      <div class="form-group">
        <label for="contents">내용:</label>
        <textarea id="contents" v-model="updateData.contents" class="form-control"></textarea>
      </div>
      <div class="form-group">
        <label for="category">카테고리:</label>
        <select name="category" v-model="selectedCategory" class="form-control">
          <option v-for="category in categories" :key="category" :value="category">{{ category }}</option>
        </select>
      </div>

      <button type="submit" class="submit-btn">수정하기</button>
    </form>
  </div>
</template>
  
  <script setup>
  import { onMounted, ref } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { usePostStore } from '../stores/posts';
  
  const route = useRoute();
  const router = useRouter();
  const store = usePostStore();
  const categories = ref(['AAA', 'BBB', 'CCC', 'DDD']);
  const selectedCategory = ref('');
  const updateData = ref({ title: '', contents: '', boardCategory: ''});
  
  onMounted(async () => {
    await store.getDetailPost(route.params.boardId);
    updateData.value = { ...store.detailPost };
    selectedCategory.value = store.detailPost.boardCategory; // 현재 카테고리 값으로 초기화
  });

  const updatePost = async () => {
    try {
      await store.putPost(route.params.boardId, {
        title: updateData.value.title,
        contents: updateData.value.contents,
        boardCategory: selectedCategory.value
      });
      router.push({ name: 'detail' });
    } catch (err) {
      console.error('Error updating post:', err);
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