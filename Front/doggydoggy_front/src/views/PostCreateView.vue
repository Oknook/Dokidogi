<template>
  <div class="post-creation-container">
    <h1>새 글 작성</h1>
    <form @submit.prevent="createPost" class="post-form">
      <div class="form-group">
        <label for="title">제목:</label>
        <input type="text" id="title" v-model="title" class="form-control">
      </div>
      <div class="form-group">
        <label for="content">내용:</label>
        <textarea id="content" v-model="contents" class="form-control"></textarea>
      </div>
      <div class="form-group">
        <label for="category">카테고리:</label>
        <select id="category" v-model="selectedCategory" class="form-control">
          <option v-for="category in categories" :key="category" :value="category">{{ category }}</option>
        </select>
      </div>
      <div class="form-group">
        <input type="file" @change="handleFileUpload" />
        <div v-if="uploadedImageUrl">
          <img :src="uploadedImageUrl" alt="Uploaded Picture" style="width: 200px; height: auto;">
        </div>
      </div>
      <button type="submit" class="submit-btn">게시글 생성</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

import { useRouter } from 'vue-router'

const title = ref('')
const contents = ref('')
const router = useRouter()
const categories = ref(['자유', '자랑', '정보', '산책후기']);
const selectedCategory = ref('');
const fileAttached = ref(); // fileAttached 추후 삭제
const boardFile = ref(null); // 파일 데이터를 저장할 ref
// 이미지 첨부 핸들러
const uploadedImageUrl = ref(null);
const handleFileUpload = (event) => {
  const file = event.target.files[0];
  boardFile.value = file;
  // Optional: Implement client-side logic to preview the image before uploading
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      uploadedImageUrl.value = e.target.result;
    };
    reader.readAsDataURL(file);
  }
};

const createPost = async function () {
  // FormData 객체를 생성하여 보낼 데이터를 저장합니다
  const formData = new FormData();
  // 'title'을 formData에 추가합니다. 'title.value'는 제목 입력 필드에서 얻은 값입니다
  formData.append('title', title.value);
  // 'contents'를 formData에 추가합니다. 'contents.value'는 내용 텍스트영역에서 얻은 값입니다
  formData.append('contents', contents.value);
  // 'boardCategory'를 formData에 추가합니다. 'selectedCategory.value'는 카테고리 드롭다운에서 선택된 값입니다
  formData.append('boardCategory', selectedCategory.value);
  // 파일이 첨부되었는지 확인합니다. 첨부되었으면 'fileAttached'를 1로, 그렇지 않으면 0으로 설정합니다
  formData.append('fileAttached', boardFile.value ? 1 : 0);
  // 파일이 첨부되었다면, 해당 파일을 formData에 추가합니다
  if (boardFile.value) {
    formData.append('boardFile', boardFile.value);
  }

  // (참고: FormData의 내용은 직접적으로 로깅할 수 없습니다)
  // axios를 사용하여 AJAX POST 요청을 수행합니다
  // FormData의 내용을 순회하면서 콘솔에 출력 , formdata 로깅 할때는 entries 사용해야 한다
  for (let [key, value] of formData.entries()) {
    console.log(key, value);
  }
  try {
    // axios.post 호출을 await로 처리하여 HTTP 요청의 완료를 기다립니다
    await axios.post(`/api/board/save`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }});
    // 요청이 성공적일 경우, 'communities' 경로로 라우팅합니다
    router.push({ name: 'communities' });
  } catch (err) {
    // 오류가 발생한 경우, 콘솔에 에러 로깅합니다
    console.error("Post creation failed: ", err);
  }
  // try {
  //   // Omitting the Content-Type in headers so the browser sets it automatically with the correct boundary
  //   const response = await fetch(`/api/board/save`, {
  //     method: 'POST', // Use 'POST' in uppercase
  //     body: formData, // Pass the formData directly as the body
  //     // Don't set 'Content-Type' header manually for FormData
  //   });
  //
  //   // Check if the fetch request was successful
  //   if (response.ok) {
  //     // If the request is successful, route to 'communities' path
  //     router.push({ name: 'communities' });
  //   } else {
  //     // If the server responds with an error status, throw an error
  //     throw new Error('Failed to create post. Server responded with an error.');
  //   }
  // } catch (err) {
  //   // If an error occurs, log the error to the console
  //   console.error("Post creation failed: ", err);
  // }
};

</script>
<style scoped>
.post-creation-container {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
    /* box-shadow: 0 0 10px rgba(0,0,0,0.1); */
    display: flex; 
    flex-direction: column;
    justify-content: center;
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