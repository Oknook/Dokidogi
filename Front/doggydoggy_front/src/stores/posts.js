import axios from 'axios'
import {ref} from 'vue'
import {defineStore} from 'pinia'

export const usePostStore = defineStore('post', () => {

  const postList = ref([])
  const commentList = ref([])
  const getPostList = function () {
    axios({
      method: 'get',
      url: '/api/board/', // URL을 Spring Boot 백엔드 엔드포인트로 변경
      // headers 부분은 Spring Boot 백엔드의 보안 설정에 따라 필요할 수 있습니다.
      // headers: {
      //   Authorization: `Bearer ${store.token}`b
      // }
    })
    .then((res) => {
      postList.value = res.data;
      console.log(postList.value);
    })
    .catch(err => console.log(err));
  }

  const detailPost = ref([])

  const getDetailPost = function (boardId) {
    axios({
      method: 'get',
      url: `/api/board/${boardId}`,
      // headers: {
      //   Authorization: `Token ${store.token}`
      // }
    })
    .then(res => {
      detailPost.value = res.data
    })
    .catch(err => console.log(err))
  }
  const deletePost = function (boardId) {
    return axios({
      method: 'post',
      url: `/api/board/${boardId}/delete`,
    })
    .then(response => {
      // 삭제 성공 후 처리 로직
      console.log('success')
    })
    .catch(err => console.error(err));
  };

  const updatePost = async (boardId, formData) => {
    try {
      const response = await axios.post(`/api/board/${boardId}/update`, formData, {
        headers: {'Content-Type': 'multipart/form-data'},
      });
      console.log("게시글이 성공적으로 업데이트 되었습니다.", response.data);
    } catch (err) {
      console.error("게시글 업데이트 중 에러 발생", err);
    }
  };

  const getCommentList = function (boardId) {
    axios({
      method: 'get',
      url: `/api/board/${boardId}/comment`,
      //
      // headers: {
      //   Authorization: `Token ${store.token}`
      // }
    })
    .then((res) => {
      commentList.value = res.data

    })
    .catch(err => console.log(err))
  }

  // 댓글 작성 함수
  const createComment = function (boardId, {commentContents}) {
    return axios({
      method: 'post',
      url: `/api/board/${boardId}/comment/save`,
      // headers: {
      //   Authorization: `Token ${store.token}`
      // },
      data: {
        commentContents,
        boardId,
      }
    })
    .then(response => {
      // 성공적으로 댓글이 생성된 후의 로직 (예: 댓글 목록 갱신)
      console.log(response.data)
      const newComment = response.data;
      commentList.value.push(newComment);
      console.log(commentList.value)

    })
    .catch(err => console.error(err));
  }

  const deleteComment = function (boardId, commentId) {
    return axios({
      method: 'post',
      url: `/api/board/${boardId}/comment/${commentId}/delete`,
      // headers: {
      //   Authorization: `Token ${store.token}`
      // }
    })
    .then(response => {
      // 댓글 삭제 후 로직, 예: 댓글 목록 갱신
    })
    .catch(err => console.error(err));
  }

  // 좋아요 증가 로직
  const likePost = (boardId) => {
    return axios.post(`/api/board/${boardId}/like`).then(() => {
      // 좋아요 후 게시글 상세 정보 업데이트
      return getDetailPost(boardId);
    });
  };

  return {
    postList,
    getPostList,
    detailPost,
    getDetailPost,
    deletePost,
    updatePost,
    getCommentList,
    commentList,
    createComment,
    deleteComment,
    likePost
  }
})