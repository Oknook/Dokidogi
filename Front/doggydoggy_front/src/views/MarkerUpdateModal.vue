<template>
  <div>
    <div class="component" @click="close"></div>
    <div class="modal-component">
      <button class="modal-exit-btn" @click="close">
        <span>X</span>
      </button>
      <div class="modal-header">Do you want to update a marker?</div>
      <div class="modal-input">
        <input v-model="title" placeholder="Please enter a location." type="text"/>
        <input v-model="content" placeholder="Please enter a content." type="text"/>
        <input type="file" @change="onFileChange"/>
        <div v-if="imageData">
          <img :src="imageData" class="preview-image"/>
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

    const addOrUpdateMarker = async () => {
      const formData = new FormData();
      formData.append('markerLatitude', props.latitude);
      formData.append('markerLongitude', props.longitude);
      formData.append('markerTitle', title.value);
      formData.append('markerContents', content.value);
      if (imageFile.value) {
        formData.append('markerBoardFile', imageFile.value); // 이미지 파일 추가
      }

      // URL과 HTTP 메서드를 수정 모드에 따라 조정
      let url = '/api/marker/save';
      let method = 'post';
      if (props.isEditMode && props.existingMarkerData && props.existingMarkerData.markerId) {
        url = `/api/marker/${props.existingMarkerData.markerId}/update`; // 가정: 마커 업데이트 API 엔드포인트
        method = 'put'; // 가정: 마커 업데이트에 PUT 메서드 사용
      }

      try {
        const response = await axios({
          method,
          url,
          data: formData,
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });

        // 성공적으로 마커를 추가하거나 수정한 후의 로직
        if (props.isEditMode) {
          emit('marker-updated', response.data); // 마커가 성공적으로 업데이트됐음을 부모 컴포넌트에 알림
        } else {
          emit('marker-added', response.data); // 마커가 성공적으로 추가됐음을 부모 컴포넌트에 알림
        }
        props.close();
      } catch (error) {
        console.error('Marker operation failed:', error);
      }
    };

    return {title, content, imageData, imageFile, addOrUpdateMarker, onFileChange};

  },
};
</script>

<style scoped>
.component {
  /* styles from Component */
}

.modal-component {
  /* styles from ModalComponent */
}

.modal-header {
  /* styles from ModalHeader */
}

.modal-input {
  /* styles from ModalInput */
}

.modal-button-container {
  /* styles from ModalButtonContainer */
}

.modal-submit-btn {
  /* styles from ModalSubmitBtn */
}

.modal-exit-btn {
  /* styles from ModalExitBtn */
}

.preview-image {
  max-width: 100%; /* Set a maximum width */
  max-height: 200px; /* Set a maximum height */
  margin-top: 10px; /* Add some space above the image */
}
</style>