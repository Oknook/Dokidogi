<template>
  <div class="board-container">
    <h1>자유게시판</h1>
    <RouterLink :to="{name:'postcreate'}" class="create-post-button">게시글 생성</RouterLink>
    <!-- 카테고리 선택 드롭다운 메뉴 -->
    <select v-model="selectedCategory" @change="fetchPostsByCategory">
      <option value="All">모든 카테고리</option>
      <option value="Free">Free</option>
      <option value="Show">Show</option>
      <option value="Info">Info</option>
      <option value="Review">Review</option>
    </select>

    <ul class="post-list">
      <li v-for="post in store.postList" :key="post.boardId" class="post-item"
          @click="goDetail(post.boardId)">
        <div class="post-contents" role="button">
          <span class="likes-count">카테고리: {{ post.boardCategory }}</span>
          <span class="post-date">[{{ post.boardId }}]번 글 - {{
              formatCreatedAt(post.boardCreatedTime)
            }}</span>
          <h3 class="post-title">제목: {{ post.title }}</h3>
          <span class="likes-count">좋아요: {{ post.likes }}</span>

        </div>


      </li>
    </ul>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue';
import {usePostStore} from '@/stores/posts';
import {useRouter} from 'vue-router';

const store = usePostStore();
const router = useRouter();
const selectedCategory = ref('All')

onMounted(() => {
  fetchPostsByCategory();
});

const goDetail = (boardId) => {
  router.push({name: 'detail', params: {boardId}});
};

// 카테고리가 변경될 때 호출될 메소드
const fetchPostsByCategory = () => {
  if (selectedCategory.value === '') {
    store.getPostList(); // 모든 카테고리의 게시글을 가져옴
  } else {
    store.getPostsByCategory(selectedCategory.value); // 선택된 카테고리의 게시글을 가져옴
  }
};

const formatCreatedAt = (createdAt) => {
  const date = new Date(createdAt);
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  const hours = date.getHours();
  const minutes = date.getMinutes();
  const ampm = hours >= 12 ? '오후' : '오전';
  const formattedHours = hours % 12 || 12;

  return `${year}년 ${month}월-${day}일 ${ampm} ${formattedHours}:${minutes < 10 ? '0' + minutes
      : minutes}`;
};

const likePost = (postId) => {
  store.likePost(postId).then(() => {
    // 좋아요 성공 시 처리
  }).catch(err => {
    // 오류 처리
  });
};
</script>

<style scoped>
.board-container {
  max-width: 800px;
  margin: auto;
  font-family: 'Noto Sans KR', sans-serif;
}

.create-post {
  display: block;
  margin-bottom: 2rem;
  text-align: right;
  color: #007bff;
  cursor: pointer;
}

.create-post:hover {
  text-decoration: underline;
}

.post-list {
  list-style: none;
  padding: 0;
}

.post-item {
  margin-bottom: 1rem;
  padding: 1rem;
  background: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.post-item:hover {
  background-color: #f0f0f0;
}

.post-date {
  font-size: 0.9rem;
  color: #666;
}

.post-title {
  margin-top: 0.5rem;
  font-size: 1.2rem;
  color: #333;
}

hr {
  margin-top: 1.5rem;
  border: none;
  border-top: 1px solid #eee;
}

.post-contents[role="button"] {
  cursor: pointer;
}

.create-post-button {
  display: inline-block;
  padding: 10px 15px;
  background-color: darkgreen;
  color: white;
  text-align: center;
  text-decoration: none;
  border-radius: 15px;
  font-weight: bold;
  transition: background-color 0.3s ease;
}

.create-post-button:hover {
  background-color: green;
}
</style>
