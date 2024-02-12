import axios from 'axios'
import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useCommentStore = defineStore('post', () => {

  const commentList = ref([])

  
  const getCommentList = function (pk) {
    axios({
      method: 'get',
      url: `/api/board/${pk}`,
      // headers: {
      //   Authorization: `Token ${store.token}`
      // }
    })
    .then((res) => {
      commentList.value = res.data
      console.log(commentList.value) 
    })
    .catch(err => console.log(err))
  }
    // const deletePost = function (pk) {
    //   return axios({
    //     method: 'delete',
    //     url: `http://127.0.0.1:8000/communities/${pk}/`,
    //   })
    // }

  


  return { commentList, getCommentList }
})