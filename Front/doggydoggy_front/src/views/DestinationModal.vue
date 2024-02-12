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