<template>
  <div>
    <!-- close 의 경우 부모 컴포넌트인 KakaoMapView.vue 에서 받아온 데이터 -->
  <div class="component" @click="close"></div>
    <div class="modal-component">
    <button class="modal-exit-btn" @click="close">
      <span>X</span> <!-- Replaced CloseIcon with text for simplicity -->
    </button>
    <div class="modal-header">도착지점 설정</div>
    
    <div class="modal-button-container">
      <button class="modal-submit-btn" @click="confirmDestination">완료</button>
    </div>
  </div>
</div>
</template>

<script>
import { ref, defineProps } from 'vue';

export default {
  props: ['latitude', 'longitude', 'close'],
  emits: ['destination-confirmed'],

  setup(props, { emit }) {
  const confirmDestination = () => {
  emit('destination-confirmed');
  props.close(); // Close the modal
};

return { confirmDestination };
}

};
</script>


<style scoped>
.component {
  /* 배경을 어둡게 하여 모달이 돋보이도록 설정합니다. */
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;

  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-component {
  /* 모달의 크기와 배경, 테두리 등의 스타일을 지정합니다. */
  width: 300px; /* 모달의 폭을 설정합니다. */
  height: 150px;
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  position: relative;
}

.modal-header {
  /* 모달의 헤더 스타일을 지정합니다. */
  margin-bottom: 20px;
  text-align: center;
  font-size: 1.5em; /* 헤더의 글자 크기를 설정합니다. */
}

.modal-input {
  /* 입력 필드의 스타일을 지정합니다. */
  margin-bottom: 20px;
}

.modal-button-container {
  /* Specifies the style of the button container. */
  display: flex;
  height: 50px;
  justify-content: center; /* Center items horizontally */
  margin: 0; /* 마진 제거 */
  padding: 0; /* 패딩 제거 */
}

.modal-submit-btn {
  /* Specifies the style of the 'Create Marker' button. */
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1em; /* Set the font size of the button. */
  margin-bottom: 10px;
}

.modal-exit-btn {
  /* 닫기(X) 버튼의 스타일을 지정합니다. */
  position: absolute;
  top: 10px;
  right: 10px;
  border: none;
  background: none;
  cursor: pointer;
  font-size: 1em; /* 버튼의 글자 크기를 설정합니다. */
}

.preview-image {
  /* 이미지 미리보기의 스타일을 지정합니다. */
  max-width: 80%; /* 이미지의 최대 폭을 설정합니다. */
  max-height: 150px; /* 이미지의 최대 높이를 설정합니다. */
  margin-top: 10px; /* 이미지 위에 공간을 추가합니다. */
  margin-left: 20px;
}

.modal-input-container {
  display: flex;
  justify-content: space-between; /* Align children to opposite ends */
  margin-bottom: 20px;
}

.preview-container {
  flex-basis: 40%; /* Adjust this value as needed */
  max-height: 100px; /* Adjust this value as needed */
}

.input-container {
  flex-basis: 58%; /* Adjust this value as needed, making sure the total with preview is less than 100% */
  display: flex;
  flex-direction: column; /* Stack the inputs vertically */
  justify-content: space-between; /* This will ensure space between inputs and file upload */
}

.input-container input[type="text"] {
  height: 30px; /* Adjust the height as desired */
  margin-bottom: 20px; /* Adjust the space between inputs as desired */
  padding: 0 10px; /* Add padding inside the input for better text alignment */
  border: 1px solid #ccc; /* Add a border to the input */
  border-radius: 4px; /* Round the corners of the input fields */

}


.input-container textarea {
  height: 80px; /* Adjust the height as desired */
  margin-bottom: 10px; /* Adjust the space between inputs as desired */
  font-size: 1em; /* Adjust font size as needed */
  padding: 0 10px; /* Add padding inside the input for better text alignment */
  border: 1px solid #ccc; /* Add a border to the input */
  border-radius: 4px; /* Round the corners of the input fields */
  width: 100%; /* Ensure the input takes the full width of the container */
  box-sizing: border-box; /* Include padding and border in the element's total width and height */
}

/* Style specifically for the textarea to prevent resizing */
.input-container textarea {
  resize: none; /* Prevent resizing of the textarea */
  overflow: auto; /* Add scroll for overflow text */
}

/* Existing styles remain unchanged */

.placeholder-image {
  /* Adjust the size as needed */
  width: 100px; /* Example size */
  height: 80px; /* Example size */
  cursor: pointer; /* Indicates that the image is clickable */
  margin-left: 20px;
}

.hidden-file-input {
  display: none; /* Hide the file input */
}
</style>