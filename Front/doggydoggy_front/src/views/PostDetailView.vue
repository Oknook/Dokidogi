<template>
  <div class="detail-container">
    <h1>게시글 상세 정보</h1>
    <div class="post-details">
      <h3>[{{ store.detailPost.boardId }} 번 글]</h3>
      <h2>{{ store.detailPost.title }}</h2>
      <p>작성일: {{ formatDate(store.detailPost.boardCreatedTime) }}</p>
      <p>수정일: {{ formatDate(store.detailPost.boardUpdatedTime) }}</p>
      <p>카테고리 : {{ store.detailPost.boardCategory }}</p>
      <img v-if="fullImgUrl" :src="fullImgUrl" alt="게시글 이미지">


      <button class="button like-button" @click="likePost">좋아요</button>
      <span>좋아요 수: {{ store.detailPost.likes }}</span>
      <button class="button edit-button" @click.prevent="updatePost">게시글 수정</button>

      <hr>
    </div>

    <div class="post-contents">

      <p>{{ store.detailPost.contents }}</p>
      <br><br><br>
      <hr>
      <button class="button delete-button" @click.prevent="deletePost(store.detailPost.boardId)">게시글
        삭제
      </button>
    </div>
    <div class="comments-section">
      <h3>댓글</h3>
      <ul class="comments-list">
        <li v-for="comment in store.commentList" :key="comment.id" class="comment-item">
          <div class="comment-details">
            <!--                      <span class="comment-user">{{ comment.user.username }}</span>-->
            <br>
            <span class="comment-contents">{{ comment.commentContents }}</span>
          </div>
          <button class="button delete-comment"
                  @click="deleteComment(store.detailPost.boardId, comment.commentId)">삭제
          </button>
        </li>
      </ul>
    </div>
    <form class="comment-form" @submit.prevent="createComment">
      <label for="contents">댓글:</label>
      <textarea id="contents" v-model="commentContents" cols="30" name="contents"
                rows="10"></textarea>
      <button class="button submit-comment" type="submit">댓글 생성</button>
    </form>
  </div>
</template>

<script setup>

import {computed, onMounted, ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {usePostStore} from '../stores/posts';
import {useCommentStore} from '@/stores/comments';

const router = useRouter()
const route = useRoute()
const store = usePostStore()
const commentContents = ref('');
const store2 = useCommentStore()

const fullImgUrl = computed(() => {
  // console.log(store.detailPost.storedFileName)
  if (store.detailPost.storedFileName) {
    return `https://donghotest.s3.ap-northeast-2.amazonaws.com/${store.detailPost.storedFileName}`;
  }
  return ''; // 이미지 URL이 없는 경우 빈 문자열 반환
});

// const filteredComments = computed(() => {
//   return store.commentList.filter(comment => comment.article.pk === store.detailPost.id);
// });

onMounted(() => {
  store.getDetailPost(route.params.boardId)
  store.getCommentList(route.params.boardId)
  console.log('Complete Comment List:', store.commentList)
})

const createComment = () => {
  store.createComment(route.params.boardId, {commentContents: commentContents.value})
      .then(() => {
        commentContents.value = '';
        store.getCommentList(route.params.boardId)
      })
      .catch(err => {
        console.error('Error creating comment:', err);
      });
};

const deleteComment = (boardId, commentId) => {
  store.deleteComment(boardId, commentId)
      .then(() => {
        console.log("Deleting comment:", boardId, commentId)
        store.getCommentList(boardId);
      })
      .catch(err => {
        console.error('Error deleting comment:', err);
      });
};

onMounted(() => {
  store2.getCommentList(route.params.boardId)
})

const deletePost = function (boardId) {

  // store.deletePost(store.detailPost.boardId)
  store.deletePost(boardId)
      .then(() => {
        // store.deletePost(route.params.boardId)
        store.getPostList();
        console.log(boardId)
        router.push({name: 'communities'});
      })
      .catch(err => {
        console.error('Error deleting post:', err);
      })
}

const updatePost = function () {
  router.push({name: 'detailupdate'})
}
const putPost = function () {
  store.deletePost(store.detailPost.boardId)
      .then(() => {
        router.push({name: 'posts'});
      })
      .catch(err => {
        console.error('Error deleting post:', err);
      })
}

const likePost = () => {
  store.likePost(store.detailPost.boardId).then(() => {
    // 좋아요 성공 시, 게시글 정보 다시 불러오기
    store.getDetailPost(store.detailPost.boardId);
  }).catch(err => {
    // 오류 처리
  });
};

const formatDate = (dateString) => {
  const options = {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    hour12: true
  };
  return new Date(dateString).toLocaleString('ko-KR', options).replace('오전', 'AM').replace('오후',
      'PM');
};
</script>

<style scoped>

div {
  max-width: 800px;
  margin: auto;
  padding: 20px;
}


h1 {
  text-align: center;
  margin-bottom: 1rem;
}


p {
  font-size: 1rem;
  color: #333;
}

.button {
  padding: 10px 20px;
  color: white;
  background-color: darkgreen;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.button:hover {
  background-color: #005f00;
}

.category-item {
  background-color: #f9f9f9;
  padding: 10px;
  border-radius: 5px;
  margin-bottom: 10px;
}


form {
  margin-top: 1rem;
}

textarea {
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 10px;
  margin-bottom: 1rem;
}

hr {
  margin-top: 2rem;
}

.comments-section h3 {
  margin-top: 20px;
}

.comment-item {
  display: flex;
  justify-contents: space-between;
  align-items: center;
  margin-bottom: 10px;
  background-color: #f9f9f9;
  padding: 10px;
  border-radius: 5px;
}

.comment-details {
  flex: 1;
}

.comment-user {
  font-weight: bold;
  margin-right: 10px;
}

.delete-comment {
  margin-left: 10px;
}

.post-contents h2, .post-contents h3 {
  margin: 0;
  padding: 0.5em 0;
}

.post-contents p {
  margin: 0;
  padding: 0.5em 0;
}

.comments-list {
  list-style: none;
  padding-left: 0;
}

.comment-form {
  margin-top: 2em;
}

.comment-form textarea {
  width: 100%;
  padding: 0.5em;
  margin-bottom: 1em;
}
</style>