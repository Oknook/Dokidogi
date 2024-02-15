<template>
  <div class="board-container">
    <h1>게시판</h1>
    <!-- 헤더 영역을 위한 컨테이너 추가 -->
    <div class="header-container">
    <!-- 카테고리 선택 드롭다운 메뉴 -->
    <select v-model="selectedCategory" @change="fetchPostsByCategory">
      <option value="All">모든 카테고리</option>
      <option value="Free">Free</option>
      <option value="Show">Show</option>
      <option value="Info">Info</option>
      <option value="Review">Review</option>
    </select>
<!--      게시글 생성 버튼-->
      <RouterLink :to="{name:'postcreate'}" class="create-post-button">게시글 생성</RouterLink>
      </div>
    <ul class="post-list">
      <li v-for="post in store.postList" :key="post.boardId" class="post-item"
          @click="goDetail(post.boardId)">
        <div class="post-contents" role="button">
          <!-- 제목과 작성일을 감싸는 컨테이너 -->
          <div class="post-header">
            <h3 class="post-title">[{{ post.boardCategory }}] {{ post.title }}</h3>
            <span class="post-date">작성일: {{ formatCreatedAt(post.boardCreatedTime) }}</span>
          </div>
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
body {
  font-family: 'Arial', sans-serif;
  background: #e9ecef;
  margin: 0;
  padding: 20px;
}

/* Board container styles */
.board-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
  max-width: 900px;
  margin: auto;
}

/* Post list styles */
.post-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

/* Individual post item styles */
.post-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #dee2e6;
}

.post-item:last-child {
  border-bottom: none;
}

/* Post content and details styles */
.post-contents {
  flex-grow: 1;
  padding: 10px 0;
}

.likes-count, .post-date, .post-title {
  display: block;
}

.likes-count {
  font-size: 0.85em;
  color: #6c757d;
}

.post-date {
  font-size: 0.75em;
  color: #adb5bd;
}

.post-title {
  font-weight: bold;
  color: #212529;
  margin: 5px 0;
}

/* Button styles */
.create-post-button {
  background-color: #008000;
  color: white;
  padding: 10px 15px;
  text-decoration: none;
  border-radius: 5px;
  font-size: 0.9em;
}

.create-post-button:hover {
  background-color: #218838;
}

/* Like button styles */
.like-button {
  background: none;
  border: none;
  cursor: pointer;
  color: #ff6b6b;
}

.like-button:hover {
  transform: scale(1.1);
}

/* Responsive design */
@media (max-width: 767px) {
  .board-container {
    padding: 15px;
  }

  .create-post-button {
    padding: 8px 10px;
    font-size: 0.8em;
  }
}

.post-header {
  display: flex;
  justify-content: space-between; /* 제목과 작성일을 양쪽 끝으로 정렬 */
  align-items: center; /* 세로 축 중앙 정렬 */
}

.post-title {
  /* 필요한 스타일을 추가하거나 수정하세요 */
  margin: 0; /* 상단 여백 제거 */
  font-size: 1.2rem;
}

.post-date {
  /* 필요한 스타일을 추가하거나 수정하세요 */
  font-size: 0.9rem;
  color: #666;
}

.header-container {
  display: flex;
  justify-content: space-between; /* 요소들을 양 끝으로 정렬 */
  align-items: center; /* 세로 축 중앙 정렬 */
  margin-bottom: 1rem; /* 컨테이너와 게시글 목록 사이의 간격 */
}

select {
  -moz-appearance: none;
  -webkit-appearance: none;
  appearance: none;

  font-family: "Noto Sans KR", sans-serif; /* 폰트 패밀리 오타 수정 */
  font-size: 1rem;
  font-weight: 400;
  line-height: 1.5;

  color: #444;
  background-color: #fff;

  padding: .6em 1.4em .5em .8em;
  margin: 0;

  border: 1px solid #aaa;
  border-radius: .5em;
  box-shadow: 0 1px 0 1px rgba(0,0,0,.04);

  width: calc(15% - 10px); /* 컨테이너 너비의 1/3에서 양 옆 마진을 고려한 값으로 조정 */
  cursor: pointer; /* 드롭다운 화살표 대신 커서 변경으로 클릭 가능 표시 */
  text-align-last: center; /* 드롭다운 내의 글씨를 중앙 정렬 */
}

select:hover {
  border-color: #888;
}

select:focus {
  border-color: #aaa;
  box-shadow: 0 0 1px 3px rgba(59, 153, 252, .7);
  color: #222;
  outline: none;
}

select:disabled {
  opacity: 0.5;
}
</style>
