<template>
  <div>
    <div class="component" @click="close"></div>
    <div class="modal-component">
      <button class="modal-exit-btn" @click="close">
        <span>X</span>
      </button>
      <div class="modal-header">Do you want to create a marker?</div>
      <div class="modal-input-container">
        <div v-if="imageData" class="preview-container" @click="triggerFileInput">
          <img :src="imageData" class="preview-image"/>
        </div>
        <div v-else class="preview-container" @click="triggerFileInput">
          <img class="placeholder-image" src="@/image-solid.svg"/>
        </div>
        <div class="input-container">
          <input v-model="title" placeholder="Please enter a location." type="text"/>
          <textarea
              ref="contentInput"
              v-model="content"
              placeholder="Please enter a content."
              rows="3"
              @keydown.enter.exact.prevent="addNewline"
          ></textarea>
          <input ref="fileInput" class="hidden-file-input" type="file" @change="onFileChange"/>
        </div>
      </div>
      <div class="modal-button-container">
        <button class="modal-submit-btn" @click="addMarker">
          Create marker
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import {ref} from 'vue';
import axios from 'axios';

export default {
  props: ['latitude', 'longitude', 'close', 'isEditMode', 'existingMarkerData'],
  emits: ['marker-added'],
  setup(props, {emit}) {
    const title = ref('');
    const content = ref('');
    const imageData = ref('');
    const imageFile = ref(null); // 이미지 파일을 저장하기 위한 참조 추가
    const fileInput = ref(null);
    const contentInput = ref(null);

    if (props.isEditMode && props.existingMarkerData) {
      title.value = props.existingMarkerData.markerTitle;
      content.value = props.existingMarkerData.markerContents;
      // 이미지 처리 로직 추가 필요
    }

    const onFileChange = (e) => {
      const files = e.target.files;
      if (files.length === 0) {
        return;
      }
      const fileReader = new FileReader();
      fileReader.onload = (event) => {
        imageData.value = event.target.result;
      };
      fileReader.readAsDataURL(files[0]);
      imageFile.value = files[0]; // 이미지 파일 저장
    };

    const checkInputLengthAndJump = (currentValue, nextInputRef) => {
      if (currentValue.length >= 12) {
        // Adds a newline at the 12th character
        content.value = currentValue.substring(0, 12) + "\n" + currentValue.substring(12);
      }
    };

    const addNewline = (inputRef) => {
      const cursorPosition = inputRef.value.selectionStart;
      const textBeforeCursor = content.value.substring(0, cursorPosition);
      const textAfterCursor = content.value.substring(cursorPosition);
      content.value = textBeforeCursor + "\n" + textAfterCursor;
      // Focus back and set cursor position
      Vue.nextTick(() => {
        inputRef.value.focus();
        inputRef.value.selectionStart = cursorPosition + 1;
        inputRef.value.selectionEnd = cursorPosition + 1;
      });
    };

    const triggerFileInput = () => {
      if (fileInput.value) {
        fileInput.value.click(); // Trigger the file input click event
      }
    };


    const addMarker = async () => {
      const formData = new FormData();
      formData.append('markerLatitude', props.latitude);
      formData.append('markerLongitude', props.longitude);
      formData.append('markerTitle', title.value);
      formData.append('markerContents', content.value);
      if (imageFile.value) {
        formData.append('markerBoardFile', imageFile.value); // 이미지 파일 추가
      }

      for (let [key, value] of formData.entries()) {
        console.log(key, value);
      }

      try {
        const response = await axios({
          method: 'post',
          url: '/api/marker/save',
          data: formData,
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });

        const newMarker = response.data;
        emit('marker-added', newMarker);
        props.close();
      } catch (error) {
        console.error('Marker creation failed:', error);
      }
    };

    return {
      title, content, imageData, addMarker, onFileChange, triggerFileInput, fileInput, checkInputLengthAndJump,
      contentInput, addNewline
    };
  },
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
  width: 100%; /* 모달의 폭을 설정합니다. */
  height: 280px;
  max-height: 600px;
  max-width: 500px; /* 모달의 최대 폭을 설정합니다. */
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
  justify-content: center; /* Center items horizontally */
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

.modal-button-container {
  display: flex;
  justify-content: center; /* Center the button */
  margin-top: 50px; /* Add space above the button */
}

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