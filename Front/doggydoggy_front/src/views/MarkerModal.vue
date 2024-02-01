<template>
  <div>
    <!-- close 의 경우 부모 컴포넌트인 KakaoMapView.vue 에서 받아온 데이터 -->
  <div class="component" @click="close"></div>
    <div class="modal-component">
    <button class="modal-exit-btn" @click="close">
      <span>X</span> <!-- Replaced CloseIcon with text for simplicity -->
    </button>
    <div class="modal-header">Do you want to create a marker?</div>
    <div class="modal-input">
      <input type="text" placeholder="Please enter a location." v-model="title" />
      <input type="text" placeholder="Please enter a content." v-model="content" />
      <input type="file" @change="onFileChange" />
      
      <!-- imageData가 존재할 때만 표시 -->
      <div v-if="imageData">
        <img :src="imageData" class="preview-image" />
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
import { ref, defineProps } from 'vue';
import { v4 as uuidv4 } from 'uuid';

export default {
  props: ['latitude', 'longitude', 'close'],
  emits: ['marker-added'],

/*
setup에서 props 사용
•	목적: 부모 컴포넌트로부터 데이터를 받기 위함입니다.
•	사용하지 않는 경우: setup 함수 내에서 부모 컴포넌트로부터 전달된 데이터에 접근할 수 없게 됨

setup에서 emit 사용:
•	목적: 부모 컴포넌트에 사용자 정의 이벤트를 보내기 위함입니다.
•	사용하지 않는 경우: 컴포넌트가 부모 컴포넌트에게 정보를 전달하거나 상태 업데이트를 할 수 없게 됨
*/


  setup(props, { emit }) {
    const title = ref('');
    const content = ref('');
    const imageData = ref('');

    const addMarker = () => {
      const newMarkerId = uuidv4();
      const newMarker = {
        id: newMarkerId,
        latitude: props.latitude,
        longitude: props.longitude,
        title: title.value,
        content: content.value,
        image: imageData.value,
      };
      // localStorage 문법 : getItem() - value 읽어 오기
      // parse => JSON text를 JavaScript 객체로 변환
      // true 면 JSON.parse(localStorage.getItem('markers')) 반환 false 면 [] 빈 배열 반환
      const markers = JSON.parse(localStorage.getItem('markers')) || [];
      markers.push(newMarker);

      // localStorage 문법 : setItem() - key, value 추가
      //stringify => JavaScript 객체를 JSON text로 변환
      localStorage.setItem('markers', JSON.stringify(markers));
      console.log(markers)
      //emit 함수를 사용하여 marker-added 이벤트를 발생시키고, newMarker 객체를 이벤트의 데이터로 전달
      emit('marker-added', newMarker);
      //모달 닫기
      props.close();
    };

    const onFileChange = (e) => {
      const files = e.target.files;
      // 파일이 없으면 넘어감
      if (files.length === 0) {
        return;
      }
      // [JavaScript] 파일 입출력 문법인 FileReader 사용
      const fileReader = new FileReader();
      //로드될 때 result 가져오기
      fileReader.onload = (event) => {
        //불러온 결과(이미지)를 imageDate에 넣기
        imageData.value = event.target.result;
      };
      //파일 데이터를 URL로 변환하기
      fileReader.readAsDataURL(files[0]);
    };

    return { title, content, imageData, addMarker, onFileChange };
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